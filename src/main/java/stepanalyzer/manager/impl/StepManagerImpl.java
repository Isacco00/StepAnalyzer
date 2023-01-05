package stepanalyzer.manager.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.EntityNotFoundException;
import stepanalyzer.exception.FileStorageException;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.StepMapper;
import stepanalyzer.merger.StepMerger;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;
import stepanalyzer.utility.FileUtility;
import stepanalyzer.utility.StepUtility;
import stepanalyzer.utility.StreamGobbler;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
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
    StepMapper stepMapper;
    @Inject
    StepMerger stepMerger;

    @Override
    public StepBean stpCalculator(MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        DocumentBean stlDocument = stepConverterManager.fromStpToStl(file);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("python", "calcVolume.py", stlDocument.getFilePath());
        builder.directory(new File(System.getProperty("user.home") + "/Desktop"));
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
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
        boolean fileAlreadyUpload = stepRepository.getStepList(stepRequestBean).size() != 0;
        if (fileAlreadyUpload) {
            throw new FileStorageException("File già presente nel sistema");
        } else {
            StepBean bean = new StepBean();
            bean.setTokenStep(0L);
            bean.setFileName(fileName);
            Step entity = stepMerger.mapNew(bean, Step.class);
            stepRepository.saveOrUpdate(entity);
            return stepMapper.mapEntityToBean(entity);
        }
    }

    @Override
    public StepBean getStepDetail(Long tokenStep) throws IOException {
        Step entity = stepRepository.find(Step.class, tokenStep);
        System.out.println(entity.getFileName());
        StepBean bean = stepMapper.mapEntityToBean(entity);
        InputStream stepFile = fileUtility.getStepFile(bean.getFileName());
        String x3DContent = stepUtility.processStepFile(stepFile);
        bean.setX3dData(x3DContent);
        return bean;
    }
}
