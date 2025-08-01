package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.AssetTypeEnum;

public class AssetQueryParam {

    private Integer userId;

    private AssetTypeEnum assetType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public AssetTypeEnum getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetTypeEnum assetType) {
        this.assetType = assetType;
    }
}
