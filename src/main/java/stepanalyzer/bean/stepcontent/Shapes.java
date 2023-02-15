package stepanalyzer.bean.stepcontent;

import java.io.Serializable;
import java.util.List;

public class Shapes implements Serializable {
    Appearance appearance;
    FaceSet faceSet;
    List<Mesh> mesh;
    String shapeID;
    String shapeName;
    Integer stepID;

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public FaceSet getFaceSet() {
        return faceSet;
    }

    public void setFaceSet(FaceSet faceSet) {
        this.faceSet = faceSet;
    }

    public List<Mesh> getMesh() {
        return mesh;
    }

    public void setMesh(List<Mesh> mesh) {
        this.mesh = mesh;
    }

    public String getShapeID() {
        return shapeID;
    }

    public void setShapeID(String shapeID) {
        this.shapeID = shapeID;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public Integer getStepID() {
        return stepID;
    }

    public void setStepID(Integer stepID) {
        this.stepID = stepID;
    }
}
