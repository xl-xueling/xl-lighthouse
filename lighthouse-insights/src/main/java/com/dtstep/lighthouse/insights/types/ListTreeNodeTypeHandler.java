package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ListTreeNodeTypeHandler extends BaseObjectTypeHandler<List<TreeNode>> {

    public ListTreeNodeTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
