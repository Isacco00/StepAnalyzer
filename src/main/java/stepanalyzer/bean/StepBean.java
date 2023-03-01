package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import stepanalyzer.bean.stepcontent.StepContentBean;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@JsonInclude(value = Include.NON_NULL)
public class StepBean implements Serializable {
    private Long tokenStep;
    private String fileName;
    private StepContentBean stepContent;
    private String X3DContent;
    private BigDecimal lunghezzaX;
    private BigDecimal larghezzaY;
    private BigDecimal spessoreZ;
    private BigDecimal volume;

    public Long getTokenStep() {
        return tokenStep;
    }

    public void setTokenStep(Long tokenStep) {
        this.tokenStep = tokenStep;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public StepContentBean getStepContent() {
        return stepContent;
    }

    public void setStepContent(StepContentBean stepContent) {
        this.stepContent = stepContent;
    }

    public String getX3DContent() {
        return X3DContent;
    }

    public void setX3DContent(String x3DContent) {
        X3DContent = x3DContent;
    }

    public BigDecimal getLunghezzaX() {
        return lunghezzaX;
    }

    public void setLunghezzaX(BigDecimal lunghezzaX) {
        this.lunghezzaX = lunghezzaX;
    }

    public BigDecimal getLarghezzaY() {
        return larghezzaY;
    }

    public void setLarghezzaY(BigDecimal larghezzaY) {
        this.larghezzaY = larghezzaY;
    }

    public BigDecimal getSpessoreZ() {
        return spessoreZ;
    }

    public void setSpessoreZ(BigDecimal spessoreZ) {
        this.spessoreZ = spessoreZ;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}
