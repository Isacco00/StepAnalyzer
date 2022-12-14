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
}
