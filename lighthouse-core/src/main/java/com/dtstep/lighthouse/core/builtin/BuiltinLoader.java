package com.dtstep.lighthouse.core.builtin;
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
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class BuiltinLoader {

    private static final HashMap<Integer, GroupExtEntity> builtinGroups = new HashMap<>();

    private static final HashMap<Integer, StatExtEntity> builtinStats = new HashMap<>();

    private static final HashMap<Integer,List<StatExtEntity>> groupStatsMapping = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(BuiltinLoader.class);

    private static final ProjectEntity builtinProject;

    private static final String secretKey = "2l2ipBHOssTzsHsdErKDcarxjU6rKZwo";

    static  {
        builtinProject = new ProjectEntity();
        builtinProject.setName("Cluster Monitor");
        builtinProject.setId(1);
        try{
            loadBuiltInStats();
        }catch (Exception ex){
            logger.error("load builtin stat error!",ex);
        }
    }

    public static StatExtEntity getBuiltinStat(int statId){
        return builtinStats.getOrDefault(statId,null);
    }

    public static boolean isBuiltinStat(int statId){
        return builtinStats.containsKey(statId);
    }

    public static boolean isBuiltinGroup(int groupId){
        return builtinGroups.containsKey(groupId);
    }

    public static boolean isBuiltinGroup(String token){
        for(GroupExtEntity groupExtEntity : builtinGroups.values()){
            if(groupExtEntity.getToken().equals(token)){
                return true;
            }
        }
        return false;
    }

    public static boolean isBuiltinProject(int projectId){
        return projectId == builtinProject.getId();
    }

    public static ProjectEntity getBuiltinProject(){
        return builtinProject;
    }

    public static GroupExtEntity getBuiltinGroup(int monitorGroupId){
        return builtinGroups.getOrDefault(monitorGroupId,null);
    }

    public static GroupExtEntity getBuiltinGroup(String token){
        for(GroupExtEntity groupExtEntity : builtinGroups.values()){
            if(groupExtEntity.getToken().equals(token)){
                return groupExtEntity;
            }
        }
        return null;
    }

    public static List<GroupExtEntity> getAllGroups(){
        return new ArrayList<>(builtinGroups.values());
    }

    public static List<StatExtEntity> getBuiltinStatByGroupId(int groupId){
        return groupStatsMapping.get(groupId);
    }

    private static void loadBuiltInStats() throws Exception {
        InputStream inputStream = BuiltinLoader.class.getResourceAsStream("builtin-stats.xml");
        Document document = Jsoup.parse(inputStream,"utf-8","");
        Elements elements = document.getElementsByTag("monitor-group");
        if(elements == null || elements.isEmpty()){
            return;
        }
        for(Element element : elements){
            GroupExtEntity groupExtEntity = new GroupExtEntity();
            int groupId = Integer.parseInt(element.attr("id"));
            String token = element.attr("token");
            groupExtEntity.setId(groupId);
            groupExtEntity.setToken(token);
            groupExtEntity.setSecretKey(secretKey);
            groupExtEntity.setStatType(StatTypeEnum.RT.getType());
            groupExtEntity.setProjectId(builtinProject.getId());
            groupExtEntity.setBuiltIn(true);
            groupExtEntity.setMinTimeParam(new TimeParam(1, TimeUnit.MINUTES));
            groupExtEntity.setVerifyKey(Md5Util.getMD5(secretKey));
            Elements columnElements = element.getElementsByTag("column");
            List<MetaColumn> columnList = new ArrayList<>();
            for(Element columnElement : columnElements){
                String columnName = columnElement.attr("name");
                MetaColumn metaColumn = new MetaColumn();
                metaColumn.setColumnName(columnName);
                metaColumn.setColumnTypeEnum(ColumnTypeEnum.String);
                columnList.add(metaColumn);
            }
            Map<String,ColumnTypeEnum> columnHashMap = columnList.stream().collect(Collectors.toMap(MetaColumn::getColumnName, MetaColumn::getColumnTypeEnum));
            groupExtEntity.setRelatedColumns(columnHashMap);
            groupExtEntity.setColumnList(columnList);
            groupExtEntity.setState(GroupStateEnum.RUNNING.getState());
            builtinGroups.put(groupId, groupExtEntity);
            Elements statList = element.getElementsByTag("monitor-item");
            if(statList == null || statList.isEmpty()){
                continue;
            }
            String groupColumns = JsonUtil.toJSONString(columnList);
            Date date = new Date();
            for(Element statElement : statList){
                int statId = Integer.parseInt(statElement.attr("id"));
                String timeParam = statElement.attr("timeparam");
                Elements templateElements = statElement.getElementsByTag("template");
                Element templateElement = templateElements.get(0);
                String template = templateElement.html();
                StatEntity statEntity = new StatEntity();
                statEntity.setTemplate(template);
                statEntity.setTimeParam(timeParam);
                statEntity.setId(statId);
                statEntity.setProjectId(builtinProject.getId());
                statEntity.setGroupId(groupId);
                statEntity.setResMeta(-1);
                statEntity.setDataExpire(2592000);
                statEntity.setCreateTime(date);
                statEntity.setUpdateTime(date);
                statEntity.setGroupColumns(groupColumns);
                StatExtEntity statExtEntity = StatDBWrapper.combineExtInfo(statEntity,true);
                statExtEntity.setBuiltIn(true);
                statExtEntity.setToken(token);
                statExtEntity.setTitle(statExtEntity.getTemplateEntity().getTitle());
                statExtEntity.setStatStateEnum(StatStateEnum.RUNNING);
                List<StatExtEntity> groupStats = groupStatsMapping.getOrDefault(groupId, new ArrayList<>());
                groupStats.add(statExtEntity);
                groupStatsMapping.put(groupId,groupStats);
                builtinStats.put(statId, statExtEntity);
            }
        }
    }

}
