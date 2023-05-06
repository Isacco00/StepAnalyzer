package stepanalyzer.manager;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepDetailBean;
import stepanalyzer.bean.stepcontent.StepJsonBean;

import java.io.IOException;
import java.util.List;

public interface StepManager {

    List<StepBean> getStepBeanList();

    StepBean uploadStepFile(MultipartFile formData) throws IOException;

    StepBean saveStep(StepDetailBean stepDetailBean) throws IOException;

    StepDetailBean getStepDetail(Long tokenStep) throws IOException;

    StepJsonBean calculateStepFile(StepDetailBean fileName);

    void deleteStepFile(Long tokenStep);

    StepBean processStepFile(@NotNull Long tokenStep);
}
