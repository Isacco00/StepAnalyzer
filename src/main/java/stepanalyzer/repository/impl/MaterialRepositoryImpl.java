package stepanalyzer.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import stepanalyzer.entity.Material;
import stepanalyzer.entity.Step;
import stepanalyzer.repository.MaterialRepository;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.MaterialRequestBean;
import stepanalyzer.request.bean.StepRequestBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MaterialRepositoryImpl extends AbstractRepositoryImpl implements MaterialRepository {

    @Override
    public List<Material> getMaterialList(MaterialRequestBean request) {
        Class<Material> clazz = Material.class;
        Map<String, Object> parameters = new HashMap<>();

        StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT m FROM " + clazz.getSimpleName() + " m ");
        StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");

        // Parameters
        if (request != null) {
            if (request.getDescrizione() != null) {
                strQueryWhere.append("AND m.descrizione = :descrizione ");
                parameters.put("descrizione", request.getDescrizione());
            }
        }
        String strQueryFinal = (strQueryFrom.append(strQueryWhere)).toString();
        TypedQuery<Material> query = entityManager.createQuery(strQueryFinal, clazz);
        parameters.forEach(query::setParameter);
        return getResultList(query);
    }
}
