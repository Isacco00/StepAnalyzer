package stepanalyzer.bean.stepcontent;

import java.io.Serializable;

public class StepContentBean implements Serializable {
    Long tokenStepContent;
    String stepContent;
    Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
