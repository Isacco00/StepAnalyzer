package stepanalyzer.mapper;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
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
