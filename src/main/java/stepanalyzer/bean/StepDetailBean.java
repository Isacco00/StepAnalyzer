package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(value = Include.NON_NULL)
public class StepDetailBean extends StepBean implements Serializable {
    private StepContentBean stepContent;

    public StepContentBean getStepContent() {
        return stepContent;
    }

    public void setStepContent(StepContentBean stepContent) {
        this.stepContent = stepContent;
    }
}
