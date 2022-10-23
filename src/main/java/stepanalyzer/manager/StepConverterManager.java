package stepanalyzer.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface StepConverterManager {

    String fromStpToX3D(MultipartFile inputStream) throws IOException, InterruptedException, ExecutionException, TimeoutException;

    String fromStpToX3DCalculator(MultipartFile inputStream) throws IOException, InterruptedException, ExecutionException, TimeoutException;
}
