package stepanalyzer.service.rest.impl;

import javax.inject.Inject;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Component;

import stepanalyzer.manager.StepConverterManager;
import stepanalyzer.service.rest.RestServiceStepConverter;

@Component public class RestServiceStepConverterImpl implements RestServiceStepConverter {

    @Inject private StepConverterManager stepConverterManager;

    @Override public String fromStpToX3D(MultipartFormDataInput formData) {
        return stepConverterManager.fromStpToX3D();
    }
}
