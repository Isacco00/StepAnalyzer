package stepanalyzer.service.rest.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.manager.CurrencyManager;
import stepanalyzer.request.bean.CurrencyRequestBean;
import stepanalyzer.service.rest.RestServiceCurrency;

@Component
public class RestServiceCurrencyImpl implements RestServiceCurrency {

	@Inject
	private CurrencyManager currencyManager;

	@Override
	public List<CurrencyBean> getCurrencyList(CurrencyRequestBean currencyRequestBean) {
		return currencyManager.getCurrencyList(currencyRequestBean);
	}
}
