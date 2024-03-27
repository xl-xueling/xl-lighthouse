package com.dtstep.lighthouse.insights.service.impl;
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
import com.clearspring.analytics.util.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.LimitTypeEnum;
import com.dtstep.lighthouse.common.modal.LimitDataObject;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.storage.limit.impl.RedisLimitStorageEngine;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Override
    public List<String> dimensArrangement(StatExtEntity statExtEntity, LinkedHashMap<String, String[]> dimensParams) throws Exception {
        String[] dimensArray = statExtEntity.getTemplateEntity().getDimensArray();
        if(ArrayUtils.isEmpty(dimensArray)){
            return null;
        }
        if(MapUtils.isEmpty(dimensParams)){
            return null;
        }
        List<String> destSortedList = Arrays.asList(dimensArray);
        List<String> originSortedList = new ArrayList<>();
        for(String dimens : dimensParams.keySet()){
            if(!dimens.contains(";")){
                originSortedList.add(dimens);
            }else{
                String [] arr = dimens.split(";");
                originSortedList.addAll(Arrays.asList(arr));
            }
        }
        String[][] valuesArray = dimensParams.values().toArray(new String[0][0]);
        List<String> unSortedList = arrangement(valuesArray);
        return unSortedList.stream().map(z -> {
            String[] arr = z.split(";");
            DimensEntity[] entity = new DimensEntity[arr.length];
            for(int i=0;i<arr.length;i++){
                DimensEntity pair = new DimensEntity(originSortedList.get(i), arr[i]);
                entity[i] = pair;
            }
            List<DimensEntity> sortedList = Arrays.stream(entity).sorted(new CustomComparator(destSortedList)).collect(Collectors.toList());
            return sortedList.stream()
                    .map(DimensEntity::getValue)
                    .collect(Collectors.joining(";"));
        }).collect(Collectors.toList());
    }

    private static class DimensEntity {

        private final String dimens;

        private final String value;

        public DimensEntity(String dimens, String value){
            this.dimens = dimens;
            this.value = value;
        }
        public String getDimens() {
            return dimens;
        }

        public String getValue() {
            return value;
        }
    }

    private static class CustomComparator implements Comparator<DimensEntity> {

        private final List<String> dimensSortList;

        public CustomComparator(List<String> dimensSortList){
            this.dimensSortList = dimensSortList;
        }

        @Override
        public int compare(DimensEntity o1, DimensEntity o2) {
            return dimensSortList.indexOf(o1.getDimens()) - dimensSortList.indexOf(o2.getDimens());
        }
    }

    public List<String> arrangement(String[]... datas) {
        List<String> result = new ArrayList<>();
        beArrangement(result, "", datas, 0);
        return result;
    }

    private void beArrangement(List<String> result, String current, String[][] lists, int index) {
        if (index == lists.length) {
            result.add(current);
            return;
        }
        for (String item : lists[index]) {
            beArrangement(result, current + (current.isEmpty() ? "" : ";") + item, lists, index + 1);
        }
    }

    @Override
    public ServiceResult<List<StatDataObject>> dataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception {
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(), startTimeStamp, endTimeStamp);
        }catch (Exception ex){
            logger.error("query batch time list error!",ex);
        }
        Validate.notNull(batchList);
        List<String> eliminateDimensList;
        HashMap<String,String> dimensValueMapping = new HashMap<>();
        if(CollectionUtils.isNotEmpty(dimensList)){
            eliminateDimensList = new ArrayList<>();
            for(String value : dimensList){
                StringBuilder sbr1 = new StringBuilder();
                StringBuilder sbr2 = new StringBuilder();
                String [] dimensArr = value.split(";");
                for(int i=0;i<dimensArr.length;i++){
                    String dimens = dimensArr[i];
                    String [] mapArr = dimens.split(",");
                    if(i != 0){
                        sbr1.append(";");
                        sbr2.append(";");
                    }
                    if(mapArr.length == 2){
                        sbr1.append(mapArr[0]);
                        sbr2.append(mapArr[1]);
                    }else{
                        sbr1.append(dimens);
                        sbr2.append(dimens);
                    }
                }
                eliminateDimensList.add(sbr1.toString());
                dimensValueMapping.put(sbr1.toString(),sbr2.toString());
            }
        }else{
            eliminateDimensList = dimensList;
        }
        if(eliminateDimensList != null && eliminateDimensList.size() * batchList.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
            return ServiceResult.result(ResultCode.dataQueryLimitExceed);
        }
        Map<String,List<StatValue>> valuesMap = ResultStorageSelector.queryWithDimensList(statExtEntity,eliminateDimensList,batchList);
        List<StatDataObject> dataObjects = new ArrayList<>();
        if(MapUtils.isNotEmpty(valuesMap)){
            for(String dimensValue : valuesMap.keySet()){
                StatDataObject dataObject = new StatDataObject();
                List<StatValue> valueList = valuesMap.get(dimensValue);
                dataObject.setDimensValue(dimensValue);
                dataObject.setDisplayDimensValue(dimensValueMapping.get(dimensValue));
                dataObject.setValuesList(valueList);
                dataObject.setStatId(statExtEntity.getId());
                dataObjects.add(dataObject);
            }
        }
        return ServiceResult.success(dataObjects);
    }

    @Override
    public List<LimitDataObject> limitQuery(StatExtEntity statExtEntity, List<Long> batchTimeList) throws Exception {
        List<LimitDataObject> resultList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(batchTimeList)){
            return resultList;
        }
        for(Long batchTime : batchTimeList){
            List<LimitValue> valueList = LimitStorageSelector.query(statExtEntity,batchTime);
            LimitDataObject dataObject = new LimitDataObject();
            dataObject.setValues(valueList);
            dataObject.setBatchTime(batchTime);
            dataObject.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
            resultList.add(dataObject);
        }
        return resultList;
    }

    @Override
    public ServiceResult<List<StatDataObject>> testDataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception{
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(), startTimeStamp, endTimeStamp);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Validate.notNull(batchList);
        List<StatDataObject> objectList = new ArrayList<>();
        List<String> eliminateDimensList;
        HashMap<String,String> dimensValueMapping = new HashMap<>();
        if(CollectionUtils.isNotEmpty(dimensList)){
            eliminateDimensList = new ArrayList<>();
            for(String value : dimensList){
                StringBuilder sbr1 = new StringBuilder();
                StringBuilder sbr2 = new StringBuilder();
                String [] dimensArr = value.split(";");
                for(int i=0;i<dimensArr.length;i++){
                    String dimens = dimensArr[i];
                    String [] mapArr = dimens.split(",");
                    if(i != 0){
                        sbr1.append(";");
                        sbr2.append(";");
                    }
                    if(mapArr.length == 2){
                        sbr1.append(mapArr[0]);
                        sbr2.append(mapArr[1]);
                    }else{
                        sbr1.append(dimens);
                        sbr2.append(dimens);
                    }
                }
                eliminateDimensList.add(sbr1.toString());
                dimensValueMapping.put(sbr1.toString(),sbr2.toString());
            }
        }else{
            eliminateDimensList = dimensList;
        }
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        if(eliminateDimensList == null){
            List<StatValue> statValues = new ArrayList<>();
            for(long batchTime:batchList){
                StatValue statValue = new StatValue();
                statValue.setValue(5000 + ThreadLocalRandom.current().nextInt(1000));
                statValue.setBatchTime(batchTime);
                statValue.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
                List<Object> statesValue = new ArrayList<>();
                for(int i=0;i<statStates.size();i++){
                    statesValue.add(5000 + ThreadLocalRandom.current().nextInt(1000));
                }
                statValue.setStatesValue(statesValue);
                statValues.add(statValue);
            }
            StatDataObject statDataObject = new StatDataObject();
            statDataObject.setStatId(statExtEntity.getId());
            statDataObject.setValuesList(statValues);
            objectList.add(statDataObject);
        }else{
            if(eliminateDimensList.size() * batchList.size() > StatConst.QUERY_RESULT_LIMIT_SIZE){
                return ServiceResult.result(ResultCode.dataQueryLimitExceed);
            }
            for(String dimens:eliminateDimensList){
                List<StatValue> statValues = new ArrayList<>();
                for(long batchTime:batchList){
                    StatValue statValue = new StatValue();
                    statValue.setValue(5000 + ThreadLocalRandom.current().nextInt(1000));
                    statValue.setBatchTime(batchTime);
                    statValue.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
                    List<Object> statesValue = new ArrayList<>();
                    for(int i=0;i<statStates.size();i++){
                        statesValue.add(5000 + ThreadLocalRandom.current().nextInt(1000));
                    }
                    statValue.setStatesValue(statesValue);
                    statValues.add(statValue);
                }
                StatDataObject statDataObject = new StatDataObject();
                statDataObject.setStatId(statExtEntity.getId());
                statDataObject.setDimensValue(dimens);
                statDataObject.setDisplayDimensValue(dimensValueMapping.get(dimens));
                statDataObject.setValuesList(statValues);
                objectList.add(statDataObject);
            }
        }
        return ServiceResult.success(objectList);
    }

    @Override
    public List<LimitDataObject> testLimitQuery(StatExtEntity statExtEntity, List<Long> batchTimeList) throws Exception {
        List<LimitDataObject> resultList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(batchTimeList)){
            return resultList;
        }
        for(Long batchTime : batchTimeList){
            List<LimitValue> valueList = new ArrayList<>();
            for(int i=0;i<10;i++){
                LimitValue limitValue = new LimitValue("test_" + i,ThreadLocalRandom.current().nextInt(1000));
                valueList.add(limitValue);
            }
            if(statExtEntity.getTemplateEntity().getLimitTypeEnum() == LimitTypeEnum.TOP){
                valueList.sort(RedisLimitStorageEngine.descComparator);
            }else{
                valueList.sort(RedisLimitStorageEngine.ascComparator);
            }
            LimitDataObject dataObject = new LimitDataObject();
            dataObject.setValues(valueList);
            dataObject.setBatchTime(batchTime);
            dataObject.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
            resultList.add(dataObject);
        }
        return resultList;
    }
}
