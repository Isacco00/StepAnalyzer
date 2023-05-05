package stepanalyzer.merger;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.entity.Step;

import jakarta.inject.Inject;
import stepanalyzer.entity.StepContent;

@Component
public class StepDetailMerger extends AbstractMerger<StepDetailBean, Step> {
    @Inject
    private StepMerger stepMerger;

    @Override
    protected void doMerge(StepDetailBean bean, Step entity) {
        stepMerger.merge(bean, entity);

        if (bean.getStepContent() != null) {
            StepContent stepContent = repo.find(StepContent.class, bean.getStepContent().getTokenStepContent());
            entity.setStepContent(stepContent);
        }
    }

}
