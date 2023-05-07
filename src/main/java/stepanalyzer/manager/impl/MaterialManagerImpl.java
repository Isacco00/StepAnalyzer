package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.Mesh;
import stepanalyzer.bean.stepcontent.Model;
import stepanalyzer.bean.stepcontent.Shapes;
import stepanalyzer.bean.stepcontent.StepJsonBean;
import stepanalyzer.entity.Material;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.MaterialManager;
import stepanalyzer.manager.StepContentManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.MaterialMapper;
import stepanalyzer.mapper.StepDetailMapper;
import stepanalyzer.mapper.StepMapper;
import stepanalyzer.merger.MaterialMerger;
import stepanalyzer.merger.StepDetailMerger;
import stepanalyzer.repository.MaterialRepository;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.MaterialRequestBean;
import stepanalyzer.request.bean.StepRequestBean;
import stepanalyzer.utility.CalcUtility;
import stepanalyzer.utility.CollectionUtils;
import stepanalyzer.utility.FileUtility;
import stepanalyzer.utility.StepUtility;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class MaterialManagerImpl implements MaterialManager {

    @Inject
    MaterialRepository materialRepository;
    @Inject
    MaterialMapper materialMapper;
    @Inject
    MaterialMerger materialMerger;

    @Override
    public List<MaterialBean> getMaterialList() {
        return materialMapper.mapEntitiesToBeans(materialRepository.getMaterialList(new MaterialRequestBean()));
    }

    @Override
    public MaterialBean getDefaultMaterial() {
        Material entity = materialRepository.find(Material.class, 1);
        if (entity != null) {
            return materialMapper.mapEntityToBean(entity);
        } else {
            throw new EntityNotFoundException("Material not found");
        }
    }
}
