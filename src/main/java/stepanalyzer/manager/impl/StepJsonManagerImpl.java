package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.StepJsonBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;
import stepanalyzer.entity.StepJson;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepJsonManager;
import stepanalyzer.mapper.StepJsonMapper;
import stepanalyzer.merger.StepJsonMerger;
import stepanalyzer.repository.StepRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class StepJsonManagerImpl implements StepJsonManager {

    @Inject
    StepRepository repo;
    @Inject
    StepJsonMapper mapper;
    @Inject
    StepJsonMerger merger;

    @Override
    public StepJsonBean saveStepJson(StepJsonBean bean) {
        StepJson entity;
        if (bean.getTokenStepJson() == 0) {
            entity = merger.mapNew(bean, StepJson.class);
        } else {
            entity = repo.find(StepJson.class, bean.getTokenStepJson());
            if (entity == null) {
                throw new EntityNotFoundException();
            }
            merger.merge(bean, entity);
        }
        this.repo.save(entity);
        return mapper.mapEntityToBean(entity);
    }

}
