package stepanalyzer.bean.stepcontent;

import java.io.Serializable;
import java.util.List;

public class Components implements Serializable {
    List<Shapes> shapes;

    public List<Shapes> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shapes> shapes) {
        this.shapes = shapes;
    }
}
