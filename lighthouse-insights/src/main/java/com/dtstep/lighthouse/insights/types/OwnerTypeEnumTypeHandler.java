package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class OwnerTypeEnumTypeHandler extends BaseObjectTypeHandler<OwnerTypeEnum>{

    public OwnerTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
