package stepanalyzer.bean.stepcontent;

import java.io.Serializable;
import java.math.BigDecimal;

public class BoundingBox implements Serializable {
    BigDecimal xMax;
    BigDecimal yMax;
    BigDecimal zMax;
    BigDecimal xMin;
    BigDecimal yMin;
    BigDecimal zMin;

    public BigDecimal getxMax() {
        return xMax;
    }

    public void setxMax(BigDecimal xMax) {
        this.xMax = xMax;
    }

    public BigDecimal getyMax() {
        return yMax;
    }

    public void setyMax(BigDecimal yMax) {
        this.yMax = yMax;
    }

    public BigDecimal getzMax() {
        return zMax;
    }

    public void setzMax(BigDecimal zMax) {
        this.zMax = zMax;
    }

    public BigDecimal getxMin() {
        return xMin;
    }

    public void setxMin(BigDecimal xMin) {
        this.xMin = xMin;
    }

    public BigDecimal getyMin() {
        return yMin;
    }

    public void setyMin(BigDecimal yMin) {
        this.yMin = yMin;
    }

    public BigDecimal getzMin() {
        return zMin;
    }

    public void setzMin(BigDecimal zMin) {
        this.zMin = zMin;
    }
}
