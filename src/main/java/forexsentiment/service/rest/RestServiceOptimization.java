package forexsentiment.service.rest;

import forexsentiment.bean.OptimizationBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;
@RestController
@RequestMapping("/optimization")
public interface RestServiceOptimization {

    @GetMapping("/downloadOptimization")
    @ResponseBody List<OptimizationBean> downloadOptimization(String fileUrl) throws MalformedURLException;
}
