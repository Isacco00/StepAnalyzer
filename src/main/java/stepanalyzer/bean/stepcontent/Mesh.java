package stepanalyzer.bean.stepcontent;

import java.math.BigDecimal;
import java.util.List;

public class Mesh {
    String coordIndex;
    List<Coordinate> coordinates;
    String edgeIndex;
    BigDecimal edgePerimeter;

    public BigDecimal getEdgePerimeter() {
        return edgePerimeter;
    }

    public void setEdgePerimeter(BigDecimal edgePerimeter) {
        this.edgePerimeter = edgePerimeter;
    }

    public String getCoordIndex() {
        return coordIndex;
    }

    public void setCoordIndex(String coordIndex) {
        this.coordIndex = coordIndex;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getEdgeIndex() {
        return edgeIndex;
    }

    public void setEdgeIndex(String edgeIndex) {
        this.edgeIndex = edgeIndex;
    }
}
