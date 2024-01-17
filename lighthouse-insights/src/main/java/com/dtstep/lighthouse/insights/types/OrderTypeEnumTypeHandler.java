package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderTypeEnumTypeHandler extends BaseObjectTypeHandler<OrderTypeEnum>{

    public OrderTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
