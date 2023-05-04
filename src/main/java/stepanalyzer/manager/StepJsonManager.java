package stepanalyzer.manager;

import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.StepJsonBean;
import stepanalyzer.bean.stepcontent.StepContentBean;

import java.io.IOException;
import java.util.List;

public interface StepJsonManager {

    StepJsonBean saveStepJson(StepJsonBean stepJsonBean) throws IOException;

}
