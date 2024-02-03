package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class ResourceTypeEnumTypeHandler extends BaseObjectTypeHandler<ResourceTypeEnum>{

    public ResourceTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
