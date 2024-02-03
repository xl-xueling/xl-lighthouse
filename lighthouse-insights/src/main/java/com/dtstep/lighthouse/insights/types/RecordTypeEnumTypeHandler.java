package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.enums.RecordTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

public class RecordTypeEnumTypeHandler extends BaseObjectTypeHandler<RecordTypeEnum>{

    public RecordTypeEnumTypeHandler(){
        super(new TypeReference<>() {});
    }
}
