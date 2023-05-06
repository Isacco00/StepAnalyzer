package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.stepcontent.StepJsonBean;
import stepanalyzer.entity.StepContent;
import stepanalyzer.utility.StepUtility;

@Component
public class StepContentMapper extends AbstractMapper<StepContent, StepContentBean> {

    @Inject
    private StepUtility stepUtility;

    protected StepContentBean doMapping(StepContent entity) {
        return doMapping(new StepContentBean(), entity);
    }

    protected StepContentBean doMapping(StepContentBean bean, StepContent entity) {
        bean.setTokenStepContent(entity.getTokenStepContent());
        bean.setPerimetro(entity.getPerimetro());
        bean.setVolume(entity.getVolume());
        ObjectMapper objectMapper = new ObjectMapper();
        if (entity.getJson() != null) {
            try {
                bean.setStepJsonBean(objectMapper.readValue(entity.getJson(), StepJsonBean.class));
                bean.setX3DContent(stepUtility.getX3DContent(bean.getStepJsonBean()));
            } catch (JsonProcessingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bean;
    }
}
