package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stepanalyzer.bean.StepBean;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import java.io.IOException;

@RestController
@RequestMapping("/stepViewer")
public interface RestServiceStepViewer {

    @GetMapping("/getStepDetail/{token}")
    StepBean getStepDetail(@PathVariable("token") Long tokenStep) throws IOException;

}
