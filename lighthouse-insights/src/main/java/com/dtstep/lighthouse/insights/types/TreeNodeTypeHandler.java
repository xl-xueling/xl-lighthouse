package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class TreeNodeTypeHandler extends BaseObjectTypeHandler<TreeNode> {

    public TreeNodeTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
