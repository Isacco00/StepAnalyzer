package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.Mesh;
import stepanalyzer.bean.stepcontent.Model;
import stepanalyzer.bean.stepcontent.Shapes;
import stepanalyzer.bean.stepcontent.StepJsonBean;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepContentManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.StepDetailMapper;
import stepanalyzer.mapper.StepMapper;
import stepanalyzer.merger.StepDetailMerger;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;
import stepanalyzer.utility.CollectionUtils;
import stepanalyzer.utility.FileUtility;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import stepanalyzer.utility.StepUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StepManagerImpl implements StepManager {

    @Inject
    FileUtility fileUtility;
    @Inject
    StepUtility stepUtility;
    @Inject
    StepRepository stepRepository;
    @Inject
    StepDetailMapper stepDetailMapper;
    @Inject
    StepMapper stepMapper;
    @Inject
    StepDetailMerger stepMerger;
    @Inject
    private StepContentManager stepContentManager;

    @Override
    public List<StepBean> getStepBeanList() {
        return stepMapper.mapEntitiesToBeans(stepRepository.getStepList(new StepRequestBean()));
    }

    @Override
    public StepBean uploadStepFile(MultipartFile formData) throws IOException {
        String fileName = new File(Objects.requireNonNull(formData.getOriginalFilename())).getName();
        StepRequestBean stepRequestBean = new StepRequestBean();
        stepRequestBean.setFileName(fileName);
        List<Step> filesFound = stepRepository.getStepList(stepRequestBean);
        StepDetailBean bean;
        if (filesFound.size() != 0) {
            Step step = CollectionUtils.getSingleElement(filesFound);
            bean = stepDetailMapper.mapEntityToBean(step);
            bean.setVersion(bean.getVersion() + 1);
        } else {
            bean = new StepDetailBean();
            bean.setTokenStep(0L);
            bean.setVersion(1);
        }
        bean.setFileName(fileName);
        bean.setAction("Calculating");
        fileUtility.storeFile(formData, bean);
        return this.saveStep(bean);
    }

    @Override
    public StepBean saveStep(StepDetailBean bean) {
        bean.setUpdateTimestamp(OffsetDateTime.now());
        Step entity;
        if (bean.getTokenStep() == 0) {
            entity = stepMerger.mapNew(bean, Step.class);
        } else {
            entity = stepRepository.find(Step.class, bean.getTokenStep());
            if (entity == null) {
                throw new EntityNotFoundException();
            }
            stepMerger.merge(bean, entity);
        }
        this.stepRepository.save(entity);
        return stepMapper.mapEntityToBean(entity);
    }

    @Override
    public StepDetailBean getStepDetail(Long tokenStep) {
        Step entity = stepRepository.find(Step.class, tokenStep);
        return stepDetailMapper.mapEntityToBean(entity);
    }

    @Override
    public void deleteStepFile(Long tokenStep) {
        Step entity = stepRepository.find(Step.class, tokenStep);
        if (entity != null) {
            stepRepository.delete(entity);
        } else {
            throw new EntityNotFoundException("Step file not found");
        }
    }

    @Override
    public StepBean processStepFile(@NotNull Long tokenStep) {
        StepDetailBean bean = this.getStepDetail(tokenStep);
        try {
            StepJsonBean stepJsonBean = this.calculateStepFile(bean);
            StepContentBean content = bean.getStepContent();
            if (content == null) {
                content = new StepContentBean();
                content.setTokenStepContent(0L);
            }
            content.setStepJsonBean(stepJsonBean);
            this.setPerimeterAndVolume(content);
            bean.setStepContent(stepContentManager.saveStepContent(content));
            bean.setAction("Completed");
        } catch (Exception ex) {
            bean.setAction("Error");
        }
        return this.saveStep(bean);
    }

    @Override
    public StepJsonBean calculateStepFile(StepDetailBean bean) {
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(desktopPath + "/STPCalculator/bin/STPCalculator.exe",
                "--input", FileUtility.getStepFilePathName(bean),
                "--output", FileUtility.getStepJsonPathName(bean));
        builder.directory(new File(desktopPath + "/STPCalculator/bin"));
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            int processCompleted = process.waitFor();
            if (processCompleted == 0) {
                Thread.sleep(1000);
                ObjectMapper objectMapper = new ObjectMapper();
                Path targetLocation = Paths.get(FileUtility.getStepJsonPathName(bean));
                File jsonFile = new File(targetLocation.toUri());
                return objectMapper.readValue(jsonFile, StepJsonBean.class);
            } else {
                throw new ValidationException("Errore calcolo file STP");
            }
        } catch (InterruptedException | IOException e) {
            throw new ValidationException("Errore calcolo file STP");
        }
    }

    private void setPerimeterAndVolume(StepContentBean bean) {
        Model model = bean.getStepJsonBean().getModel();
        Shapes shape = model.getComponents().get(0).getShapes().get(0);
        Comparator<Mesh> byCoordinates = Comparator.comparingInt(e -> e.getCoordinates().size());
        List<Mesh> ordered = shape.getMesh().stream().sorted(byCoordinates.reversed()).toList();
        bean.setPerimetro(ordered.get(0).getEdgePerimeter());
        bean.setVolume(shape.getVolume());
    }
}
