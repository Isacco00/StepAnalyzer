package stepanalyzer.bean.stepcontent;

import java.math.BigDecimal;

public class Appearance {
    Color diffuseColor;
    Double shininess;
    Color specularColor;

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Double getShininess() {
        return shininess;
    }

    public void setShininess(Double shininess) {
        this.shininess = shininess;
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Color specularColor) {
        this.specularColor = specularColor;
    }
}
