package stepanalyzer.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import stepanalyzer.entity.Currency;
import stepanalyzer.repository.CurrencyRepository;
import stepanalyzer.request.bean.CurrencyRequestBean;

@Repository
public class CurrencyRepositoryImpl extends AbstractRepositoryImpl implements CurrencyRepository {

	@Override
	public List<Currency> getCurrencyList(CurrencyRequestBean request) {
		Class<Currency> clazz = Currency.class;
		Map<String, Object> parameters = new HashMap<>();

		StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT c FROM " + clazz.getSimpleName() + " c ");
		StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");

		// Parameters
		if (request != null) {
			if (request.getCurrencyName() != null) {
				strQueryWhere.append(" AND c.currencyName like :currencyIsoCode ");
				parameters.put("currencyIsoCode", "%" + request.getCurrencyName() + "%");
			}
		}
		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<Currency> query = entityManager.createQuery(strQueryFinal, clazz);
		parameters.forEach(query::setParameter);
		return getResultList(query);
	}
}
