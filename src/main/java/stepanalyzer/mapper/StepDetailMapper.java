package stepanalyzer.mapper;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.stepcontent.*;
import stepanalyzer.entity.Step;
import stepanalyzer.utility.StepUtility;

import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;

@Component
public class StepDetailMapper extends AbstractMapper<Step, StepDetailBean> {

    @Inject
    private StepMapper stepMapper;
    @Inject
    private StepContentMapper stepContentMapper;

    protected StepDetailBean doMapping(Step entity) {
        return doMapping(new StepDetailBean(), entity);
    }

    protected StepDetailBean doMapping(StepDetailBean bean, Step entity) {
        stepMapper.mapEntityToBean(bean, entity);

        if (entity.getStepContent() != null) {
            StepContentBean stepContentBean = stepContentMapper.mapEntityToBean(entity.getStepContent());
            bean.setStepContent(stepContentBean);
        }

        return bean;
    }

}
