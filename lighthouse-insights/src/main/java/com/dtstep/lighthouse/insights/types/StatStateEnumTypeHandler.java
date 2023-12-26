package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class StatStateEnumTypeHandler extends BaseObjectTypeHandler<StatStateEnum>{

    public StatStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
