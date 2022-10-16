package forexsentiment.manager.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.entity.ForexClientSentiment;
import forexsentiment.entity.ForexSentiment;
import forexsentiment.entity.ForexSentimentVersion;
import forexsentiment.entity.FxCoSentiment;
import forexsentiment.entity.MyFxBookSentiment;
import forexsentiment.exception.ValidationException;
import forexsentiment.manager.ForexSentimentManager;
import forexsentiment.mapper.CurrencyMapper;
import forexsentiment.mapper.ForexSentimentMapper;
import forexsentiment.merger.ForexClientMerger;
import forexsentiment.merger.ForexSentimentMerger;
import forexsentiment.merger.FxCoMerger;
import forexsentiment.merger.MyFxBookMerger;
import forexsentiment.repository.CurrencyRepository;
import forexsentiment.repository.ForexSentimentRepository;
import forexsentiment.repository.ForexSentimentVersionRepository;
import forexsentiment.request.bean.ForexSentimentRequestBean;
import forexsentiment.utility.CalcUtility;
import forexsentiment.utility.CollectionUtils;
import forexsentiment.utility.CsvUtility;

@Service
@Transactional
public class ForexSentimentManagerImpl implements ForexSentimentManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForexSentimentManagerImpl.class);

	private static final int NUMBER_OF_COMMON_PAIR = 30;
	@Inject
	private CurrencyRepository currRepo;
	@Inject
	private CurrencyMapper currMapper;
	@Inject
	private ForexSentimentMerger merger;
	@Inject
	private MyFxBookMerger myFxBookMerger;
	@Inject
	private FxCoMerger fxCoMerger;
	@Inject
	private ForexClientMerger forexClientMerger;
	@Inject
	private ForexSentimentMapper mapper;
	@Inject
	private ForexSentimentRepository repo;
	@Inject
	private ForexSentimentVersionRepository repoVersion;

	@Inject
	private CalcUtility calcUtility;
	@Inject
	private CsvUtility csvUtility;

	@Override
	public List<ForexSentimentBean> getLastForexSentiments() {
		ForexSentimentVersion version = repoVersion.getMostRecentForexSentimentVersion();
		if (version != null) {
			List<ForexSentimentBean> bean = mapper
					.mapEntitiesToBeans(repo.getLastForexSentiments(version.getMostRecentVersion()));
			checkConsistencyData(bean, 31, "final");
			return bean;
		} else {
			LOGGER.warn("No data found");
			throw new ValidationException("No data found");
		}
	}

	@Override
	public List<List<ForexSentimentBean>> getForexSentimentListForHistory(ForexSentimentRequestBean requestBean) {
		// Sentiments are grouped by date
		List<ForexSentimentBean> bean = mapper.mapEntitiesToBeans(repo.getForexSentimentList(requestBean));
		Comparator<List<ForexSentimentBean>> byToken = Comparator.comparing(e -> (e.get(0).getTokenForexSentiment()));
		return bean.stream().collect(Collectors.groupingBy(x -> x.getUpdateTimestamp().toString())).values().stream().sorted(byToken).collect(Collectors.toList());
	}

	@Override
	public List<List<ForexSentimentBean>> getForexSentimentList(ForexSentimentRequestBean requestBean) {
		// Sentiments are grouped by date
		List<ForexSentimentBean> bean = mapper.mapEntitiesToBeans(repo.getForexSentimentList(requestBean));
		Comparator<List<ForexSentimentBean>> byToken = Comparator.comparing(e -> (e.get(0).getTokenForexSentiment()));
		return bean.stream().collect(Collectors.groupingBy(x -> x.getCurrency().getTokenCurrency().toString()))
				.values().stream().sorted(byToken).collect(Collectors.toList());
	}

	@Override
	public List<ForexSentimentBean> saveLastForexSentimentToDb(boolean isHistorySaving) {
		LOGGER.info("Starting save new entity at " + LocalDateTime.now());
		List<CurrencyBean> currencies = currMapper.mapEntitiesToBeans(currRepo.getCurrencyList(null));
		ForexSentimentVersion version = repoVersion.getMostRecentForexSentimentVersion();
		if (version == null) {
			version = new ForexSentimentVersion();
			version.setTokenForexSentimentVersion(0L);
			version.setUpdateTimestamp(OffsetDateTime.now());
			version.setMostRecentVersion(1);
		} else {
			version.setUpdateTimestamp(OffsetDateTime.now());
			version.setMostRecentVersion(version.getMostRecentVersion() + 1);
		}
		List<ForexSentimentBean> aggregatedForexSentiment = getForexSentimentsFromProvider(currencies,
				version.getMostRecentVersion(), isHistorySaving);
		List<ForexSentiment> entities = new ArrayList<>();
		for (ForexSentimentBean bean : aggregatedForexSentiment) {
			ForexSentiment entity = merger.mapNew(bean, ForexSentiment.class);
			if (entity != null) {
				entities.add(entity);
				repo.saveOrUpdate(entity);
			}
		}
		repoVersion.saveOrUpdate(version);
		List<ForexSentimentBean> bean = mapper.mapEntitiesToBeans(entities);
		checkConsistencyData(bean, 31, "final");
		LOGGER.info("Save completed at " + LocalDateTime.now());
		return bean;
	}

	@Override
	public void checkConsistencyData(List<ForexSentimentBean> bean, int expectedSize, String checkDataFrom) {
		if (bean.isEmpty() || bean.size() != expectedSize) {
			LOGGER.info("Sentiment from " + checkDataFrom);
			LOGGER.error("Was expecting exactly " + (expectedSize) + "element, but found [" + bean.size() + "]");
			throw new ValidationException(
					"Was expecting exactly " + (expectedSize) + "element, but found [" + bean.size() + "]");
		}
	}

	private List<ForexSentimentBean> getForexSentimentsFromProvider(List<CurrencyBean> currencies, int version,
			boolean isHistorySaving) {
		List<ForexSentimentBean> myFxBookSentiments = getBeanMyFxbook(currencies);
		List<ForexSentimentBean> forexClientSentiments = getBeanForexClient(currencies);
		List<ForexSentimentBean> fxCoSentiments = getBeanFxCo(currencies);
		// List<ForexSentimentBean> forexFactory = getBeanForexFactory(currencies);

		if (!myFxBookSentiments.isEmpty()) {
			checkConsistencyData(myFxBookSentiments, 30, "myFxBook");
			for (ForexSentimentBean bean : myFxBookSentiments) {
				MyFxBookSentiment entity = myFxBookMerger.mapNew(bean, MyFxBookSentiment.class);
				if (entity != null) {
					repo.saveOrUpdate(entity);
				}
			}
		} else {
			LOGGER.info("Sentiment from myfxbook is broken");
		}
		if (!fxCoSentiments.isEmpty()) {
			checkConsistencyData(fxCoSentiments, 30, "fxCoSentiment");
			for (ForexSentimentBean bean : myFxBookSentiments) {
				FxCoSentiment entity = fxCoMerger.mapNew(bean, FxCoSentiment.class);
				if (entity != null) {
					repo.saveOrUpdate(entity);
				}
			}
		} else {
			LOGGER.info("Sentiment from fxCoSentiments is broken");
		}
		if (!forexClientSentiments.isEmpty()) {
			checkConsistencyData(forexClientSentiments, 31, "forexClient");
			for (ForexSentimentBean bean : forexClientSentiments) {
				ForexClientSentiment entity = forexClientMerger.mapNew(bean, ForexClientSentiment.class);
				if (entity != null) {
					repo.saveOrUpdate(entity);
				}
			}
		} else {
			LOGGER.info("Sentiment from forexClientSentiments is broken");
		}
		List<ForexSentimentBean> aggregatedSentiments = aggregateSentiments(myFxBookSentiments, forexClientSentiments,
				fxCoSentiments, version, isHistorySaving);
		checkConsistencyData(aggregatedSentiments,
				!forexClientSentiments.isEmpty() ? 31 : 30, "final");
		return aggregatedSentiments;
	}

	private List<ForexSentimentBean> aggregateSentiments(List<ForexSentimentBean> myFxBookSentiments,
			List<ForexSentimentBean> forexClientSentiments, List<ForexSentimentBean> fxCoSentiments, int version,
			boolean isHistorySaving) {
		List<ForexSentimentBean> aggregatedSentiments = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_COMMON_PAIR; i++) {
			ForexSentimentBean myFxBookSentiment = myFxBookSentiments != null && !myFxBookSentiments.isEmpty()
					? myFxBookSentiments.get(i)
					: null;
			ForexSentimentBean forexClientSentiment = forexClientSentiments != null && !forexClientSentiments.isEmpty()
					? forexClientSentiments.get(i)
					: null;
			ForexSentimentBean fxCoSentiment = fxCoSentiments != null && !fxCoSentiments.isEmpty()
					? fxCoSentiments.get(i)
					: null;

			CurrencyBean currency = checkIfSameCurrency(myFxBookSentiment, forexClientSentiment, fxCoSentiment);

			List<BigDecimal> longPositionForAverage = new ArrayList<>();
			List<BigDecimal> shortPositionForAverage = new ArrayList<>();

			if (myFxBookSentiment != null) {
				longPositionForAverage.add(myFxBookSentiment.getLongPosition());
				shortPositionForAverage.add(myFxBookSentiment.getShortPosition());
			}
			if (forexClientSentiment != null) {
				longPositionForAverage.add(forexClientSentiment.getLongPosition());
				shortPositionForAverage.add(forexClientSentiment.getShortPosition());
			}
			if (fxCoSentiment != null) {
				longPositionForAverage.add(fxCoSentiment.getLongPosition());
				shortPositionForAverage.add(fxCoSentiment.getShortPosition());
			}

			ForexSentimentBean fs = new ForexSentimentBean();
			fs.setTokenForexSentiment(0L);
			fs.setVersion(version);
			fs.setCurrency(currency);
			fs.setUpdateTimestamp(OffsetDateTime.now());
			BigDecimal longPosition = calcUtility.averageBigDecimalValue(longPositionForAverage, z -> z);
			fs.setLongPosition(longPosition);
			BigDecimal shortPosition = calcUtility.averageBigDecimalValue(shortPositionForAverage, z -> z);
			fs.setShortPosition(shortPosition);
			fs.setValidForHistory(isHistorySaving);
			aggregatedSentiments.add(fs);
		}
		// Add USOIL
		if (forexClientSentiments != null && !forexClientSentiments.isEmpty()) {
			ForexSentimentBean fs = new ForexSentimentBean();
			fs.setTokenForexSentiment(0L);
			fs.setVersion(version);
			fs.setCurrency(forexClientSentiments.get(30).getCurrency());
			fs.setUpdateTimestamp(OffsetDateTime.now());
			fs.setLongPosition(forexClientSentiments.get(30).getLongPosition());
			fs.setShortPosition(forexClientSentiments.get(30).getShortPosition());
			fs.setValidForHistory(isHistorySaving);
			aggregatedSentiments.add(fs);
		}
		return aggregatedSentiments;
	}

	private CurrencyBean checkIfSameCurrency(ForexSentimentBean myFxBookSentiment,
			ForexSentimentBean forexClientSentiment, ForexSentimentBean fxCoSentiment) {
		Long value1 = myFxBookSentiment != null ? myFxBookSentiment.getCurrency().getTokenCurrency() : null;
		Long value2 = forexClientSentiment != null ? forexClientSentiment.getCurrency().getTokenCurrency() : null;
		Long value3 = fxCoSentiment != null ? fxCoSentiment.getCurrency().getTokenCurrency() : null;
		if (value1 == null) {
			if (value2 == null) {
				if (value3 != null) {
					return fxCoSentiment.getCurrency();
				} else {
					LOGGER.error("Check data. Was expected the same currency for all sentiment");
					throw new ValidationException("Check data. Was expected the same currency for all sentiment");
				}
			} else {
				if (value3 != null) {
					if (value2.equals(value3)) {
						return fxCoSentiment.getCurrency();
					} else {
						LOGGER.error("Check data. Was expected the same currency for all sentiment");
						throw new ValidationException("Check data. Was expected the same currency for all sentiment");
					}
				} else {
					return forexClientSentiment.getCurrency();
				}
			}
		} else {
			if (value2 == null) {
				if (value3 == null) {
					return myFxBookSentiment.getCurrency();
				} else {
					if (value1.equals(value3)) {
						return myFxBookSentiment.getCurrency();
					} else {
						LOGGER.error("Check data. Was expected the same currency for all sentiment");
						throw new ValidationException("Check data. Was expected the same currency for all sentiment");
					}
				}
			} else {
				if (value3 != null) {
					if (value1.equals(value2) && value2.equals(value3)) {
						return myFxBookSentiment.getCurrency();
					} else {
						LOGGER.error("Check data. Was expected the same currency for all sentiment");
						throw new ValidationException("Check data. Was expected the same currency for all sentiment");
					}
				} else {
					if (value1.equals(value2)) {
						return myFxBookSentiment.getCurrency();
					} else {
						LOGGER.error("Check data. Was expected the same currency for all sentiment");
						throw new ValidationException("Check data. Was expected the same currency for all sentiment");
					}
				}
			}
		}
	}

	private List<ForexSentimentBean> getBeanFxCo(List<CurrencyBean> currencies) {
		List<String> currenciesMyFxBook = Arrays.asList("AUDCAD", "AUDCHF", "AUDJPY", "AUDNZD", "AUDUSD", "CADCHF",
				"CADJPY", "CHFJPY", "EURAUD", "EURCAD", "EURCHF", "EURGBP", "EURJPY", "EURNZD", "EURUSD", "GBPAUD",
				"GBPCAD", "GBPCHF", "GBPJPY", "GBPNZD", "GBPUSD", "NZDCAD", "NZDCHF", "NZDJPY", "NZDUSD", "USDCAD",
				"USDCHF", "USDJPY", "GOLD", "SILVER");
		List<ForexSentimentBean> sentiments = new ArrayList<>();
		String url = "https://www.fx.co/it/quote/";
		OffsetDateTime update = OffsetDateTime.now();
		try {
			for (String currency : currenciesMyFxBook) {
				String finalUrl = url + currency.toLowerCase();
				Document document = Jsoup.connect(finalUrl).get();
				List<Element> rows = document.select("div.block-quote__position div"); // td
				if (!rows.isEmpty()) {
					ForexSentimentBean bean = new ForexSentimentBean();
					bean.setTokenForexSentiment(0L);
					if (currency.equals("GOLD")) {
						bean.setCurrency(findCurrency(currencies, "XAUUSD", ""));
					} else if (currency.equals("SILVER")) {
						bean.setCurrency(findCurrency(currencies, "XAGUSD", ""));
					} else {
						bean.setCurrency(findCurrency(currencies, currency, ""));
					}
					bean.setUpdateTimestamp(update);
					bean.setShortPosition(convertToNumber(rows.get(9).text(), "%"));
					bean.setLongPosition(convertToNumber(rows.get(4).text(), "%"));
					sentiments.add(bean);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sentiments;
	}

	private List<ForexSentimentBean> getBeanForexClient(List<CurrencyBean> currencies) {
		List<String> currenciesMyFxBook = Arrays.asList("AUD/CAD", "AUD/CHF", "AUD/JPY", "AUD/NZD", "AUD/USD",
				"CAD/CHF", "CAD/JPY", "CHF/JPY", "EUR/AUD", "EUR/CAD", "EUR/CHF", "EUR/GBP", "EUR/JPY", "EUR/NZD",
				"EUR/USD", "GBP/AUD", "GBP/CAD", "GBP/CHF", "GBP/JPY", "GBP/NZD", "GBP/USD", "NZD/CAD", "NZD/CHF",
				"NZD/JPY", "NZD/USD", "USD/CAD", "USD/CHF", "USD/JPY", "GOLD", "SILVER", "USOIL");
		OffsetDateTime update = OffsetDateTime.now();
		List<ForexSentimentBean> sentiments = new ArrayList<>();
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpGet request = new HttpGet("https://storage.googleapis.com/public-sentiments-v7/fxcs-sentiments.json");
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			HttpResponse response = httpClient.execute(request);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(EntityUtils.toString(response.getEntity()));
			// get a String from the JSON object
			JSONArray allSentiments = (JSONArray) jsonObject.get("sentiments");
			for (String currency : currenciesMyFxBook) {
				for (Object x : allSentiments) {
					JSONObject sentiment = (JSONObject) x;
					if (currency.equals(sentiment.get("instrument"))) {
						ForexSentimentBean bean = new ForexSentimentBean();
						bean.setTokenForexSentiment(0L);
						switch (currency) {
							case "GOLD" -> bean.setCurrency(findCurrency(currencies, "XAUUSD", ""));
							case "SILVER" -> bean.setCurrency(findCurrency(currencies, "XAGUSD", ""));
							case "USOIL" -> bean.setCurrency(findCurrency(currencies, "USOIL", ""));
							default -> bean.setCurrency(findCurrency(currencies, currency, "/"));
						}
						bean.setUpdateTimestamp(update);
						bean.setShortPosition(new BigDecimal(((Long) sentiment.get("short_percentage")).toString()));
						bean.setLongPosition(new BigDecimal(((Long) sentiment.get("long_percentage")).toString()));
						sentiments.add(bean);
					}
				}
			}

		} catch (IOException | ParseException | org.json.simple.parser.ParseException ex) {
			LOGGER.error(ex.getMessage());
		}
		return sentiments;
	}

	private List<ForexSentimentBean> getBeanMyFxbook(List<CurrencyBean> currencies) {
		List<String> currenciesMyFxBook = Arrays.asList("AUDCAD", "AUDCHF", "AUDJPY", "AUDNZD", "AUDUSD", "CADCHF",
				"CADJPY", "CHFJPY", "EURAUD", "EURCAD", "EURCHF", "EURGBP", "EURJPY", "EURNZD", "EURUSD", "GBPAUD",
				"GBPCAD", "GBPCHF", "GBPJPY", "GBPNZD", "GBPUSD", "NZDCAD", "NZDCHF", "NZDJPY", "NZDUSD", "USDCAD",
				"USDCHF", "USDJPY", "XAUUSD", "XAGUSD");
		List<ForexSentimentBean> sentiments = new ArrayList<>();
		String url = "https://www.myfxbook.com/community/outlook/";
		OffsetDateTime update = OffsetDateTime.now();
		try {
			for (String currency : currenciesMyFxBook) {
				String finalUrl = url + currency;
				Document document = Jsoup.connect(finalUrl).get();
				List<Element> rows = document.select("table#currentMetricsTable td"); // td
				if (!rows.isEmpty()) {
					ForexSentimentBean bean = new ForexSentimentBean();
					bean.setTokenForexSentiment(0L);
					bean.setCurrency(findCurrency(currencies, currency, ""));
					bean.setUpdateTimestamp(update);
					bean.setShortPosition(convertToNumber(rows.get(2).text(), " "));
					bean.setLongPosition(convertToNumber(rows.get(6).text(), " "));
					sentiments.add(bean);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sentiments;
	}

	private CurrencyBean findCurrency(List<CurrencyBean> currencies, String currency, String splitter) {
		String cleanCurrency = currency.replace(splitter, "");
		return CollectionUtils.getSingleElementOrNull(
				currencies.stream().filter(x -> x.getCurrencyName().equals(cleanCurrency)).toList());
	}

	private BigDecimal convertToNumber(String text, String splitter) {
		return new BigDecimal(text.split(splitter)[0]);
	}

	@Override
	public List<ForexSentimentBean> getAverageForexSentiments(ForexSentimentRequestBean requestBean) {
		List<ForexSentimentBean> allEntities = mapper.mapEntitiesToBeans(repo.getForexSentimentList(requestBean));
		Map<Long, List<ForexSentimentBean>> map = allEntities.stream()
				.collect(Collectors.groupingBy(x -> x.getCurrency().getTokenCurrency()));

		List<ForexSentimentBean> fsAverage = new ArrayList<>();
		for (Entry<Long, List<ForexSentimentBean>> elements : map.entrySet()) {
			ForexSentimentBean bean = new ForexSentimentBean();
			bean.setCurrency(elements.getValue().get(0).getCurrency());
			bean.setAverageLongPosition(
					calcUtility.averageBigDecimalValue(elements.getValue(), ForexSentimentBean::getLongPosition));
			bean.setAverageShortPosition(
					calcUtility.averageBigDecimalValue(elements.getValue(), ForexSentimentBean::getShortPosition));
			fsAverage.add(bean);
		}
		checkConsistencyData(fsAverage, 30, "final");
		return fsAverage;
	}

	@Override
	public void downloadForexSentimentsHistory(HttpServletResponse response) {
		LOGGER.info("Starting download forex sentiment at " + LocalDateTime.now());
		List<String> currencies = Arrays.asList("AUDCAD.csv", "AUDCHF.csv", "AUDJPY.csv", "AUDNZD.csv", "AUDUSD.csv",
				"CADCHF.csv", "CADJPY.csv", "CHFJPY.csv", "EURAUD.csv", "EURCAD.csv", "EURCHF.csv", "EURGBP.csv",
				"EURJPY.csv", "EURNZD.csv", "EURUSD.csv", "GBPAUD.csv", "GBPCAD.csv", "GBPCHF.csv", "GBPJPY.csv",
				"GBPNZD.csv", "GBPUSD.csv", "NZDCAD.csv", "NZDCHF.csv", "NZDJPY.csv", "NZDUSD.csv", "USDCAD.csv",
				"USDCHF.csv", "USDJPY.csv", "XAUUSD.csv", "XAGUSD.csv");
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=history.zip");
		List<List<ForexSentimentBean>> forexSentimentList = this.getForexSentimentList(null);
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
			for (int i = 0; i < 30; i++) {
				List<ForexSentimentBean> entity = forexSentimentList.get(i);
				InputStreamResource fileResource = new InputStreamResource(csvUtility.writeDataToCsv(entity));
				ZipEntry zipEntry = new ZipEntry(currencies.get(i));
				zipOutputStream.putNextEntry(zipEntry);
				StreamUtils.copy(fileResource.getInputStream(), zipOutputStream);
				zipOutputStream.closeEntry();
			}
			zipOutputStream.finish();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Override
	public List<ForexSentimentBean> getForexSentimentHistory(ForexSentimentRequestBean requestBean) {
		// Sentiments are grouped by date
		return mapper.mapEntitiesToBeans(repo.getForexSentimentHistory(requestBean));
	}
}
