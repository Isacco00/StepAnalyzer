package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.StepDetailMapper;
import stepanalyzer.mapper.StepMapper;
import stepanalyzer.merger.StepDetailMerger;
import stepanalyzer.merger.StepMerger;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;
import stepanalyzer.utility.CalcUtility;
import stepanalyzer.utility.CollectionUtils;
import stepanalyzer.utility.FileUtility;
import stepanalyzer.utility.StepUtility;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
public class StepManagerImpl implements StepManager {

    @Inject
    FileUtility fileUtility;
    @Inject
    StepRepository stepRepository;
    @Inject
    StepDetailMapper stepDetailMapper;
    @Inject
    StepMapper stepMapper;
    @Inject
    StepDetailMerger stepMerger;

    @Override
    public List<StepBean> getStepBeanList() {
        return stepMapper.mapEntitiesToBeans(stepRepository.getStepList(new StepRequestBean()));
    }

    @Override
    public StepBean uploadStepFile(MultipartFile formData) throws IOException {
        String fileName = fileUtility.storeFile(formData);
        StepRequestBean stepRequestBean = new StepRequestBean();
        stepRequestBean.setFileName(fileName);
        List<Step> filesFound = stepRepository.getStepList(stepRequestBean);
        StepDetailBean bean;
        if (filesFound.size() != 0) {
            Step step = CollectionUtils.getSingleElement(filesFound);
            bean = stepDetailMapper.mapEntityToBean(step);
            //throw new FileStorageException("File già presente nel sistema");
        } else {
            bean = new StepDetailBean();
            bean.setTokenStep(0L);
        }
        bean.setFileName(fileName);
        bean.setAction("Calculating");
        return this.saveStep(bean);
    }

    @Override
    public StepDetailBean saveStep(StepDetailBean bean) {
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
        return stepDetailMapper.mapEntityToBean(entity);
    }

    @Override
    public StepDetailBean getStepDetail(Long tokenStep) {
        Step entity = stepRepository.find(Step.class, tokenStep);
        return stepDetailMapper.mapEntityToBean(entity);
    }

    @Override
    public StepContentBean calculateStepFile(String fileName) {
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", "start", "/min", "STPCalculator.exe", "--input", desktopPath + "/UploadedStepFiles/" + fileName, "--output", desktopPath + "/STPCalculator/" + fileName + ".json");
        builder.directory(new File(desktopPath + "/STPCalculator/bin"));
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            int processCompleted = process.waitFor();
            if (processCompleted == 0) {
                Thread.sleep(1000);
                ObjectMapper objectMapper = new ObjectMapper();
                Path targetLocation = Paths.get(System.getProperty("user.home") + "/Desktop/STPCalculator/" + fileName + ".json");
                File jsonFile = new File(targetLocation.toUri());
                return objectMapper.readValue(jsonFile, StepContentBean.class);
            } else {
                throw new ValidationException("Errore calcolo file STP");
            }
        } catch (InterruptedException | IOException e) {
            throw new ValidationException("Errore calcolo file STP");
        }
    }
}
