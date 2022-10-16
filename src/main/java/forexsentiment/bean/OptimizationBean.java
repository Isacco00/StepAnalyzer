package forexsentiment.bean;

import javax.persistence.Id;
import java.io.Serializable;

@SuppressWarnings("serial")
public class OptimizationBean implements Serializable {

	public Long getTokenOptimization() {
		return tokenOptimization;
	}

	public void setTokenOptimization(Long tokenOptimization) {
		this.tokenOptimization = tokenOptimization;
	}

	public boolean isActivePair() {
		return activePair;
	}

	public void setActivePair(boolean activePair) {
		this.activePair = activePair;
	}

	public int getFirstRangeSentiment() {
		return firstRangeSentiment;
	}

	public void setFirstRangeSentiment(int firstRangeSentiment) {
		this.firstRangeSentiment = firstRangeSentiment;
	}

	public int getSecondRangeSentiment() {
		return secondRangeSentiment;
	}

	public void setSecondRangeSentiment(int secondRangeSentiment) {
		this.secondRangeSentiment = secondRangeSentiment;
	}

	public boolean isUseBreakeven() {
		return useBreakeven;
	}

	public void setUseBreakeven(boolean useBreakeven) {
		this.useBreakeven = useBreakeven;
	}

	@Id
	private Long tokenOptimization;
	private boolean activePair;
	private int firstRangeSentiment;
	private int secondRangeSentiment;
	private boolean useBreakeven;
}
