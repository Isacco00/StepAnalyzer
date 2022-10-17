package stepanalyzer.mapper;

import org.springframework.stereotype.Component;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.entity.Currency;

@Component
public class CurrencyMapper extends AbstractMapper<Currency, CurrencyBean> {

	protected CurrencyBean doMapping(Currency entity) {
		return doMapping(new CurrencyBean(), entity);
	}

	protected CurrencyBean doMapping(CurrencyBean bean, Currency entity) {
		bean.setTokenCurrency(entity.getTokenCurrency());
		bean.setCurrencyName(entity.getCurrencyName());
		return bean;
	}

}
