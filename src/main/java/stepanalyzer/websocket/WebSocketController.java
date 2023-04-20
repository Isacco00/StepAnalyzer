package stepanalyzer.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;

import java.io.Console;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {

    @MessageMapping("/processStepFile")
    @SendTo("/websocketapi/processStepFile")
    public List<StepBean> greet(StepBean stepBean) throws InterruptedException {
        return Collections.singletonList(stepBean);
    }
}
