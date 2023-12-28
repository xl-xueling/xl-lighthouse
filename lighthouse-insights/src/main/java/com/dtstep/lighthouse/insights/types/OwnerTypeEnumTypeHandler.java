package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class OwnerTypeEnumTypeHandler extends BaseObjectTypeHandler<OwnerTypeEnum>{

    public OwnerTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
