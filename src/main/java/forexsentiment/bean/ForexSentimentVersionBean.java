package forexsentiment.bean;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Id;

public class ForexSentimentVersionBean implements Serializable {

	@Id
	private Long tokenForexSentimentVersion;
	private OffsetDateTime updateTimestamp;
	private Long mostRecentVersion;

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

	public Long getMostRecentVersion() {
		return mostRecentVersion;
	}

	public void setMostRecentVersion(Long mostRecentVersion) {
		this.mostRecentVersion = mostRecentVersion;
	}
}
