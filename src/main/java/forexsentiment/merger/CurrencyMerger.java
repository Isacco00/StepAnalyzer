package forexsentiment.merger;

import org.springframework.stereotype.Component;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.entity.Currency;

@Component
public class CurrencyMerger extends AbstractMerger<CurrencyBean, Currency> {

	@Override
	protected void doMerge(CurrencyBean bean, Currency entity) {
		entity.setTokenCurrency(bean.getTokenCurrency());
		entity.setCurrencyName(bean.getCurrencyName());
	}

}
