package stepanalyzer.manager;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;
import stepanalyzer.bean.StepContentBean;
import stepanalyzer.bean.StepDetailBean;

import java.io.IOException;
import java.util.List;

public interface StepManager {

    List<StepBean> getStepBeanList();

    StepBean uploadStepFile(MultipartFile formData) throws IOException;

    StepDetailBean saveStep(StepDetailBean stepDetailBean) throws IOException;

    StepDetailBean getStepDetail(Long tokenStep) throws IOException;

    StepDetailBean calculateStepFile(@NotNull StepDetailBean fileName, @NotNull StepContentBean contentBean);

    void deleteStepFile(Long tokenStep);

    StepBean processStepFile(@NotNull Long tokenStep);
}
