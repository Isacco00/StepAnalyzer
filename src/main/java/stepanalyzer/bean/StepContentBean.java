package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import stepanalyzer.bean.stepcontent.StepJsonBean;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(value = Include.NON_NULL)
public class StepContentBean implements Serializable {
    private Long tokenStepContent;
    private String json;
    private StepJsonBean stepJsonBean;
    private String X3DContent;
    private BigDecimal lunghezzaX;
    private BigDecimal larghezzaY;
    private BigDecimal spessoreZ;
    private BigDecimal volume;
    private BigDecimal perimetro;
    private BigDecimal volumeAsportato;
    private BigDecimal pesoPezzo;
    private BigDecimal costoPezzoMateriale;

    public Long getTokenStepContent() {
        return tokenStepContent;
    }

    public void setTokenStepContent(Long tokenStepContent) {
        this.tokenStepContent = tokenStepContent;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public StepJsonBean getStepJsonBean() {
        return stepJsonBean;
    }

    public void setStepJsonBean(StepJsonBean stepJsonBean) {
        this.stepJsonBean = stepJsonBean;
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

    public BigDecimal getPerimetro() {
        return perimetro;
    }

    public void setPerimetro(BigDecimal perimetro) {
        this.perimetro = perimetro;
    }

    public BigDecimal getPesoPezzo() {
        return pesoPezzo;
    }

    public void setPesoPezzo(BigDecimal pesoPezzo) {
        this.pesoPezzo = pesoPezzo;
    }

    public BigDecimal getCostoPezzoMateriale() {
        return costoPezzoMateriale;
    }

    public void setCostoPezzoMateriale(BigDecimal costoPezzoMateriale) {
        this.costoPezzoMateriale = costoPezzoMateriale;
    }

    public BigDecimal getVolumeAsportato() {
        return volumeAsportato;
    }

    public void setVolumeAsportato(BigDecimal volumeAsportato) {
        this.volumeAsportato = volumeAsportato;
    }
}
