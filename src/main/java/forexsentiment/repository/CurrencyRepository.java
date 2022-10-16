package forexsentiment.repository;

import java.util.List;

import forexsentiment.entity.Currency;
import forexsentiment.request.bean.CurrencyRequestBean;

public interface CurrencyRepository extends AbstractRepository {
	List<Currency> getCurrencyList(CurrencyRequestBean request);
}
