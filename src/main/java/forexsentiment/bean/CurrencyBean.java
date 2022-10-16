package forexsentiment.bean;

import java.io.Serializable;

import javax.persistence.Id;

public class CurrencyBean implements Serializable {

	@Id
	private Long tokenCurrency;
	private String currencyName;

	public Long getTokenCurrency() {
		return tokenCurrency;
	}

	public void setTokenCurrency(Long tokenCurrency) {
		this.tokenCurrency = tokenCurrency;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
}
