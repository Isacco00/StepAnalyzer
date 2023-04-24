package stepanalyzer.entity;

import javax.persistence.*;

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

    @Column(name = "step_content", columnDefinition = "jsonb")
    private String stepContent;

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

    public String getStepContent() {
        return stepContent;
    }

    public void setStepContent(String stepContent) {
        this.stepContent = stepContent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
