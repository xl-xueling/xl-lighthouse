package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.AssetTypeEnum;

public class AssetUpdateParam {

    private Integer id;

    private String name;

    private AssetTypeEnum assetType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetTypeEnum getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetTypeEnum assetType) {
        this.assetType = assetType;
    }
}
