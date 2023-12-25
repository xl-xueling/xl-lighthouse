package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.modal.GroupExtendConfig;
import com.fasterxml.jackson.core.type.TypeReference;

public class GroupConfigTypeHandler extends JsonObjectTypeHandler<GroupExtendConfig> {

    public GroupConfigTypeHandler()
    {
        super(new TypeReference<GroupExtendConfig>() {});
    }
}
