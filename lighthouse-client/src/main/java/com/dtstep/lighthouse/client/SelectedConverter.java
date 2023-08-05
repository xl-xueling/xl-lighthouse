package com.dtstep.lighthouse.client;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.components.SelectedElement;
import com.dtstep.lighthouse.common.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SelectedConverter {

    public static String convert(List<SelectedElement> elementList) throws Exception {
        if(elementList == null || elementList.size() == 0){
            return null;
        }
        HashMap<String, SelectedElement> elementMap = new HashMap<>();
        HashMap<String,List<SelectedElement>> elementLevelMap = new HashMap<>();
        for(SelectedElement selectedElement : elementList){
            String id = selectedElement.getId();
            String name = selectedElement.getName();
            if(StringUtil.isEmpty(id)){
                throw new Exception("element attribute[id] cannot be empty!");
            }
            if(StringUtil.isEmpty(name)){
                throw new Exception("element attribute[name] cannot be empty!");
            }
            if(elementMap.containsKey(id)){
                throw new Exception("duplicate element id:" + id);
            }
            elementMap.put(id, selectedElement);
        }

        for(SelectedElement selectedElement : elementList){
            String pid = selectedElement.getPid();
            if(!pid.equals(SysConst.TREE_ROOT_NODE_NAME) && !elementMap.containsKey(pid)){
                throw new Exception(String.format("The element[id:%s] does not exist!",pid));
            }
            List<SelectedElement> subList = elementLevelMap.computeIfAbsent(pid, k -> new ArrayList<>());
            subList.add(selectedElement);
        }
        List<SelectedElement> topList = elementLevelMap.get(SysConst.TREE_ROOT_NODE_NAME);
        if(topList == null || topList.size() == 0){
            throw new Exception(String.format("top-level element[pid:%s] does not exist!",SysConst.TREE_ROOT_NODE_NAME));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        loopElements(elementLevelMap, arrayNode, topList);
        return arrayNode.toString();
    }

    private static void combineSubElements(String pid,ObjectNode objectNode,HashMap<String,List<SelectedElement>> elementsLevelMap){
        List<SelectedElement> subList = elementsLevelMap.get(pid);
        if(subList != null && subList.size() != 0){
            ArrayNode arrayNode = objectNode.withArray("children");
            loopElements(elementsLevelMap, arrayNode, subList);
        }
    }

    private static void loopElements(HashMap<String, List<SelectedElement>> elementsLevelMap, ArrayNode arrayNode, List<SelectedElement> subList) {
        for(SelectedElement selectedElement :subList){
            ObjectNode subNode = arrayNode.addObject();
            subNode.put("name", selectedElement.getName());
            subNode.put("id", selectedElement.getId());
            combineSubElements(selectedElement.getId(),subNode,elementsLevelMap);
        }
    }
}
