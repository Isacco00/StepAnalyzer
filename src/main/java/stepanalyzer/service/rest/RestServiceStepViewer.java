package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.*;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;

import java.io.IOException;

@RestController
@RequestMapping(RestServicePath.STEPVIEWER)
public interface RestServiceStepViewer {

    @GetMapping("/getStepDetail/{token}")
    StepBean getStepDetail(@PathVariable("token") Long tokenStep) throws IOException;
    @PostMapping("/calculateStepFile")
    StepDetailBean calculateStepFile(@RequestBody StepDetailBean bean) throws IOException;

}
