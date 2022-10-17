package stepanalyzer.merger;

import org.springframework.stereotype.Component;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.entity.Currency;

@Component
public class CurrencyMerger extends AbstractMerger<CurrencyBean, Currency> {

	@Override
	protected void doMerge(CurrencyBean bean, Currency entity) {
		entity.setTokenCurrency(bean.getTokenCurrency());
		entity.setCurrencyName(bean.getCurrencyName());
	}

}
