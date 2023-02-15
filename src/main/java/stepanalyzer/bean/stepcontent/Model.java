package stepanalyzer.bean.stepcontent;

import java.io.Serializable;
import java.util.List;

public class Model implements Serializable {
    BoundingBox boundingBox;
    List<Components> components;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<Components> getComponents() {
        return components;
    }

    public void setComponents(List<Components> components) {
        this.components = components;
    }
}
