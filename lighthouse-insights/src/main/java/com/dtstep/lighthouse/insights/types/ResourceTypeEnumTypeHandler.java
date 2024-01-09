package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class ResourceTypeEnumTypeHandler extends BaseObjectTypeHandler<ResourceTypeEnum>{

    public ResourceTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
