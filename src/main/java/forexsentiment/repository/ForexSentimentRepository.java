package forexsentiment.repository;

import java.time.OffsetDateTime;
import java.util.List;

import forexsentiment.entity.ForexSentiment;
import forexsentiment.request.bean.ForexSentimentRequestBean;

public interface ForexSentimentRepository extends AbstractRepository {

	List<ForexSentiment> getLastForexSentiments(int version);

	void deleteDailyForexSentiment(OffsetDateTime lteDate);

	List<ForexSentiment> getForexSentimentList(ForexSentimentRequestBean requestBean);
	
	List<ForexSentiment> getForexSentimentHistory(ForexSentimentRequestBean requestBean);
}
