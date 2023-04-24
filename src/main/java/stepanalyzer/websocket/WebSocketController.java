package stepanalyzer.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepContentBean;
import stepanalyzer.manager.StepManager;

import javax.inject.Inject;
import java.io.IOException;

@Controller
public class WebSocketController {
    @Inject
    private StepManager stepManager;

    @MessageMapping("/processStepFile")
    @SendTo("/websocketapi/getProcessedStepFile")
    public WebSocketOutputBean processStepFile(WebSocketInputBean webSocketBean) throws IOException {
        WebSocketOutputBean outputBean = new WebSocketOutputBean();
        if (webSocketBean.getTokenStep() != null) {
            StepDetailBean bean = stepManager.getStepDetail(webSocketBean.getTokenStep());
            try {
                StepContentBean stepContentBean = stepManager.calculateStepFile(bean.getFileName());
                bean.setStepContent(stepContentBean);
                bean.setAction("Completed");
            } catch (Exception ex) {
                bean.setAction("Error");
            }
            outputBean.setStepBean(stepManager.saveStep(bean));
        }
        return outputBean;
    }
}
