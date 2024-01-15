package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class RelationTypeEnumTypeHandler extends BaseObjectTypeHandler<RelationTypeEnum>{

    public RelationTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
