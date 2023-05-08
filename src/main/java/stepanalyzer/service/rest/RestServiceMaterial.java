package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(RestServicePath.MATERIAL)
public interface RestServiceMaterial {

    @GetMapping("/getMaterialList")
    List<MaterialBean> getMaterialList();

}
