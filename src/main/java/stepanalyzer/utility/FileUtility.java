package stepanalyzer.utility;//
// Decompiled by Procyon v0.5.36
//

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.exception.FileStorageException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;

@Component
public class FileUtility {
    public String storeFile(MultipartFile formData) throws IOException {
        File directory = new File(Paths.get(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/").toUri());
        if (!directory.exists()) {
            Files.createDirectories(Paths.get(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/"));
        }
        String fileName = new File(Objects.requireNonNull(formData.getOriginalFilename())).getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (!Arrays.asList("stp", "step").contains(extension) || fileName.contains("..")) {
            throw new FileStorageException("Errore! Nome file o estensione errati");
        }
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/" + fileName);
            Files.copy(formData.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toFile().getName();
        } catch (IOException ex) {
            throw new FileStorageException("Impossibile salvare il file " + fileName, ex);
        }
    }

    public byte[] getFile(String fileName) {
        try {
            Path targetLocation = Paths.get(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/" + fileName);
            byte[] data = Files.readAllBytes(targetLocation);
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
