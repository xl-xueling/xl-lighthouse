package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class RecordTypeEnumTypeHandler extends BaseObjectTypeHandler<RecordTypeEnum>{

    public RecordTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
