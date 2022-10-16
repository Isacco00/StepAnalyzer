package forexsentiment.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "forex_sentiment_version")
@NamedQuery(name = "ForexSentimentVersion.findAll", query = "SELECT f FROM ForexSentimentVersion f")
public class ForexSentimentVersion {
	@Id
	@SequenceGenerator(allocationSize = 1, name = "FOREX_SENTIMENT_VERSION_TOKENFOREXSENTIMENTVERSION_GENERATOR", sequenceName = "FOREX_SENTIMENT_VERSION_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOREX_SENTIMENT_VERSION_TOKENFOREXSENTIMENTVERSION_GENERATOR")
	@Column(name = "token_forex_sentiment_version")
	private Long tokenForexSentimentVersion;

	@Column(name = "update_timestamp", nullable = false)
	private OffsetDateTime updateTimestamp;

	@Column(name = "most_recent_version")
	private int mostRecentVersion;

	public Long getTokenForexSentimentVersion() {
		return tokenForexSentimentVersion;
	}

	public void setTokenForexSentimentVersion(Long tokenForexSentimentVersion) {
		this.tokenForexSentimentVersion = tokenForexSentimentVersion;
	}

	public OffsetDateTime getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(OffsetDateTime updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public int getMostRecentVersion() {
		return mostRecentVersion;
	}

	public void setMostRecentVersion(int mostRecentVersion) {
		this.mostRecentVersion = mostRecentVersion;
	}
}
