package stepanalyzer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "step_content")
@NamedQuery(name = "StepContent.findAll", query = "SELECT s FROM StepContent s")
public class StepContent {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "STEP_CONTENT_TOKENSTEPCONTENT_GENERATOR", sequenceName = "STEP_CONTENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_CONTENT_TOKENSTEPCONTENT_GENERATOR")
    @Column(name = "token_step_content")
    private Long tokenStepContent;

    @Column(name = "json", columnDefinition = "jsonb")
    private String json;
    @Column(name = "perimetro")
    private BigDecimal perimetro;
    @Column(name = "volume", precision = 19, scale = 3)
    private BigDecimal volume;
    @OneToOne(mappedBy = "stepContent")
    private Step step;
    @Column(name = "lunghezza_x")
    private BigDecimal lunghezzaX;
    @Column(name = "larghezza_y")
    private BigDecimal larghezzaY;
    @Column(name = "spessore_z")
    private BigDecimal spessoreZ;
    @Column(name = "peso_pezzo", precision = 19, scale = 3)
    private BigDecimal pesoPezzo;
    @Column(name = "costo_pezzo_materiale", precision = 19, scale = 1)
    private BigDecimal costoPezzoMateriale;

    public Long getTokenStepContent() {
        return tokenStepContent;
    }

    public void setTokenStepContent(Long tokenStepJson) {
        this.tokenStepContent = tokenStepJson;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public BigDecimal getPerimetro() {
        return perimetro;
    }

    public void setPerimetro(BigDecimal perimetro) {
        this.perimetro = perimetro;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
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
}
