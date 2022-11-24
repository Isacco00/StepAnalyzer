package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@JsonInclude(value = Include.NON_NULL)
public class StepBean implements Serializable {
    private String tokenStep;
    private String fileName;
    private BigDecimal lunghezzaX;
    private BigDecimal larghezzaY;
    private BigDecimal spessoreZ;
    private BigDecimal volume;


    public BigDecimal getLunghezzaX() {
        return lunghezzaX;
    }

    public void setLunghezzaX(BigDecimal lunghezzaX) {
        this.lunghezzaX = lunghezzaX;
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

    public String getTokenStep() {
        return tokenStep;
    }

    public void setTokenStep(String tokenStep) {
        this.tokenStep = tokenStep;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getLarghezzaY() {
        return larghezzaY;
    }

    public void setLarghezzaY(BigDecimal larghezzaY) {
        this.larghezzaY = larghezzaY;
    }
}
