package stepanalyzer.mapper;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.entity.Step;

import jakarta.inject.Inject;

@Component
public class StepDetailMapper extends AbstractMapper<Step, StepDetailBean> {

    @Inject
    private StepMapper stepMapper;
    @Inject
    private StepContentMapper stepContentMapper;
    @Inject
    private MaterialMapper materialMapper;

    protected StepDetailBean doMapping(Step entity) {
        return doMapping(new StepDetailBean(), entity);
    }

    protected StepDetailBean doMapping(StepDetailBean bean, Step entity) {
        stepMapper.mapEntityToBean(bean, entity);

        if (entity.getStepContent() != null) {
            StepContentBean stepContentBean = stepContentMapper.mapEntityToBean(entity.getStepContent());
            bean.setStepContent(stepContentBean);
        }
        if (entity.getMaterial() != null) {
            MaterialBean materialBean = materialMapper.mapEntityToBean(entity.getMaterial());
            bean.setMaterialBean(materialBean);
        }
        return bean;
    }

}
