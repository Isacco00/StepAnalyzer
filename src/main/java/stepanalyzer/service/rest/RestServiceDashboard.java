package stepanalyzer.service.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(RestServicePath.DASHBOARD)
public interface RestServiceDashboard {

    @GetMapping("/getStepBeanList")
    List<StepBean> getStepBeanList();

    @PostMapping("/uploadStepFile")
    StepBean uploadStepFile(@RequestParam("file") MultipartFile formData) throws IOException;

    @GetMapping("/deleteStepFile/{token}")
    void deleteStepFile(@PathVariable("token") Long tokenStep) throws IOException;

}
