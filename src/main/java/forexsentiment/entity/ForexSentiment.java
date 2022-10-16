package forexsentiment.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "forex_sentiment")
@NamedQuery(name = "ForexSentiment.findAll", query = "SELECT f FROM ForexSentiment f")
public class ForexSentiment {
	@Id
	@SequenceGenerator(allocationSize = 1, name = "FOREX_SENTIMENT_TOKENFOREXSENTIMENT_GENERATOR", sequenceName = "FOREX_SENTIMENT_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOREX_SENTIMENT_TOKENFOREXSENTIMENT_GENERATOR")
	@Column(name = "token_forex_sentiment")
	private Long tokenForexSentiment;

	@Column(name = "update_timestamp", nullable = false)
	private OffsetDateTime updateTimestamp;

	@Column(name = "long_position", nullable = false, precision = 2, scale = 12)
	private BigDecimal longPosition;

	@Column(name = "short_position", nullable = false, precision = 2, scale = 12)
	private BigDecimal shortPosition;

	@Column(name = "version", nullable = false)
	private int version;

	@Column(name = "is_valid_for_history")
	private boolean isValidForHistory;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "token_currency")
	private Currency currency;

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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
