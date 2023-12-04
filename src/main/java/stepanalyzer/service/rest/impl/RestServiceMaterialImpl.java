package stepanalyzer.service.rest.impl;

import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.bean.StepBean;
import stepanalyzer.manager.MaterialManager;
import stepanalyzer.manager.StepManager;
import stepanalyzer.service.rest.RestServiceDashboard;
import stepanalyzer.service.rest.RestServiceMaterial;

import java.io.IOException;
import java.util.List;

@Component
public class RestServiceMaterialImpl implements RestServiceMaterial {

    @Inject
    private MaterialManager materialManager;

    @Override
    public List<MaterialBean> getMaterialList() {
        return materialManager.getMaterialList();
    }

    @Override
    public void importMaterialsFromFile(MultipartFile formData) throws IOException {
        materialManager.importMaterialsFromFile(formData);
    }
}