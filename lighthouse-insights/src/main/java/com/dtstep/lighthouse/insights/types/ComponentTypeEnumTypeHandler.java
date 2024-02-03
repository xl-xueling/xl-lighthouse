package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.ComponentTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class ComponentTypeEnumTypeHandler extends BaseObjectTypeHandler<ComponentTypeEnum>{

    public ComponentTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
