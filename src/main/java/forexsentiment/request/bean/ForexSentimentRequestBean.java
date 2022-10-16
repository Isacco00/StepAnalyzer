package forexsentiment.request.bean;

import org.springframework.stereotype.Component;

@Component
public class ForexSentimentRequestBean {
	private Boolean isValidForHistory;
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private String specificDay;

	public String getSpecificDay() {
		return specificDay;
	}

	public void setSpecificDay(String specificDay) {
		this.specificDay = specificDay;
	}

	public Boolean getIsValidForHistory() {
		return isValidForHistory;
	}

	public void setIsValidForHistory(Boolean isValidForHistory) {
		this.isValidForHistory = isValidForHistory;
	}

}
