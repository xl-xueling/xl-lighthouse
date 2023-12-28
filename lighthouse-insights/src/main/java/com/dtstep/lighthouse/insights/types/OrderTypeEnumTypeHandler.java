package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderTypeEnumTypeHandler extends BaseObjectTypeHandler<OrderTypeEnum>{

    public OrderTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
