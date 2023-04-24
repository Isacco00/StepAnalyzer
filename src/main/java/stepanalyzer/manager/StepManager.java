package stepanalyzer.manager;

import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepContentBean;

import java.io.IOException;
import java.util.List;

public interface StepManager {

    List<StepBean> getStepBeanList();

    StepBean uploadStepFile(MultipartFile formData) throws IOException;

    StepBean saveStep(StepDetailBean stepDetailBean) throws IOException;

    StepDetailBean getStepDetail(Long tokenStep) throws IOException;

    StepContentBean calculateStepFile(String fileName);

    void deleteStepFile(Long tokenStep);
}
