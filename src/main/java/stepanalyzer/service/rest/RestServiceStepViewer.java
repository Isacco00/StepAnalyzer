package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stepanalyzer.bean.StepBean;

import java.io.IOException;

@RestController
@RequestMapping(RestServicePath.STEPVIEWER)
public interface RestServiceStepViewer {

    @GetMapping("/getStepDetail/{token}")
    StepBean getStepDetail(@PathVariable("token") Long tokenStep) throws IOException;

}
