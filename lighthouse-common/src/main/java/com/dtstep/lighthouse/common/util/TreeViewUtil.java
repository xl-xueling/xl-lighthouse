package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.tree.EChartTreeNode;
import com.dtstep.lighthouse.common.entity.tree.ZTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeViewUtil {

    public static List<EChartTreeNode> translate(List<ZTreeNode> nodeList) throws Exception {
        if(nodeList == null || nodeList.size()  == 0){
            return null;
        }
        Map<String,ZTreeNode> nodeMap = nodeList.stream()
                .collect(Collectors.toMap(ZTreeNode::getId,z -> z));
        return combineSubList(SysConst.TREE_ROOT_NODE_NAME, new ArrayList<>(),nodeMap);
    }

    private static List<EChartTreeNode> combineSubList(String pid,List<EChartTreeNode> eChartTreeNodes,Map<String,ZTreeNode> nodeMap){
        for(String id : nodeMap.keySet()){
            ZTreeNode node = nodeMap.get(id);
            String currentPid = node.getpId();
            String currentId = node.getId();
            if(currentPid.equals(pid)){
                EChartTreeNode eChartTreeNode = new EChartTreeNode();
                eChartTreeNode.setId(node.getId());
                eChartTreeNode.setName(node.getName());
                List<EChartTreeNode> children = combineSubList(currentId,new ArrayList<>(),nodeMap);
                eChartTreeNode.setChildren(children);
                eChartTreeNodes.add(eChartTreeNode);
            }
        }
        return eChartTreeNodes;
    }
}
