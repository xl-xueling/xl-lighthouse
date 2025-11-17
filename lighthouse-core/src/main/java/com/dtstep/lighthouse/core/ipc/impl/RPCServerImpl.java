package com.dtstep.lighthouse.core.ipc.impl;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.*;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.exception.LimitExceedException;
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.util.*;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.ipc.DisruptorEventProducer;
import com.dtstep.lighthouse.core.storage.common.IndicatorGet;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.core.tools.TimePeriodUtil;
import com.dtstep.lighthouse.core.tools.TimePointUtil;
import com.dtstep.lighthouse.core.wrapper.*;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RPCServerImpl implements RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(RPCServerImpl.class);

    private static final DisruptorEventProducer eventProducer = new DisruptorEventProducer();

    @Override
    public int authVerification(String callerName, String callerKey, int resourceId, ResourceTypeEnum resourceTypeEnum) throws Exception {
        if(StringUtil.isEmpty(callerName)){
            throw new IllegalArgumentException("Missing required parameters[callerName]!");
        }
        if(StringUtil.isEmpty(callerKey)){
            throw new IllegalArgumentException("Missing required parameters[callerKey]!");
        }
        Caller caller = CallerDBWrapper.queryByName(callerName);
        if(caller == null){
            throw new PermissionException("API caller[" + callerName + "] does not exist!");
        }
        if(caller.getState() != CallerStateEnum.NORMAL){
            throw new PermissionException("API caller[" + callerName + "] status is unavailable!");
        }
        if(!callerKey.equals(caller.getSecretKey())){
            throw new PermissionException("API caller[" + callerName + "] secret-key verification failed!");
        }
        Role role = RoleDBWrapper.queryAccessRoleByResource(resourceId,resourceTypeEnum);
        if(role == null){
            throw new PermissionException("API caller[" + callerName + "] is not authorized to access this resource!");
        }
        boolean hasPermission = PermissionDBWrapper.hasPermission(caller.getId(), OwnerTypeEnum.CALLER,role.getId());
        if(!hasPermission){
            throw new PermissionException("API caller[" + callerName + "] is not authorized to access this resource!");
        }
        return caller.getId();
    }

    @Override
    public GroupVerifyEntity queryGroupInfo(String token) throws Exception {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        GroupVerifyEntity groupVerifyEntity = null;
        if(groupExtEntity != null){
            groupVerifyEntity = new GroupVerifyEntity();
            groupVerifyEntity.setVerifyKey(groupExtEntity.getVerifyKey());
            groupVerifyEntity.setRelationColumns(groupExtEntity.getRunningRelatedColumns());
            groupVerifyEntity.setState(groupExtEntity.getState());
            groupVerifyEntity.setGroupId(groupExtEntity.getId());
            groupVerifyEntity.setToken(groupExtEntity.getToken());
            groupVerifyEntity.setMinTimeParam(groupExtEntity.getMinTimeParam());
            if(logger.isTraceEnabled()){
                logger.trace("query groupInfo by token,token:{},groupInfo:{}",token, JsonUtil.toJSONString(groupVerifyEntity));
            }
        }
        return groupVerifyEntity;
    }

    @Override
    public StatVerifyEntity queryStatInfo(int id) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(id);
        StatVerifyEntity statVerifyEntity = null;
        if(statExtEntity != null){
            statVerifyEntity = new StatVerifyEntity();
            String token = statExtEntity.getToken();
            GroupVerifyEntity groupVerifyEntity = queryGroupInfo(token);
            statVerifyEntity.setStatId(id);
            statVerifyEntity.setVerifyKey(groupVerifyEntity.getVerifyKey());
        }
        return statVerifyEntity;
    }

    @Override
    public void process(byte[] bytes) throws Exception {
        if(bytes == null){
            return;
        }
        String data;
        if(SnappyUtil.isCompress(bytes)){
            data = SnappyUtil.uncompressByte(bytes);
        }else{
            data = new String(bytes, StandardCharsets.UTF_8);
        }
        if(StringUtil.isEmpty(data)){
            return;
        }
        if(logger.isDebugEnabled()){
            logger.debug("lighthouse debug,ice service receive message:{}",data);
        }
        for (String temp : Splitter.on(StatConst.SEPARATOR_LEVEL_0).split(data)) {
            if (!StringUtil.isEmpty(temp)) {
                int index = temp.lastIndexOf(StatConst.SEPARATOR_LEVEL_1);
                eventProducer.onData(temp.substring(0, index), Integer.parseInt(temp.substring(index + 1)));
            }
        }
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchList) throws Exception{
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return ResultStorageSelector.query(statExtEntity,dimensValue,batchList);
    }

    @Override
    public List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception{
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        List<Long> batchList;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(),startTime,endTime);
        }catch (Exception ex){
            logger.error("query batch list error,statId:{}",statId,ex);
            throw ex;
        }
        return ResultStorageSelector.query(statExtEntity,dimensValue,batchList);
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return ResultStorageSelector.queryWithDimensList(statExtEntity,dimensValueList,batchList);
    }

    @Override
    public Map<String, List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        List<Long> batchList;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(),startTime,endTime);
        }catch (Exception ex){
            logger.error("query batch list error,statId:{}",statId,ex);
            throw ex;
        }
        return ResultStorageSelector.queryWithDimensList(statExtEntity,dimensValueList,batchList);
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return LimitStorageSelector.query(statExtEntity,batchTime);
    }

    @Override
    public Object viewQuery(String callerName,String callerKey,String source,String config) throws Exception {
        if("remote".equals(source)){
            HttpRequestConfig httpRequestConfig = JsonUtil.toJavaObject(config,HttpRequestConfig.class);
            if(httpRequestConfig == null){
                throw new IllegalArgumentException();
            }
            if(StringUtil.isEmpty(httpRequestConfig.getUrl())){
                throw new IllegalArgumentException();
            }
            if(StringUtil.isEmpty(httpRequestConfig.getMethod())){
                throw new IllegalArgumentException();
            }
            Validate.notNull(httpRequestConfig);
            return OkHttpUtil.request(httpRequestConfig);
        }else if("stat".equals(source)){
            ViewStatBindsInfo bindsInfo = JsonUtil.toJavaObject(config, ViewStatBindsInfo.class);
            if(bindsInfo == null || bindsInfo.getBinds() == null){
                throw new IllegalArgumentException();
            }
            return queryStatData(callerName,callerKey,bindsInfo);
        }
        throw new IllegalArgumentException();
    }

    private List<IndicatorValue> queryStatData(String callerName,String callerKey,ViewStatBindsInfo viewStatBindsInfo) throws Exception {
        String mode = viewStatBindsInfo.getMode();
        if("period".equals(mode)){
            return queryStatDataByPeriod(callerName,callerKey,viewStatBindsInfo.getBinds(),viewStatBindsInfo.getDimens());
        }else{
            return queryStatDataByPoint(callerName,callerKey,viewStatBindsInfo.getBinds(),viewStatBindsInfo.getDimens());
        }
    }

    private List<IndicatorValue> queryStatDataByPeriod(String callerName,String callerKey,List<ViewStatBindItem> binds, List<LabelValue> globalDimensList) throws Exception {
        List<IndicatorGet> indicatorGets = new ArrayList<>();
        HashMap<String,String> globalDimensMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(globalDimensList)){
            for(LabelValue labelValue:globalDimensList){
                globalDimensMap.put(labelValue.getValue().toString(),labelValue.getLabel());
            }
        }
        Map<String, Map<String, String>> localDimensMap = new HashMap<>();
        HashMap<String,String> aliasMap = new HashMap<>();
        List<TimeUnit> timeUnits = new ArrayList<>();
        List<IndicatorValue> valueList = new ArrayList<>();
        for(ViewStatBindItem bindItem : binds){
            Integer statId = bindItem.getStatId();
            if(statId == null){
                continue;
            }
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                continue;
            }
            int indicator = bindItem.getIndicator();
            if(indicator > statExtEntity.getTemplateEntity().getStatStateList().size()){
                continue;
            }

            authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);

            List<LabelValue> allDimensList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(globalDimensList)){
                allDimensList.addAll(globalDimensList);
            }
            List<LabelValue> localDimensList = bindItem.getDimens();
            if (CollectionUtils.isNotEmpty(localDimensList)) {
                allDimensList.addAll(localDimensList);
                Map<String, String> statMapper = new HashMap<>();
                for (LabelValue labelValue : localDimensList) {
                    statMapper.put(String.valueOf(labelValue.getValue()), labelValue.getLabel());
                }
                localDimensMap.put(statId+"-"+indicator, statMapper);
            }
            TimeUnit timeUnit = statExtEntity.getTimeUnit();
            timeUnits.add(timeUnit);
            String alias = bindItem.getAlias();
            if(alias == null || "".equals(alias)){
                if(indicator == 0){
                    alias = "["+statId+"]-Default";
                }else{
                    alias = "["+statId+"]-Indicator"+indicator;
                }
            }
            aliasMap.put(statId+"-"+indicator,alias);
            String period = bindItem.getTimePeriod();
            long[] periodArr;
            try{
                periodArr = TimePeriodUtil.resolvePeriod(period);
            }catch (Exception ex){
                return valueList;
            }
            long startTime = periodArr[0];
            long endTime = periodArr[1];
            List<Long> batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(),startTime,endTime);
            int dimensLength = statExtEntity.getTemplateEntity().getDimensArray().length;
            if(dimensLength > 0){
                for(LabelValue labelValue : allDimensList){
                    String value = labelValue.getValue().toString();
                    if(value.split(";").length != dimensLength){
                        continue;
                    }
                    for(Long batchTime : batchList){
                        IndicatorGet indicatorGet = new IndicatorGet();
                        indicatorGet.setDimensValue(value);
                        indicatorGet.setStatId(statId);
                        indicatorGet.setBatchTime(batchTime);
                        indicatorGet.setIndicatorIndex(indicator);
                        if(indicatorGets.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
                            throw new LimitExceedException();
                        }
                        indicatorGets.add(indicatorGet);
                    }
                }
            }else{
                for(Long batchTime : batchList){
                    IndicatorGet indicatorGet = new IndicatorGet();
                    indicatorGet.setDimensValue(null);
                    indicatorGet.setStatId(statId);
                    indicatorGet.setBatchTime(batchTime);
                    indicatorGet.setIndicatorIndex(bindItem.getIndicator());
                    if(indicatorGets.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
                        throw new LimitExceedException();
                    }
                    indicatorGets.add(indicatorGet);
                }
            }
        }
        String timeFormat = resolveDateFormat(timeUnits);
        HashMap<IndicatorGet, Object> resultMap = ResultStorageSelector.query(indicatorGets);
        if(MapUtils.isNotEmpty(resultMap)){
            for(IndicatorGet indicatorGet : resultMap.keySet()){
                IndicatorValue indicatorValue = new IndicatorValue();
                int statId = indicatorGet.getStatId();
                int indicator = indicatorGet.getIndicatorIndex();
                indicatorValue.setAlias(aliasMap.get(statId+"-"+indicator));
                indicatorValue.setValue(resultMap.get(indicatorGet));
                String rawDimensValue = indicatorGet.getDimensValue();
                String mappedLabel = mapDimensValue(rawDimensValue, localDimensMap.get(statId+"-"+indicator), globalDimensMap);
                indicatorValue.setCategory(DateUtil.formatTimeStamp(indicatorGet.getBatchTime(),timeFormat));
                indicatorValue.setSeries(mappedLabel);
                valueList.add(indicatorValue);
            }
        }
        valueList.sort(
                Comparator.comparing(IndicatorValue::getCategory, Comparator.nullsFirst(String::compareTo))
                        .thenComparing(IndicatorValue::getAlias, Comparator.nullsFirst(String::compareTo)));
        return valueList;
    }

    private List<IndicatorValue> queryStatDataByPoint(String callerName,String callerKey,List<ViewStatBindItem> binds,List<LabelValue> globalDimensList) throws Exception {
        List<IndicatorGet> indicatorGets = new ArrayList<>();
        HashMap<String,String> globalDimensMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(globalDimensList)){
            for(LabelValue labelValue:globalDimensList){
                globalDimensMap.put(labelValue.getValue().toString(),labelValue.getLabel());
            }
        }
        Map<String, Map<String, String>> localDimensMap = new HashMap<>();
        HashMap<String,String> aliasMap = new HashMap<>();
        List<TimeUnit> timeUnits = new ArrayList<>();
        List<IndicatorValue> valueList = new ArrayList<>();
        for(ViewStatBindItem bindItem : binds){
            Integer statId = bindItem.getStatId();
            if(statId == null){
                continue;
            }
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                continue;
            }

            authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);

            int indicator = bindItem.getIndicator();
            if(indicator > statExtEntity.getTemplateEntity().getStatStateList().size()){
                continue;
            }
            TimeUnit timeUnit = statExtEntity.getTimeUnit();
            timeUnits.add(timeUnit);
            List<LabelValue> allDimensList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(globalDimensList)){
                allDimensList.addAll(globalDimensList);
            }
            List<LabelValue> localDimensList = bindItem.getDimens();
            if (CollectionUtils.isNotEmpty(localDimensList)) {
                allDimensList.addAll(localDimensList);
                Map<String, String> statMapper = new HashMap<>();
                for (LabelValue labelValue : localDimensList) {
                    statMapper.put(String.valueOf(labelValue.getValue()), labelValue.getLabel());
                }
                localDimensMap.put(statId+"-"+indicator, statMapper);
            }
            String alias = bindItem.getAlias();
            if(alias == null || "".equals(alias)){
                if(indicator == 0){
                    alias = "["+statId+"]-Default";
                }else{
                    alias = "["+statId+"]-Indicator"+indicator;
                }
            }
            aliasMap.put(statId+"-"+indicator,alias);
            String timePoint = bindItem.getTimePoint();
            long batchTime = TimePointUtil.getBatchTime(statExtEntity,System.currentTimeMillis(),timePoint);
            if(batchTime == StatConst.ILLEGAL_VAL){
                return valueList;
            }
            int dimensLength = statExtEntity.getTemplateEntity().getDimensArray().length;
            if(dimensLength > 0){
                for(LabelValue labelValue : allDimensList){
                    String value = labelValue.getValue().toString();
                    if(value.split(";").length != dimensLength){
                        continue;
                    }
                    IndicatorGet indicatorGet = new IndicatorGet();
                    indicatorGet.setDimensValue(value);
                    indicatorGet.setStatId(statId);
                    indicatorGet.setBatchTime(batchTime);
                    indicatorGet.setIndicatorIndex(indicator);
                    if(indicatorGets.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
                        throw new LimitExceedException();
                    }
                    indicatorGets.add(indicatorGet);
                }
            }else{
                IndicatorGet indicatorGet = new IndicatorGet();
                indicatorGet.setDimensValue(null);
                indicatorGet.setStatId(statId);
                indicatorGet.setBatchTime(batchTime);
                indicatorGet.setIndicatorIndex(bindItem.getIndicator());
                if(indicatorGets.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
                    throw new LimitExceedException();
                }
                indicatorGets.add(indicatorGet);
            }
        }
        String timeFormat = resolveDateFormat(timeUnits);
        HashMap<IndicatorGet, Object> resultMap = ResultStorageSelector.query(indicatorGets);
        if(MapUtils.isNotEmpty(resultMap)){
            for(IndicatorGet indicatorGet : resultMap.keySet()){
                IndicatorValue indicatorValue = new IndicatorValue();
                int statId = indicatorGet.getStatId();
                int indicator = indicatorGet.getIndicatorIndex();
                indicatorValue.setAlias(aliasMap.get(statId+"-"+indicator));
                indicatorValue.setValue(resultMap.get(indicatorGet));
                indicatorValue.setCategory(DateUtil.formatTimeStamp(indicatorGet.getBatchTime(),timeFormat));
                String rawDimensValue = indicatorGet.getDimensValue();
                String mappedLabel = mapDimensValue(rawDimensValue, localDimensMap.get(statId+"-"+indicator), globalDimensMap);
                indicatorValue.setSeries(mappedLabel);
                valueList.add(indicatorValue);
            }
        }
        valueList.sort(
                Comparator.comparing(IndicatorValue::getAlias, Comparator.nullsFirst(String::compareTo))
                        .thenComparing(IndicatorValue::getCategory, Comparator.nullsFirst(String::compareTo)));
        return valueList;
    }

    private static String mapDimensValue(String rawValue,
                                         Map<String, String> statDimensMap,
                                         Map<String, String> globalDimensMap) {
        if (rawValue == null) return null;
        if (statDimensMap != null && statDimensMap.containsKey(rawValue)) {
            return statDimensMap.get(rawValue);
        } else if (globalDimensMap != null && globalDimensMap.containsKey(rawValue)) {
            return globalDimensMap.get(rawValue);
        } else {
            return rawValue;
        }
    }

    public static String resolveDateFormat(Collection<TimeUnit> units) {
        if (units == null || units.isEmpty()) {
            return "MMdd HH:mm";
        }
        boolean hasDay = false;
        boolean hasHour = false;
        boolean hasMinute = false;
        for (TimeUnit unit : units) {
            switch (unit) {
                case DAYS:
                    hasDay = true;
                    break;
                case HOURS:
                    hasHour = true;
                    break;
                case MINUTES:
                    hasMinute = true;
                    break;
                default:
                    break;
            }
        }
        if (hasDay) {
            if (hasHour || hasMinute) {
                return "yyyyMMdd HH:mm";
            } else {
                return "yyyyMMdd";
            }
        } else if (hasHour) {
            if (hasMinute) {
                return "MMdd HH:mm";
            } else {
                return "MMdd HH:00";
            }
        } else if (hasMinute) {
            return "MMdd HH:mm";
        } else {
            return "MMdd HH:mm";
        }
    }
}
