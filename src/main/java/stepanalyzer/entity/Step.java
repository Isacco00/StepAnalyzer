package stepanalyzer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "step")
@NamedQuery(name = "Step.findAll", query = "SELECT s FROM Step s")
public class Step {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "STEP_TOKENSTEP_GENERATOR", sequenceName = "STEP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_TOKENSTEP_GENERATOR")
    @Column(name = "token_step")
    private Long tokenStep;

    @Column(name = "fileName", nullable = false)
    private String fileName;
    @Column(name = "action")
    private String action;

    //bi-directional many-to-one association to GeoRegion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_step_json")
    private StepJson stepJson;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public StepJson getStepJson() {
        return stepJson;
    }

    public void setStepJson(StepJson stepJson) {
        this.stepJson = stepJson;
    }
}
