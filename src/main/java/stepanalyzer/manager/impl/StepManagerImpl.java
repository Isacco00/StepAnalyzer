package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.StepDetailMapper;
import stepanalyzer.mapper.StepMapper;
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
    StepUtility stepUtility;
    @Inject
    StepConverterManager stepConverterManager;
    @Inject
    FileUtility fileUtility;
    @Inject
    StepRepository stepRepository;
    @Inject
    StepDetailMapper stepDetailMapper;
    @Inject
    StepMapper stepMapper;
    @Inject
    StepMerger stepMerger;
    @Inject
    CalcUtility calcUtility;

    @Override
    public StepBean stpCalculator(MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        DocumentBean stlDocument = stepConverterManager.fromStpToStl(file);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("python", "calcVolume.py", stlDocument.getFilePath());
        builder.directory(new File(System.getProperty("user.home") + "/Desktop"));
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            StepBean bean = new StepBean();
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                switch (counter) {
                    case 0 -> {
                        String[] lunghezze = line.split(";");
                        bean.setLunghezzaX(new BigDecimal(lunghezze[0]));
                        bean.setLarghezzaY(new BigDecimal(lunghezze[1]));
                        bean.setSpessoreZ(new BigDecimal(lunghezze[2]));
                    }
                    case 1 -> bean.setVolume(new BigDecimal(line));
                    default -> throw new ValidationException("Errore calcolo file STP");
                }
                counter++;
            }
            bean.setFileName(stlDocument.getFileName());
            return bean;
        } else {
            throw new ValidationException("Errore calcolo file STP");
        }
    }

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
        StepBean bean;
        if (filesFound.size() != 0) {
            Step step = CollectionUtils.getSingleElement(filesFound);
            bean = stepDetailMapper.mapEntityToBean(step);
            //throw new FileStorageException("File già presente nel sistema");
        } else {
            bean = new StepBean();
            bean.setTokenStep(0L);
        }
        //StepContentBean stepContentBean = this.processStepFile(fileName);
        bean.setFileName(fileName);
        bean.setAction("Calculating");
        //bean.setStepContent(stepContentBean);
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
    public StepBean getStepDetail(Long tokenStep) throws IOException {
        Step entity = stepRepository.find(Step.class, tokenStep);
        StepBean bean = stepDetailMapper.mapEntityToBean(entity);
        bean.setX3DContent(stepUtility.getX3DContent(bean.getStepContent()));
        return bean;
    }

    private StepContentBean processStepFile(String fileName) {
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
