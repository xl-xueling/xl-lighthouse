package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class ApproveStateEnumTypeHandler extends BaseObjectTypeHandler<ApproveStateEnum>{

    public ApproveStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
