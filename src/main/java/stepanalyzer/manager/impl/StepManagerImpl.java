package stepanalyzer.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jcae.opencascade.jni.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.entity.Step;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.mapper.StepMapper;
import stepanalyzer.merger.StepMerger;
import stepanalyzer.repository.StepRepository;
import stepanalyzer.request.bean.StepRequestBean;
import stepanalyzer.stepmodel.ModelBean;
import stepanalyzer.utility.CollectionUtils;
import stepanalyzer.utility.FileUtility;
import stepanalyzer.utility.StepUtility;

import javax.inject.Inject;
import javax.jws.WebParam;
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
                    case 0:
                        String[] lunghezze = line.split(";");
                        bean.setLunghezzaX(new BigDecimal(lunghezze[0]));
                        bean.setLarghezzaY(new BigDecimal(lunghezze[1]));
                        bean.setSpessoreZ(new BigDecimal(lunghezze[2]));
                        break;
                    case 1:
                        bean.setVolume(new BigDecimal(line));
                        break;
                    default:
                        throw new ValidationException("Errore calcolo file STP");
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
        ModelBean stepModelBean = readStepFile(fileName);
        StepRequestBean stepRequestBean = new StepRequestBean();
        stepRequestBean.setFileName(fileName);
        List<Step> filesFound = stepRepository.getStepList(stepRequestBean);
        StepBean bean;
        if (filesFound.size() != 0) {
            Step step = CollectionUtils.getSingleElement(filesFound);
            bean = stepMapper.mapEntityToBean(step);
            //throw new FileStorageException("File già presente nel sistema");
        } else {
            bean = new StepBean();
            bean.setTokenStep(0L);
        }
        StepContentBean stepContentBean = this.processStepFile(fileName);
        bean.setFileName(fileName);
        bean.setStepContent(stepContentBean);
        Step entity = stepMerger.mapNew(bean, Step.class);
        stepRepository.saveOrUpdate(entity);
        return stepMapper.mapEntityToBean(entity);
    }

    @Override
    public StepBean getStepDetail(Long tokenStep) throws IOException {
        Step entity = stepRepository.find(Step.class, tokenStep);
        System.out.println(entity.getFileName());
        StepBean bean = stepMapper.mapEntityToBean(entity);
        bean.setX3DContent(stepUtility.getX3DContent(bean.getStepContent()));
        return bean;
    }

    private ModelBean readStepFile(String fileName) {
        ModelBean modelBean = new ModelBean();
        IFSelect_ReturnStatus status;
        STEPControl_Reader reader = new STEPControl_Reader();
        status = reader.readFile(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/" + fileName);
        if (checkReturnStatus(status)) {
            if (reader.transferRoots() > 0) {
                TopoDS_Shape shape = reader.oneShape();
                TopExp_Explorer expFace = new TopExp_Explorer();
                expFace.init(shape, TopAbs_ShapeEnum.FACE);
                modelBean.setShape(shape);
                modelBean.setFaceSet(expFace.more());
            }
        }
        return modelBean;
    }

    private boolean checkReturnStatus(IFSelect_ReturnStatus status) {
        switch (status) {
            case ERROR:
                throw new ValidationException("Not a valid STEP file.\n");
            case FAIL:
                throw new ValidationException("Reading STEP has failed.\n");
            case VOID:
                throw new ValidationException("STEP file is empty.\n");
            case STOP:
                throw new ValidationException("Reading STEP has stopped.\n");
            case DONE:
                return true;
            default:
                throw new ValidationException("Error reading STEP file");
        }
    }

    private StepContentBean processStepFile(String fileName) {
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", "start", "/min", "STPCalculator.exe", "--html", "1", "--edge", "1", "--input", desktopPath + "/UploadedStepFiles/" + fileName, "--output", desktopPath + "/STPCalculator");
        builder.directory(new File(desktopPath + "/STPCalculator/bin"));
        try {
            Process process = builder.start();
            int processCompleted = process.waitFor();
            if (processCompleted == 0) {
                Thread.sleep(1000);
                ObjectMapper objectMapper = new ObjectMapper();
                Path targetLocation = Paths.get(System.getProperty("user.home") + "/Desktop/STPCalculator/calculatedStep.json");
                File jsonFile = new File(targetLocation.toUri());
                StepContentBean stepContent = objectMapper.readValue(jsonFile, StepContentBean.class);
                return stepContent;
            } else {
                throw new ValidationException("Errore calcolo file STP");
            }
        } catch (InterruptedException | IOException e) {
            throw new ValidationException("Errore calcolo file STP");
        }
    }
}
