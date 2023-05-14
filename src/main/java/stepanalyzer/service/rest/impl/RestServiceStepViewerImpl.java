package stepanalyzer.service.rest.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.manager.StepManager;
import stepanalyzer.service.rest.RestServiceStepViewer;

import jakarta.inject.Inject;
import java.io.IOException;

@Component
public class RestServiceStepViewerImpl implements RestServiceStepViewer {

    @Inject
    private StepManager stepManager;

    @Override
    public StepBean getStepDetail(Long tokenStep) throws IOException {
        return stepManager.getStepDetail(tokenStep);
    }

    @Override
    public StepDetailBean calculateStepFile(@RequestBody StepDetailBean bean) throws IOException {
        return stepManager.saveStep(bean);
    }
}
