package com.dtstep.lighthouse.insights.test.components;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtil {

    @Test
    public void testComponent() throws Exception {
        TreeNode treeNode1 = new TreeNode("北京","1");
        TreeNode treeNode2 = new TreeNode("上海","2");
        List<TreeNode> nodeList = new ArrayList<>();
        nodeList.add(treeNode1);
        nodeList.add(treeNode2);
        System.out.println("data:" + JsonUtil.toJSONString(nodeList));
    }
}
