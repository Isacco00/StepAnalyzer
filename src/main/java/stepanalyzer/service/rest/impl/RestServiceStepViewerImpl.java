package stepanalyzer.service.rest.impl;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.StepBean;
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
}
