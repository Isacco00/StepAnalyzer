package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(value = Include.NON_NULL)
public class StepDetailBean extends StepBean implements Serializable {
    private StepContentBean stepContent;
    private MaterialBean materialBean;

    public MaterialBean getMaterialeBean() {
        return materialBean;
    }

    public void setMaterialeBean(MaterialBean materialBean) {
        this.materialBean = materialBean;
    }

    public StepContentBean getStepContent() {
        return stepContent;
    }

    public void setStepContent(StepContentBean stepContent) {
        this.stepContent = stepContent;
    }
}
