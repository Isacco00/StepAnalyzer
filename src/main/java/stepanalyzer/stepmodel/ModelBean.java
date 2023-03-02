package stepanalyzer.stepmodel;

import org.jcae.opencascade.jni.TopoDS_Shape;

public class ModelBean {
    TopoDS_Shape shape;
    boolean faceSet;

    public boolean isFaceSet() {
        return faceSet;
    }

    public void setFaceSet(boolean faceSet) {
        this.faceSet = faceSet;
    }

    public TopoDS_Shape getShape() {
        return shape;
    }

    public void setShape(TopoDS_Shape shape) {
        this.shape = shape;
    }
}
