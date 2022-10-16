package forexsentiment.repository.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import forexsentiment.entity.ForexSentimentVersion;
import forexsentiment.repository.ForexSentimentVersionRepository;

@Repository
public class ForexSentimentVersionImpl extends AbstractRepositoryImpl implements ForexSentimentVersionRepository {

	@Override
	public ForexSentimentVersion getMostRecentForexSentimentVersion() {
		Class<ForexSentimentVersion> clazz = ForexSentimentVersion.class;
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT f FROM " + clazz.getSimpleName() + " f ");

		StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");

		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<ForexSentimentVersion> query = entityManager.createQuery(strQueryFinal, clazz);
		parameters.forEach(query::setParameter);
		return getResultSingle(query);
	}

}