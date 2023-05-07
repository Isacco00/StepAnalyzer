package stepanalyzer.repository;

import stepanalyzer.entity.Material;
import stepanalyzer.entity.Step;
import stepanalyzer.request.bean.MaterialRequestBean;
import stepanalyzer.request.bean.StepRequestBean;

import java.util.List;

public interface MaterialRepository extends AbstractRepository {
    List<Material> getMaterialList(MaterialRequestBean request);
}
