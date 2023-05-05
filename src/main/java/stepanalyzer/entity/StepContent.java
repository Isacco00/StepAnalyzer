package stepanalyzer.entity;

import jakarta.persistence.*;

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

    @OneToOne(mappedBy = "stepContent")
    private Step step;

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
}
