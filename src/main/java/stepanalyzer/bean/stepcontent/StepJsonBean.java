package stepanalyzer.bean.stepcontent;

import java.io.Serializable;

public class StepJsonBean implements Serializable {
    Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
