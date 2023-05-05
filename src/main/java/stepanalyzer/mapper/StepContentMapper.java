package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.stepcontent.StepJsonBean;
import stepanalyzer.entity.StepContent;

@Component
public class StepContentMapper extends AbstractMapper<StepContent, StepContentBean> {

    protected StepContentBean doMapping(StepContent entity) {
        return doMapping(new StepContentBean(), entity);
    }

    protected StepContentBean doMapping(StepContentBean bean, StepContent entity) {
        bean.setTokenStepContent(entity.getTokenStepContent());
        ObjectMapper objectMapper = new ObjectMapper();
        if (entity.getJson() != null) {
            try {
                bean.setStepJsonBean(objectMapper.readValue(entity.getJson(), StepJsonBean.class));
            } catch (JsonProcessingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bean;
    }
}
