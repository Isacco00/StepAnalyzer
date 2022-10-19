package stepanalyzer.manager.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.exception.EntityNotFoundException;
import stepanalyzer.exception.FileStorageException;
import stepanalyzer.exception.ValidationException;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.mapper.CurrencyMapper;
import stepanalyzer.repository.CurrencyRepository;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
public class StepConverterManagerImpl implements StepConverterManager {

    @Inject
    private CurrencyRepository repo;
    @Inject
    private CurrencyMapper mapper;

    @Override
    public String fromStpToX3D(MultipartFile formData) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String fileName = storeFile(formData);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("C:\\Users\\PC\\Downloads\\SFA-4.80\\sfa-cl.exe", fileName, "view");
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            String htmlFileName = changeExtension(fileName, "-sfa.html");
            Resource resource = loadFileAsResource(htmlFileName);
            String s = new String(Files.readAllBytes(resource.getFile().toPath()));
            return s.substring(s.indexOf("<X3D"), s.indexOf("</X3D>") + 6);
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
            Path targetLocation = Path.of("C:\\stpviewer\\" + fileName);
            File file = new File(targetLocation.toUri());
            file.getParentFile().mkdirs();
            Files.copy(formData.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toFile().getAbsolutePath();
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

    public static String changeExtension(String fileName, String newExtension) {
        int i = fileName.lastIndexOf('.');
        String name = fileName.substring(0, i);
        return name + newExtension;
    }

}
