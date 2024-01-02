package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class PrivateTypeEnumTypeHandler extends BaseObjectTypeHandler<PrivateTypeEnum>{

    public PrivateTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
