package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;

import javax.inject.Inject;

@Component
public class StepDetailMapper extends AbstractMapper<Step, StepBean> {

    @Inject
    private StepMapper stepMapper;

    protected StepBean doMapping(Step entity) {
        return doMapping(new StepBean(), entity);
    }

    protected StepBean doMapping(StepBean bean, Step entity) {
        stepMapper.mapEntityToBean(bean, entity);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            bean.setStepContent(objectMapper.readValue(entity.getStepContent(), StepContentBean.class));
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
        return bean;
    }

}
