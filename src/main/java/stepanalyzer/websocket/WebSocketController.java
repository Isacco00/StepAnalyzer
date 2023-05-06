package stepanalyzer.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepJsonBean;
import stepanalyzer.manager.StepManager;

import jakarta.inject.Inject;

import java.io.IOException;

@Controller
public class WebSocketController {
    @Inject
    private StepManager stepManager;

    @MessageMapping("/processStepFile")
    @SendTo("/websocketapi/getProcessedStepFile")
    public WebSocketOutputBean processStepFile(WebSocketInputBean webSocketBean) {
        WebSocketOutputBean outputBean = new WebSocketOutputBean();
        if (webSocketBean.getTokenStep() != null) {
            StepBean bean = stepManager.processStepFile(webSocketBean.getTokenStep());
            outputBean.setStepBean(bean);
        }
        return outputBean;
    }
}
