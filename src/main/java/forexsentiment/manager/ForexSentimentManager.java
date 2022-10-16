package forexsentiment.manager;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.request.bean.ForexSentimentRequestBean;

public interface ForexSentimentManager {

	List<ForexSentimentBean> getLastForexSentiments();

	List<List<ForexSentimentBean>> getForexSentimentListForHistory(ForexSentimentRequestBean requestBean);
	
	List<ForexSentimentBean> getForexSentimentHistory(ForexSentimentRequestBean requestBean);

	List<List<ForexSentimentBean>> getForexSentimentList(ForexSentimentRequestBean requestBean);

	List<ForexSentimentBean> saveLastForexSentimentToDb(boolean isHistorySaving);

	void checkConsistencyData(List<ForexSentimentBean> bean, int expectedSize, String checkDataFrom);

	List<ForexSentimentBean> getAverageForexSentiments(ForexSentimentRequestBean requestBean);

	void downloadForexSentimentsHistory(HttpServletResponse response);
}
