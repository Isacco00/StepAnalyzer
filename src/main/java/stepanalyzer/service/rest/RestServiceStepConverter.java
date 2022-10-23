package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/stpConverter")
public interface RestServiceStepConverter {

    @PostMapping("/fromStpToX3D")
    String fromStpToX3D(@RequestParam("file") MultipartFile formData) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    @PostMapping("/fromStpToX3DCalculator")
    String fromStpToX3DCalculator(@RequestParam("file") MultipartFile formData) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    @GetMapping("/testGet")
    String testGet();
}
