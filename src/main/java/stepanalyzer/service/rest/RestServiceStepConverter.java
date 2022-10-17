package stepanalyzer.service.rest;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.request.bean.CurrencyRequestBean;

@RestController
@RequestMapping("/stpConverter")
public interface RestServiceStepConverter {

	@PostMapping("/fromStpToX3D")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	public String fromStpToX3D(@NotNull MultipartFormDataInput formData);
}
