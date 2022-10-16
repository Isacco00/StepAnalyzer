package forexsentiment.repository;

import forexsentiment.entity.ForexSentimentVersion;

public interface ForexSentimentVersionRepository extends AbstractRepository {
	ForexSentimentVersion getMostRecentForexSentimentVersion();
}
