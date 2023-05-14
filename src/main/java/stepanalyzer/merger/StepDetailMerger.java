package stepanalyzer.merger;

import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.entity.Material;
import stepanalyzer.entity.Step;
import stepanalyzer.entity.StepContent;

@Component
public class StepDetailMerger extends AbstractMerger<StepDetailBean, Step> {
    @Inject
    private StepMerger stepMerger;

    @Override
    protected void doMerge(StepDetailBean bean, Step entity) {
        stepMerger.merge(bean, entity);
        if (bean.getStepContent() != null) {
            StepContent stepContent = repo.find(StepContent.class, bean.getStepContent().getTokenStepContent());
            entity.setStepContent(stepContent);
        }

        if (bean.getMaterialBean() != null) {
            Material material = repo.find(Material.class, bean.getMaterialBean().getTokenMaterial());
            entity.setMaterial(material);
        }
    }

}
