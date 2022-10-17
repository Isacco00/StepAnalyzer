package stepanalyzer.manager;

import java.util.List;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.request.bean.CurrencyRequestBean;

public interface CurrencyManager {

	List<CurrencyBean> getCurrencyList(CurrencyRequestBean currencyRequestBean);
}
