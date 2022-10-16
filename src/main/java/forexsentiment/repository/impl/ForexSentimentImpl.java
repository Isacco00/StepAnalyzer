package forexsentiment.repository.impl;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import forexsentiment.entity.ForexSentiment;
import forexsentiment.repository.ForexSentimentRepository;
import forexsentiment.request.bean.ForexSentimentRequestBean;

@Repository
public class ForexSentimentImpl extends AbstractRepositoryImpl implements ForexSentimentRepository {

	@Override
	public List<ForexSentiment> getLastForexSentiments(int version) {
		Class<ForexSentiment> clazz = ForexSentiment.class;
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT f FROM " + clazz.getSimpleName() + " f ");
		StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");
		// Parameters
		strQueryWhere.append(" AND f.version like :version ");
		parameters.put("version", version);

		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<ForexSentiment> query = entityManager.createQuery(strQueryFinal, clazz);
		parameters.forEach(query::setParameter);
		return getResultList(query);
	}

	@Override
	public void deleteDailyForexSentiment(OffsetDateTime lteDate) {
		Class<ForexSentiment> clazz = ForexSentiment.class;
		javax.persistence.Query q = entityManager.createQuery("DELETE " + clazz.getSimpleName()
				+ " f WHERE f.updateTimestamp < '" + lteDate + "' AND f.isValidForHistory = false");
		q.executeUpdate();
	}

	@Override
	public List<ForexSentiment> getForexSentimentList(ForexSentimentRequestBean requestBean) {
		Class<ForexSentiment> clazz = ForexSentiment.class;
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT fs FROM " + clazz.getSimpleName() + " fs ");
		StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");
		// Parameters
		if (requestBean != null) {
			if (requestBean.getIsValidForHistory() != null) {
				strQueryWhere.append(" AND fs.isValidForHistory = :isValidForHistory ");
				parameters.put("isValidForHistory", requestBean.getIsValidForHistory());
			}
			if (requestBean.getSpecificDay() != null) {
				strQueryWhere.append(" AND fs.updateTimestamp LIKE '%" + requestBean.getSpecificDay() + "%' ");
			}
		}
		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<ForexSentiment> query = entityManager.createQuery(strQueryFinal, clazz);
		parameters.forEach(query::setParameter);
		return getResultList(query);
	}

	@Override
	public List<ForexSentiment> getForexSentimentHistory(ForexSentimentRequestBean requestBean) {
		Class<ForexSentiment> clazz = ForexSentiment.class;
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT fs FROM " + clazz.getSimpleName() + " fs ");
		StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");

		strQueryFrom.append("INNER JOIN fs.currency c ");
		// Parameters
		if (requestBean != null) {
			if (requestBean.getCurrency() != null) {
				strQueryWhere.append(" AND c.currencyName = :currencyName ");
				parameters.put("currencyName", requestBean.getCurrency());
			}
		}
		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<ForexSentiment> query = entityManager.createQuery(strQueryFinal, clazz);
		parameters.forEach(query::setParameter);
		return getResultList(query);
	}
}