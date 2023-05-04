package stepanalyzer.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.StepJsonBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.manager.StepJsonManager;
import stepanalyzer.manager.StepManager;

import jakarta.inject.Inject;

import java.io.IOException;

@Controller
public class WebSocketController {
    @Inject
    private StepManager stepManager;
    @Inject
    private StepJsonManager stepJsonManager;

    @MessageMapping("/processStepFile")
    @SendTo("/websocketapi/getProcessedStepFile")
    public WebSocketOutputBean processStepFile(WebSocketInputBean webSocketBean) throws IOException {
        WebSocketOutputBean outputBean = new WebSocketOutputBean();
        if (webSocketBean.getTokenStep() != null) {
            StepDetailBean bean = stepManager.getStepDetail(webSocketBean.getTokenStep());
            try {
                StepContentBean stepContentBean = stepManager.calculateStepFile(bean.getFileName());
                StepJsonBean stepJsonBean = bean.getStepJson();
                if (stepJsonBean == null) {
                    stepJsonBean = new StepJsonBean();
                    stepJsonBean.setTokenStepJson(0L);
                }
                stepJsonBean.setStepContentBean(stepContentBean);
                bean.setStepJson(stepJsonManager.saveStepJson(stepJsonBean));
                bean.setAction("Completed");
            } catch (Exception ex) {
                bean.setAction("Error");
            }
            outputBean.setStepBean(stepManager.saveStep(bean));
        }
        return outputBean;
    }
}
