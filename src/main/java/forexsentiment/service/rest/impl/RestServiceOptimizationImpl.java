package forexsentiment.service.rest.impl;

import forexsentiment.bean.OptimizationBean;
import forexsentiment.manager.OptimizationManager;
import forexsentiment.service.rest.RestServiceOptimization;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.List;

@Component
public class RestServiceOptimizationImpl implements RestServiceOptimization {

	@Inject
	private OptimizationManager optimizationManager;

	@Override
	public List<OptimizationBean> downloadOptimization(String fileUrl) throws MalformedURLException {
		return optimizationManager.downloadOptimization(fileUrl);
	}

}
