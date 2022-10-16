package forexsentiment.service.rest.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.manager.CurrencyManager;
import forexsentiment.request.bean.CurrencyRequestBean;
import forexsentiment.service.rest.RestServiceCurrency;

@Component
public class RestServiceCurrencyImpl implements RestServiceCurrency {

	@Inject
	private CurrencyManager currencyManager;

	@Override
	public List<CurrencyBean> getCurrencyList(CurrencyRequestBean currencyRequestBean) {
		return currencyManager.getCurrencyList(currencyRequestBean);
	}
}
