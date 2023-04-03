package stepanalyzer.service.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(RestServicePath.STEPCONVERTER)
public interface RestServiceStepConverter {

    @PostMapping("/fromStpToX3D")
    String fromStpToX3D(@RequestParam("file") MultipartFile formData)
            throws IOException, ExecutionException, InterruptedException, TimeoutException;

    @PostMapping("/fromStpToStl")
    ResponseEntity<Resource> fromStpToStl(@RequestParam("file") MultipartFile formData)
            throws IOException, ExecutionException, InterruptedException, TimeoutException;

    @PostMapping("/stpCalculator")
    StepBean stpCalculator(@RequestParam("file") MultipartFile formData)
            throws IOException, ExecutionException, InterruptedException, TimeoutException;

    @GetMapping("/testGet")
    String testGet();

    @PostMapping("/fromStpToX3DCalculator")
    String fromStpToX3DCalculator(@RequestParam("file") MultipartFile formData)
            throws IOException, ExecutionException, InterruptedException, TimeoutException;
}
