package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.MetaTableStateEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class MetaTableStateEnumTypeHandler extends BaseObjectTypeHandler<MetaTableStateEnum>{

    public MetaTableStateEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
