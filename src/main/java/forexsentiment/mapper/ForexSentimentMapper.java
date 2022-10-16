package forexsentiment.mapper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.entity.ForexSentiment;

@Component
public class ForexSentimentMapper extends AbstractMapper<ForexSentiment, ForexSentimentBean> {

	@Inject
	private CurrencyMapper currencyMapper;

	protected ForexSentimentBean doMapping(ForexSentiment entity) {
		return doMapping(new ForexSentimentBean(), entity);
	}

	protected ForexSentimentBean doMapping(ForexSentimentBean bean, ForexSentiment entity) {
		bean.setTokenForexSentiment(entity.getTokenForexSentiment());
		bean.setUpdateTimestamp(entity.getUpdateTimestamp());
		bean.setLongPosition(entity.getLongPosition());
		bean.setShortPosition(entity.getShortPosition());

		if (entity.getCurrency() != null) {
			CurrencyBean currency = currencyMapper.mapEntityToBean(entity.getCurrency());
			bean.setCurrency(currency);
		}
		bean.setValidForHistory(entity.isValidForHistory());
		return bean;
	}

}
