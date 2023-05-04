package stepanalyzer.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepJsonBean;
import stepanalyzer.entity.Step;
import stepanalyzer.entity.StepJson;

@Component
public class StepJsonMerger extends AbstractMerger<StepJsonBean, StepJson> {

    @Override
    protected void doMerge(StepJsonBean bean, StepJson entity) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (bean.getStepContentBean() != null) {
            try {
                entity.setJson(objectMapper.writeValueAsString(bean.getStepContentBean()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
