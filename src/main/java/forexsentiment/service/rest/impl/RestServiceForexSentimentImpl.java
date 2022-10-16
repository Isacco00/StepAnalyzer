package forexsentiment.service.rest.impl;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.manager.ForexSentimentManager;
import forexsentiment.request.bean.ForexSentimentRequestBean;
import forexsentiment.service.rest.RestServiceForexSentiment;

@Component
public class RestServiceForexSentimentImpl implements RestServiceForexSentiment {

	@Inject
	private ForexSentimentManager forexSentimentManager;

	@Override
	public List<ForexSentimentBean> getLastForexSentiments() {
		return forexSentimentManager.getLastForexSentiments();
	}

	@Override
	public List<List<ForexSentimentBean>> getForexSentimentListForHistory(ForexSentimentRequestBean requestBean) {
		return forexSentimentManager.getForexSentimentListForHistory(requestBean);
	}

	@Override
	public List<List<ForexSentimentBean>> getForexSentimentList(ForexSentimentRequestBean requestBean) {
		return forexSentimentManager.getForexSentimentList(requestBean);
	}

	@Override
	public List<ForexSentimentBean> saveLastForexSentimentToDb(boolean isValidForHistory) {
		return forexSentimentManager.saveLastForexSentimentToDb(isValidForHistory);
	}

	@Override
	public List<ForexSentimentBean> getAveregeForexSentiments(ForexSentimentRequestBean requestBean) {
		return forexSentimentManager.getAverageForexSentiments(requestBean);
	}

	@Override
	public void downloadForexSentimentsHistory(HttpServletResponse response) {
		forexSentimentManager.downloadForexSentimentsHistory(response);
	}

	@Override
	public List<ForexSentimentBean> getForexSentimentHistory(ForexSentimentRequestBean requestBean) {
		return forexSentimentManager.getForexSentimentHistory(requestBean);
	}

}
