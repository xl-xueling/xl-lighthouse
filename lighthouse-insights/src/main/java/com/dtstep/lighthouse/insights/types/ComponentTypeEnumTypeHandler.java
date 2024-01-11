package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;
import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class ComponentTypeEnumTypeHandler extends BaseObjectTypeHandler<ComponentTypeEnum>{

    public ComponentTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
