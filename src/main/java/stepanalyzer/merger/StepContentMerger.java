package stepanalyzer.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.entity.StepContent;

@Component
public class StepContentMerger extends AbstractMerger<StepContentBean, StepContent> {

    @Override
    protected void doMerge(StepContentBean bean, StepContent entity) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (bean.getStepJsonBean() != null) {
            try {
                entity.setJson(objectMapper.writeValueAsString(bean.getStepJsonBean()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        entity.setPerimetro(bean.getPerimetro());
        entity.setVolume(bean.getVolume());
        entity.setLunghezzaX(bean.getLunghezzaX());
        entity.setLarghezzaY(bean.getLarghezzaY());
        entity.setSpessoreZ(bean.getSpessoreZ());
        entity.setPesoPezzo(bean.getPesoPezzo());
        entity.setCostoPezzoMateriale(bean.getCostoPezzoMateriale());
    }

}
