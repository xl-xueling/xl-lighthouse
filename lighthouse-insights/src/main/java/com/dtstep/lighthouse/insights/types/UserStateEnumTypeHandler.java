package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class UserStateEnumTypeHandler extends BaseObjectTypeHandler<UserStateEnum>{

    public UserStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
