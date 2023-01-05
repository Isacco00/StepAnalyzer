package stepanalyzer.repository;

import stepanalyzer.entity.Step;
import stepanalyzer.request.bean.StepRequestBean;

import java.util.List;

public interface StepRepository extends AbstractRepository {
	List<Step> getStepList(StepRequestBean request);
}
