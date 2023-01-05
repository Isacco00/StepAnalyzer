package stepanalyzer.manager;

import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.DocumentBean;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface StepManager {

    StepBean stpCalculator(MultipartFile file) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    List<StepBean> getStepBeanList();

    StepBean uploadStepFile(MultipartFile formData) throws IOException;

    StepBean getStepDetail(Long tokenStep) throws IOException;
}
