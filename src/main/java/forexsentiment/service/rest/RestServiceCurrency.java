package forexsentiment.service.rest;

import java.util.List;

import javax.ws.rs.BeanParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.request.bean.CurrencyRequestBean;

@RestController
@RequestMapping("/currency")
public interface RestServiceCurrency {

	@GetMapping("/getCurrencyList")
	public List<CurrencyBean> getCurrencyList(@BeanParam CurrencyRequestBean currencyRequestBean);
}
