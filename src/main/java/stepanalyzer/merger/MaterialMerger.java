package stepanalyzer.merger;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.entity.Material;
import stepanalyzer.entity.Step;

@Component
public class MaterialMerger extends AbstractMerger<MaterialBean, Material> {

    @Override
    protected void doMerge(MaterialBean bean, Material entity) {
        entity.setDescrizione(bean.getDescrizione());
    }

}
