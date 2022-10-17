package stepanalyzer.service.rest;

import java.util.List;

import javax.ws.rs.BeanParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.request.bean.CurrencyRequestBean;

@RestController
@RequestMapping("/stpConverter")
public interface RestServiceStepConverter {

	@GetMapping("/fromStpToX3D")
	public String fromStpToX3D();
}
