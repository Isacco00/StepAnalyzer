package forexsentiment.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class ForexSentimentBean implements Serializable {

	@Id
	private Long tokenForexSentiment;
	private OffsetDateTime updateTimestamp;
	private BigDecimal longPosition;
	private BigDecimal shortPosition;
	private CurrencyBean currency;
	private int version;
	private boolean isValidForHistory;
	private BigDecimal averageLongPosition;
	private BigDecimal averageShortPosition;

	public BigDecimal getAverageLongPosition() {
		return averageLongPosition;
	}

	public void setAverageLongPosition(BigDecimal averageLongPosition) {
		this.averageLongPosition = averageLongPosition;
	}

	public BigDecimal getAverageShortPosition() {
		return averageShortPosition;
	}

	public void setAverageShortPosition(BigDecimal averageShortPosition) {
		this.averageShortPosition = averageShortPosition;
	}

	public boolean isValidForHistory() {
		return isValidForHistory;
	}

	public void setValidForHistory(boolean isValidForHistory) {
		this.isValidForHistory = isValidForHistory;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public CurrencyBean getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyBean currency) {
		this.currency = currency;
	}

	public Long getTokenForexSentiment() {
		return tokenForexSentiment;
	}

	public void setTokenForexSentiment(Long tokenForexSentiment) {
		this.tokenForexSentiment = tokenForexSentiment;
	}

	public OffsetDateTime getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(OffsetDateTime updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public BigDecimal getLongPosition() {
		return longPosition;
	}

	public void setLongPosition(BigDecimal longPosition) {
		this.longPosition = longPosition;
	}

	public BigDecimal getShortPosition() {
		return shortPosition;
	}

	public void setShortPosition(BigDecimal shortPosition) {
		this.shortPosition = shortPosition;
	}

}
