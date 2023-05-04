package stepanalyzer.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.entity.Step;

import jakarta.inject.Inject;
import stepanalyzer.entity.StepJson;
import stepanalyzer.exception.EntityNotFoundException;

@Component
public class StepDetailMerger extends AbstractMerger<StepDetailBean, Step> {
    @Inject
    private StepMerger stepMerger;

    @Override
    protected void doMerge(StepDetailBean bean, Step entity) {
        stepMerger.merge(bean, entity);

        if (bean.getStepJson() != null) {
            StepJson stepJson = repo.find(StepJson.class, bean.getStepJson().getTokenStepJson());
            entity.setStepJson(stepJson);
        }
    }

}
