package stepanalyzer.merger;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.entity.Step;

@Component
public class StepMerger extends AbstractMerger<StepBean, Step> {

	@Override
	protected void doMerge(StepBean bean, Step entity) {
		entity.setTokenStep(bean.getTokenStep());
		entity.setFileName(bean.getFileName());
	}

}
