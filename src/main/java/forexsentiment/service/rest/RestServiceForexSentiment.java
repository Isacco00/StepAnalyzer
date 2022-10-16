package forexsentiment.service.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import forexsentiment.bean.ForexSentimentBean;
import forexsentiment.request.bean.ForexSentimentRequestBean;

@RestController
@RequestMapping("/forexSentiment")
public interface RestServiceForexSentiment {

	@GetMapping("/getLastForexSentiments")
	public List<ForexSentimentBean> getLastForexSentiments();

	@GetMapping("/getForexSentimentListForHistory")
	public @ResponseBody List<List<ForexSentimentBean>> getForexSentimentListForHistory(
			ForexSentimentRequestBean requestBean);

	@GetMapping("/getForexSentimentList")
	public @ResponseBody List<List<ForexSentimentBean>> getForexSentimentList(ForexSentimentRequestBean requestBean);

	@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
	@GetMapping("/saveLastForexSentiment")
	public @ResponseBody List<ForexSentimentBean> saveLastForexSentimentToDb(
			@NotNull @RequestParam boolean isValidForHistory);

	@GetMapping("/getAveregeForexSentiments")
	public List<ForexSentimentBean> getAveregeForexSentiments(ForexSentimentRequestBean requestBean);

	@GetMapping("/downloadForexSentimentsHistory")
	public void downloadForexSentimentsHistory(HttpServletResponse response);

	@GetMapping("/getForexSentimentHistory")
	public List<ForexSentimentBean> getForexSentimentHistory(ForexSentimentRequestBean requestBean);
}
