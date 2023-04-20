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
        entity.setFileName(bean.getFileName());
        entity.setAction(bean.getAction());
        ObjectMapper objectMapper = new ObjectMapper();
        if(bean.getStepContent() != null) {
            try {
                entity.setStepContent(objectMapper.writeValueAsString(bean.getStepContent()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
