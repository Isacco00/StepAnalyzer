package stepanalyzer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(value = Include.NON_NULL)
public class MaterialBean implements Serializable {
    Long tokenMaterial;
    private String descrizione;
    private String dimensioni;
    private String spessore;
    private BigDecimal pesoSpecifico;
    private BigDecimal pesoSpecificoCosto;
    private BigDecimal trasporto;
    private BigDecimal costoAlKg;

    public Long getTokenMaterial() {
        return tokenMaterial;
    }

    public void setTokenMaterial(Long tokenMaterial) {
        this.tokenMaterial = tokenMaterial;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDimensioni() {
        return dimensioni;
    }

    public void setDimensioni(String dimensioni) {
        this.dimensioni = dimensioni;
    }

    public String getSpessore() {
        return spessore;
    }

    public void setSpessore(String spessore) {
        this.spessore = spessore;
    }

    public BigDecimal getPesoSpecifico() {
        return pesoSpecifico;
    }

    public void setPesoSpecifico(BigDecimal pesoSpecifico) {
        this.pesoSpecifico = pesoSpecifico;
    }

    public BigDecimal getPesoSpecificoCosto() {
        return pesoSpecificoCosto;
    }

    public void setPesoSpecificoCosto(BigDecimal pesoSpecificoCosto) {
        this.pesoSpecificoCosto = pesoSpecificoCosto;
    }

    public BigDecimal getTrasporto() {
        return trasporto;
    }

    public void setTrasporto(BigDecimal trasporto) {
        this.trasporto = trasporto;
    }

    public BigDecimal getCostoAlKg() {
        return costoAlKg;
    }

    public void setCostoAlKg(BigDecimal costoAlKg) {
        this.costoAlKg = costoAlKg;
    }
}
