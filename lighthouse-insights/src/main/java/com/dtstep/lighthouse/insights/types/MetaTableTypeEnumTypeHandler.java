package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.MetaTableStateEnum;
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class MetaTableTypeEnumTypeHandler extends BaseObjectTypeHandler<MetaTableTypeEnum>{

    public MetaTableTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
