package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import stepanalyzer.bean.stepcontent.StepContentBean;

import java.io.Serializable;

@JsonInclude(value = Include.NON_NULL)
public class StepJsonBean implements Serializable {
    private Long tokenStepJson;
    private String json;
    private StepContentBean stepContentBean;

    public Long getTokenStepJson() {
        return tokenStepJson;
    }

    public void setTokenStepJson(Long tokenStepJson) {
        this.tokenStepJson = tokenStepJson;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public StepContentBean getStepContentBean() {
        return stepContentBean;
    }

    public void setStepContentBean(StepContentBean stepContentBean) {
        this.stepContentBean = stepContentBean;
    }
}
