package stepanalyzer.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "step_json")
@NamedQuery(name = "StepJson.findAll", query = "SELECT s FROM StepJson s")
public class StepJson {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "STEP_JSON_TOKENSTEPJSON_GENERATOR", sequenceName = "STEP_JSON_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_JSON_TOKENSTEPJSON_GENERATOR")
    @Column(name = "token_step_json")
    private Long tokenStepJson;

    @Column(name = "json", columnDefinition = "jsonb")
    private String json;

    //bi-directional many-to-one association to Country
    @OneToMany(mappedBy = "stepJson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Step> steps = new HashSet<>();

    public Long getTokenStepJson() {
        return tokenStepJson;
    }

    public void setTokenStepJson(Long tokenStepJson) {
        this.tokenStepJson = tokenStepJson;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Set<Step> getSteps() {
        return steps;
    }

    public void setSteps(Set<Step> steps) {
        this.steps = steps;
    }
}
