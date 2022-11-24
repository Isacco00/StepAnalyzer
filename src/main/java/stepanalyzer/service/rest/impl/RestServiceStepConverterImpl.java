package stepanalyzer.service.rest.impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.service.rest.RestServiceStepConverter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component public class RestServiceStepConverterImpl implements RestServiceStepConverter {

    @Inject private StepConverterManager stepConverterManager;
    @Inject private StepManager stepManager;

    @Override public String fromStpToX3D(@RequestParam("file") MultipartFile file)
        throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return stepConverterManager.fromStpToX3D(file);
    }

    @Override public ResponseEntity<Resource> fromStpToStl(@RequestParam("file") MultipartFile file)
        throws IOException, ExecutionException, InterruptedException, TimeoutException {
        DocumentBean response = stepConverterManager.fromStpToStl(file);
        ByteArrayResource resource = new ByteArrayResource(response.getPayload());
        return ResponseEntity.ok().header("Content-Disposition", "attachment" + "; filename=\"" + response.getFileName() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @Override public String testGet() {
        return "";
    }

    @Override public StepBean stpCalculator(@RequestParam("file") MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return stepManager.stpCalculator(file);
    }
}
