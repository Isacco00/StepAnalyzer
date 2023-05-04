package stepanalyzer.repository.impl;

import org.springframework.stereotype.Repository;
import stepanalyzer.entity.Step;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;

import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StepRepositoryImpl extends AbstractRepositoryImpl implements StepRepository {

    @Override
    public List<Step> getStepList(StepRequestBean request) {
        Class<Step> clazz = Step.class;
        Map<String, Object> parameters = new HashMap<>();

        StringBuilder strQueryFrom = new StringBuilder(" SELECT DISTINCT s FROM " + clazz.getSimpleName() + " s ");
        StringBuilder strQueryWhere = new StringBuilder(" WHERE 1=1 ");

        // Parameters
        if (request != null) {
            if (request.getFileName() != null) {
                strQueryWhere.append("AND s.fileName = :fileName ");
                parameters.put("fileName", request.getFileName());
            }
        }
        String strQueryFinal = (strQueryFrom.append(strQueryWhere)).toString();
        TypedQuery<Step> query = entityManager.createQuery(strQueryFinal, clazz);
        parameters.forEach(query::setParameter);
        return getResultList(query);
    }
}
