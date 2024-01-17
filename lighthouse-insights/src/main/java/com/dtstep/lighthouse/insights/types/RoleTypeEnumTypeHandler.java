package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class RoleTypeEnumTypeHandler extends BaseObjectTypeHandler<RoleTypeEnum>{

    public RoleTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
