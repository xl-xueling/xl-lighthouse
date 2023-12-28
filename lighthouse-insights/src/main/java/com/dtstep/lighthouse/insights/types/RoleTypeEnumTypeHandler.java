package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class RoleTypeEnumTypeHandler extends BaseObjectTypeHandler<RoleTypeEnum>{

    public RoleTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
