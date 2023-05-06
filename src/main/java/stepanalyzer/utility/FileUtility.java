package stepanalyzer.utility;//
// Decompiled by Procyon v0.5.36
//

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
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

    public static String PRINCIPAL_DIR = System.getProperty("user.home") + "/Desktop/UploadedStepFiles/";
    public static String getStepJsonPathName(StepBean bean) {
        String fileName = bean.getFileName();
        String name = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            name = fileName.substring(0, i);
        }
        return PRINCIPAL_DIR + name + "/" + name + ".json";
    }
    public static String getStepFilePathName(StepBean bean) {
        String fileName = bean.getFileName();
        String extension = "", name = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
            name = fileName.substring(0, i);
        }
        String versionedFileName = name + "_" + bean.getVersion() + "." + extension;
        return PRINCIPAL_DIR + name + "/step/" + versionedFileName;
    }

    public static String getStepPathName(StepBean bean) {
        String fileName = bean.getFileName();
        String extension = "", name = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
            name = fileName.substring(0, i);
        }
        if (!Arrays.asList("stp", "step").contains(extension) || fileName.contains("..")) {
            throw new FileStorageException("Errore! Nome file o estensione errati");
        }
        return PRINCIPAL_DIR + name + "/step";
    }

    public void storeFile(MultipartFile formData, StepDetailBean bean) throws IOException {
        File directory = new File(Paths.get(getStepPathName(bean)).toUri());
        if (!directory.exists()) {
            Files.createDirectories(Paths.get(getStepPathName(bean)));
        }
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(getStepFilePathName(bean));
            Files.copy(formData.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Impossibile salvare il file " + bean.getFileName(), ex);
        }
    }

    public byte[] getFile(String fileName) {
        try {
            Path targetLocation = Paths.get(System.getProperty("user.home") + "/Desktop/UploadedStepFiles/" + fileName);
            return Files.readAllBytes(targetLocation);
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
