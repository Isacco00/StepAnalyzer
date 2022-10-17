package stepanalyzer.repository;

import java.util.List;

import stepanalyzer.entity.Currency;
import stepanalyzer.request.bean.CurrencyRequestBean;

public interface CurrencyRepository extends AbstractRepository {
	List<Currency> getCurrencyList(CurrencyRequestBean request);
}
