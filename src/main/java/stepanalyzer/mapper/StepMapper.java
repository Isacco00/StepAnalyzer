package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;

@Component public class StepMapper extends AbstractMapper<Step, StepBean> {

    protected StepBean doMapping(Step entity) {
        return doMapping(new StepBean(), entity);
    }

    protected StepBean doMapping(StepBean bean, Step entity) {
        bean.setTokenStep(entity.getTokenStep());
        bean.setFileName(entity.getFileName());
        bean.setAction(entity.getAction());
        return bean;
    }

}
