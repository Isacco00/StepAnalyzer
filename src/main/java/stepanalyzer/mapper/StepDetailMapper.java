package stepanalyzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepDetailBean;
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
                setPerimeterAndVolume(bean, bean.getStepContent().getModel());
            } catch (JsonProcessingException ex) {
                System.out.println(ex.getMessage());
            }
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
