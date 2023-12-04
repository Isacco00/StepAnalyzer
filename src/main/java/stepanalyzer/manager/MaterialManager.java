package stepanalyzer.manager;

import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.List;

public interface MaterialManager {

    List<MaterialBean> getMaterialList();
    MaterialBean getDefaultMaterial();

    void importMaterialsFromFile(MultipartFile formData) throws IOException;
}
