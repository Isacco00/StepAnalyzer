package stepanalyzer.websocket;

import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;

public class WebSocketOutputBean {

    StepBean stepBean;

    public StepBean getStepBean() {
        return stepBean;
    }

    public void setStepBean(StepBean stepBean) {
        this.stepBean = stepBean;
    }
}
