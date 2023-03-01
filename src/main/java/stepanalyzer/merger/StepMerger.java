package stepanalyzer.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.entity.Step;

@Component
public class StepMerger extends AbstractMerger<StepBean, Step> {

    @Override
    protected void doMerge(StepBean bean, Step entity) {
        entity.setTokenStep(bean.getTokenStep());
        entity.setFileName(bean.getFileName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            entity.setStepContent(objectMapper.writeValueAsString(bean.getStepContent()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
