package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.modal.OrderExtendConfig;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderExtendConfigTypeHandler extends BaseObjectTypeHandler<OrderExtendConfig> {

    public OrderExtendConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
