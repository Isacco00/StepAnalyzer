package stepanalyzer.mapper;

import org.springframework.stereotype.Component;
import stepanalyzer.bean.MaterialBean;
import stepanalyzer.entity.Material;

@Component
public class MaterialMapper extends AbstractMapper<Material, MaterialBean> {

    protected MaterialBean doMapping(Material entity) {
        return doMapping(new MaterialBean(), entity);
    }

    protected MaterialBean doMapping(MaterialBean bean, Material entity) {
        bean.setTokenMaterial(entity.getTokenMaterial());
        bean.setDescrizione(entity.getDescrizione());
        bean.setCostoAlKg(entity.getCostoAlKg());
        bean.setDimensioni(entity.getDimensioni());
        bean.setPesoSpecifico(entity.getPesoSpecifico());
        bean.setPesoSpecificoCosto(entity.getPesoSpecificoCosto());
        bean.setSpessore(entity.getSpessore());
        bean.setTrasporto(entity.getTrasporto());
        return bean;
    }

}
