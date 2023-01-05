package stepanalyzer.service.rest.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.manager.StepManager;
import stepanalyzer.service.rest.RestServiceDashboard;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Component public class RestServiceDashboardImpl implements RestServiceDashboard {

    @Inject private StepManager stepManager;

    @Override
    public List<StepBean> getStepBeanList() {
        return stepManager.getStepBeanList();
    }

    @Override
    public StepBean uploadStepFile(MultipartFile formData) throws IOException {
        return stepManager.uploadStepFile(formData);
    }
}
