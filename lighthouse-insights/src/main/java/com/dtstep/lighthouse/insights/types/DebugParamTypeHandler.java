package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.DebugParam;
import com.dtstep.lighthouse.common.modal.LimitingParam;
import com.fasterxml.jackson.core.type.TypeReference;

public class DebugParamTypeHandler extends BaseObjectTypeHandler<DebugParam> {

    public DebugParamTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
