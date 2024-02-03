package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.RenderConfig;
import com.fasterxml.jackson.core.type.TypeReference;

public class RenderConfigTypeHandler extends BaseObjectTypeHandler<RenderConfig> {

    public RenderConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
