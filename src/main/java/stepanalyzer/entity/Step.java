package stepanalyzer.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

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
    @Column(name = "version")
    private long version;
    @Column(name = "update_timestamp")
    private OffsetDateTime updateTimestamp;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_step_content")
    private StepContent stepContent;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_costanti_calcolo_costi")
    private CostantiCalcoloCosti costantiCalcoloCosti;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_material")
    private Material material;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public StepContent getStepContent() {
        return stepContent;
    }

    public void setStepContent(StepContent stepContent) {
        this.stepContent = stepContent;
    }

    public OffsetDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(OffsetDateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public CostantiCalcoloCosti getCostantiCalcoloCosti() {
        return costantiCalcoloCosti;
    }

    public void setCostantiCalcoloCosti(CostantiCalcoloCosti costantiCalcoloCosti) {
        this.costantiCalcoloCosti = costantiCalcoloCosti;
    }
}
