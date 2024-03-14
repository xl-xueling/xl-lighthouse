package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class SwitchStateEnumTypeHandler extends BaseObjectTypeHandler<SwitchStateEnum>{

    public SwitchStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
