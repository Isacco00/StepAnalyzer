package forexsentiment.manager;

import forexsentiment.bean.OptimizationBean;

import java.net.MalformedURLException;
import java.util.List;

public interface OptimizationManager {
    List<OptimizationBean> downloadOptimization(String fileUrl) throws MalformedURLException;
}
