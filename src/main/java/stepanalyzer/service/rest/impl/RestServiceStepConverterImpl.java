package stepanalyzer.service.rest.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.service.rest.RestServiceStepConverter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class RestServiceStepConverterImpl implements RestServiceStepConverter {

    @Inject
    private StepConverterManager stepConverterManager;

    @Override
    public String fromStpToX3D(@RequestParam("file") MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return stepConverterManager.fromStpToX3D(file);
    }

    @Override
    public String testGet() {
        return "";
    }

    @Override
    public String fromStpToX3DCalculator(@RequestParam("file") MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return stepConverterManager.fromStpToX3DCalculator(file);
    }
}
