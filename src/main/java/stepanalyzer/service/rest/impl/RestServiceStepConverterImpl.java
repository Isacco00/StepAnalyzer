package stepanalyzer.service.rest.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.service.rest.RestServiceStepConverter;

@Component public class RestServiceStepConverterImpl implements RestServiceStepConverter {

    @Inject private StepConverterManager stepConverterManager;

    @Override public String fromStpToX3D() {
        return stepConverterManager.fromStpToX3D();
    }
}
