package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class StatStateEnumTypeHandler extends BaseObjectTypeHandler<StatStateEnum>{

    public StatStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
