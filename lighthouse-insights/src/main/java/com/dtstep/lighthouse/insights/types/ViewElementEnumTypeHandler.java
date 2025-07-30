package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.ViewElementType;
import com.fasterxml.jackson.core.type.TypeReference;

public class ViewElementEnumTypeHandler extends BaseObjectTypeHandler<ViewElementType>{

    public ViewElementEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
