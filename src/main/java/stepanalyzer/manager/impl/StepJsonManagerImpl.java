package stepanalyzer.manager.impl;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.entity.StepContent;
import stepanalyzer.manager.StepContentManager;
import stepanalyzer.mapper.StepContentMapper;
import stepanalyzer.merger.StepContentMerger;
import stepanalyzer.repository.StepRepository;

@Service
@Transactional
public class StepJsonManagerImpl implements StepContentManager {

    @Inject
    StepRepository repo;
    @Inject
    StepContentMapper mapper;
    @Inject
    StepContentMerger merger;

    @Override
    public StepContentBean saveStepContent(StepContentBean bean) {
        StepContent entity;
        if (bean.getTokenStepContent() == 0) {
            entity = merger.mapNew(bean, StepContent.class);
        } else {
            entity = repo.find(StepContent.class, bean.getTokenStepContent());
            if (entity == null) {
                throw new EntityNotFoundException();
            }
            merger.merge(bean, entity);
        }
        this.repo.save(entity);
        return mapper.mapEntityToBean(entity);
    }

}
