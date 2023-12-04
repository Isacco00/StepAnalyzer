package stepanalyzer.service.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(RestServicePath.MATERIAL)
public interface RestServiceMaterial {

    @GetMapping("/getMaterialList")
    List<MaterialBean> getMaterialList();

    @PostMapping("/importMaterialsFromFile")
    void importMaterialsFromFile(@RequestParam("file") MultipartFile formData) throws IOException;

}
