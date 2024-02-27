package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class GroupStateEnumTypeHandler extends BaseObjectTypeHandler<GroupStateEnum>{

    public GroupStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
