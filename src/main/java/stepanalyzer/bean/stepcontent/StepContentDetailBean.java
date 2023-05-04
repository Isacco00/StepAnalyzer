package stepanalyzer.bean.stepcontent;

import java.io.Serializable;

public class StepContentDetailBean extends StepContentBean implements Serializable {


    public Long getTokenStepContent() {
        return tokenStepContent;
    }

    public void setTokenStepContent(Long tokenStepContent) {
        this.tokenStepContent = tokenStepContent;
    }

    public String getStepContent() {
        return stepContent;
    }

    public void setStepContent(String stepContent) {
        this.stepContent = stepContent;
    }
}
