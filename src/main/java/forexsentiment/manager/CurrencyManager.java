package forexsentiment.manager;

import java.util.List;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.request.bean.CurrencyRequestBean;

public interface CurrencyManager {

	List<CurrencyBean> getCurrencyList(CurrencyRequestBean currencyRequestBean);
}
