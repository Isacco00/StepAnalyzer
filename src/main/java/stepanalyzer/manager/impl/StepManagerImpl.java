package stepanalyzer.manager.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.exception.EntityNotFoundException;
import stepanalyzer.exception.FileStorageException;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.utility.StepUtility;
import stepanalyzer.utility.StreamGobbler;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
}
