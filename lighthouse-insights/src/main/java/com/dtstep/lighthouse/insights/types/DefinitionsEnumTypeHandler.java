package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.DefinitionsEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class DefinitionsEnumTypeHandler extends BaseObjectTypeHandler<DefinitionsEnum>{

    public DefinitionsEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
