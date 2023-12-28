package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderStateEnumTypeHandler extends BaseObjectTypeHandler<OrderStateEnum>{

    public OrderStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
