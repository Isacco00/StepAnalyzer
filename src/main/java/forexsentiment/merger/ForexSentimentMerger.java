package forexsentiment.merger;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.entity.Currency;
import forexsentiment.entity.ForexSentiment;
import forexsentiment.exception.EntityNotFoundException;
import forexsentiment.repository.CurrencyRepository;

@Component
public class ForexSentimentMerger extends AbstractMerger<ForexSentimentBean, ForexSentiment> {

	@Inject
	private CurrencyRepository currencyRepo;

	@Override
	protected void doMerge(ForexSentimentBean bean, ForexSentiment entity) {
		entity.setTokenForexSentiment(bean.getTokenForexSentiment());
		entity.setUpdateTimestamp(bean.getUpdateTimestamp());
		entity.setLongPosition(bean.getLongPosition());
		entity.setShortPosition(bean.getShortPosition());

		Currency currency = currencyRepo.find(Currency.class, bean.getCurrency().getTokenCurrency());
		if (currency == null) {
			throw new EntityNotFoundException(
					"Currency not found. Token Currency: " + bean.getCurrency().getTokenCurrency());
		}
		entity.setCurrency(currency);
		entity.setVersion(bean.getVersion());
		entity.setValidForHistory(bean.isValidForHistory());
	}

}
