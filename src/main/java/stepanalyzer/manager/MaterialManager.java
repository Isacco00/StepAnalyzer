package stepanalyzer.manager;

import stepanalyzer.bean.MaterialBean;

import java.util.List;

public interface MaterialManager {

    List<MaterialBean> getMaterialList();
    MaterialBean getDefaultMaterial();

}
