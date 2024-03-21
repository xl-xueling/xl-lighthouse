package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.LimitingParam;
import com.fasterxml.jackson.core.type.TypeReference;

public class LimitingParamTypeHandler extends BaseObjectTypeHandler<LimitingParam> {

    public LimitingParamTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
