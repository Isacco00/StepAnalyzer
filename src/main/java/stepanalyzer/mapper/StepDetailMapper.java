package stepanalyzer.mapper;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.StepJsonBean;
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
    private StepJsonMapper stepJsonMapper;
    @Inject
    private StepUtility stepUtility;

    protected StepDetailBean doMapping(Step entity) {
        return doMapping(new StepDetailBean(), entity);
    }

    protected StepDetailBean doMapping(StepDetailBean bean, Step entity) {
        stepMapper.mapEntityToBean(bean, entity);

        if (entity.getStepJson() != null) {
            StepJsonBean stepJsonBean = stepJsonMapper.mapEntityToBean(entity.getStepJson());
            bean.setStepJson(stepJsonBean);
        }

        return bean;
    }

    private void setPerimeterAndVolume(StepDetailBean bean, Model model) {
        Shapes shape = model.getComponents().get(0).getShapes().get(0);
        Comparator<Mesh> byCoordinates = Comparator.comparingInt(e -> e.getCoordinates().size());
        List<Mesh> ordered = shape.getMesh().stream().sorted(byCoordinates.reversed()).toList();
        bean.setPerimetro(ordered.get(0).getEdgePerimeter());
        bean.setVolume(shape.getVolume());
    }
}
