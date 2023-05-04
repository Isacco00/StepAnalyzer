package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepJsonBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.StepJson;

@Component
public class StepJsonMapper extends AbstractMapper<StepJson, StepJsonBean> {

    protected StepJsonBean doMapping(StepJson entity) {
        return doMapping(new StepJsonBean(), entity);
    }

    protected StepJsonBean doMapping(StepJsonBean bean, StepJson entity) {
        bean.setTokenStepJson(entity.getTokenStepJson());
        ObjectMapper objectMapper = new ObjectMapper();
        if (entity.getJson() != null) {
            try {
                bean.setStepContentBean(objectMapper.readValue(entity.getJson(), StepContentBean.class));
            } catch (JsonProcessingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bean;
    }
}
