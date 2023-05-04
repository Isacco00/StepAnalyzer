package stepanalyzer.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import stepanalyzer.entity.Step;
import stepanalyzer.repository.GenericRepository;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GenericRepositoryImpl extends AbstractRepositoryImpl implements GenericRepository {
}
