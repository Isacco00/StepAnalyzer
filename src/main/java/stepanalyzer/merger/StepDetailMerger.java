package stepanalyzer.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.entity.Step;

import jakarta.inject.Inject;

@Component
public class StepDetailMerger extends AbstractMerger<StepDetailBean, Step> {
    @Inject
    private StepMerger stepMerger;

    @Override
    protected void doMerge(StepDetailBean bean, Step entity) {
        stepMerger.merge(bean, entity);
        ObjectMapper objectMapper = new ObjectMapper();
        if (bean.getStepContent() != null) {
            try {
                entity.setStepContent(objectMapper.writeValueAsString(bean.getStepContent()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
