package stepanalyzer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "costanti_calcolo_costi")
@NamedQuery(name = "CostantiCalcoloCosti.findAll", query = "SELECT s FROM CostantiCalcoloCosti s")
public class CostantiCalcoloCosti {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "COSTANTI_CALCOLO_COSTI_TOKENCOSTANTICALCOLOCOSTI_GENERATOR", sequenceName = "COSTANTI_CALCOLO_COSTI_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSTANTI_CALCOLO_COSTI_TOKENCOSTANTICALCOLOCOSTI_GENERATOR")
    @Column(name = "token_costanti_calcolo_costi")
    private Long tokenCostantiCalcoloCosti;
    @OneToOne(mappedBy = "costantiCalcoloCosti")
    private Step step;
    @Column(name = "cnc")
    private BigDecimal cnc;
    @Column(name = "attrezzaggio")
    private BigDecimal attrezzaggio;
    @Column(name = "costi_calibratrice")
    private BigDecimal costiCalibratrice;
    @Column(name = "rettifica", precision = 19, scale = 3)
    private BigDecimal rettifica;
    @Column(name = "accessori", precision = 19, scale = 1)
    private BigDecimal accessori;

    public Long getTokenCostantiCalcoloCosti() {
        return tokenCostantiCalcoloCosti;
    }

    public void setTokenCostantiCalcoloCosti(Long tokenCostantiCalcoloCosti) {
        this.tokenCostantiCalcoloCosti = tokenCostantiCalcoloCosti;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public BigDecimal getCnc() {
        return cnc;
    }

    public void setCnc(BigDecimal cnc) {
        this.cnc = cnc;
    }

    public BigDecimal getAttrezzaggio() {
        return attrezzaggio;
    }

    public void setAttrezzaggio(BigDecimal attrezzaggio) {
        this.attrezzaggio = attrezzaggio;
    }

    public BigDecimal getCostiCalibratrice() {
        return costiCalibratrice;
    }

    public void setCostiCalibratrice(BigDecimal costiCalibratrice) {
        this.costiCalibratrice = costiCalibratrice;
    }

    public BigDecimal getRettifica() {
        return rettifica;
    }

    public void setRettifica(BigDecimal rettifica) {
        this.rettifica = rettifica;
    }

    public BigDecimal getAccessori() {
        return accessori;
    }

    public void setAccessori(BigDecimal accessori) {
        this.accessori = accessori;
    }
}
