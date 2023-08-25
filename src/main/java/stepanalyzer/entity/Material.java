package stepanalyzer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "material")
@NamedQuery(name = "Material.findAll", query = "SELECT m FROM Material m")
public class Material {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "MATERIAL_TOKENMATERIAL_GENERATOR", sequenceName = "MATERIAL_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MATERIAL_TOKENMATERIAL_GENERATOR")
    @Column(name = "token_material")
    private Long tokenMaterial;
    @Column(name = "descrizione", nullable = false)
    private String descrizione;
    @Column(name = "dimensioni")
    private String dimensioni;
    @Column(name = "spessore")
    private String spessore;
    @Column(name = "peso_specifico", precision = 19, scale = 2)
    private BigDecimal pesoSpecifico;
    @Column(name = "peso_specifico_costo")
    private BigDecimal pesoSpecificoCosto;
    @Column(name = "trasporto")
    private BigDecimal trasporto;
    @Column(name = "costoAlKg")
    private BigDecimal costoAlKg;
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> step;

    public List<Step> getStep() {
        return step;
    }

    public void setStep(List<Step> step) {
        this.step = step;
    }

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
