package com.dtstep.lighthouse.common.util;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
