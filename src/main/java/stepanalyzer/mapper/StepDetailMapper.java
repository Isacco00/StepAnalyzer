package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;
import stepanalyzer.utility.StepUtility;

import javax.inject.Inject;

@Component
public class StepDetailMapper extends AbstractMapper<Step, StepDetailBean> {

    @Inject
    private StepMapper stepMapper;
    @Inject
    private StepUtility stepUtility;

    protected StepDetailBean doMapping(Step entity) {
        return doMapping(new StepDetailBean(), entity);
    }

    protected StepDetailBean doMapping(StepDetailBean bean, Step entity) {
        stepMapper.mapEntityToBean(bean, entity);

        ObjectMapper objectMapper = new ObjectMapper();
        if (entity.getStepContent() != null) {
            try {
                bean.setStepContent(objectMapper.readValue(entity.getStepContent(), StepContentBean.class));
                bean.setX3DContent(stepUtility.getX3DContent(bean.getStepContent()));
            } catch (JsonProcessingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bean;
    }
}
