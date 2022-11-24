package stepanalyzer.manager.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.exception.EntityNotFoundException;
import stepanalyzer.exception.FileStorageException;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.utility.StepUtility;
import stepanalyzer.utility.StreamGobbler;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.Executors;

@Service @Transactional public class StepConverterManagerImpl implements StepConverterManager {

    @Inject StepUtility stepUtility;

    @Override public DocumentBean fromStpToStl(MultipartFile formData) throws IOException, InterruptedException {
        String fileName = storeFile(formData);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("python", "converter.py");
        builder.directory(new File(System.getProperty("user.home") + "/Desktop"));
        Process process = builder.inheritIO().start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            String stlFileName = System.getProperty("user.home") + "/Desktop/3DModelsToConvert/Converted-STLs/" + fileName + ".stl";
            Resource resource = loadFileAsResource(stlFileName);
            DocumentBean bean = new DocumentBean();
            bean.setPayload(Files.readAllBytes(resource.getFile().toPath()));
            bean.setFileName(fileName + ".stl");
            return bean;
        } else {
            throw new ValidationException("Errore lettura file STP");
        }
    }

    private String storeFile(MultipartFile formData) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(formData.getOriginalFilename()));
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Path.of(System.getProperty("user.home") + "/Desktop/3DModelsToConvert/" + fileName);
            Files.copy(formData.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toFile().getName();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Path.of(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new EntityNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new EntityNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override public String fromStpToX3DCalculator(MultipartFile formData) throws IOException {
        stepUtility.processStepFile(formData.getInputStream());
        return null;
    }

}
