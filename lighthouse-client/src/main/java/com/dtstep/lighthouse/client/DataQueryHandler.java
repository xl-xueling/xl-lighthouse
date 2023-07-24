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
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.exception.LightTimeOutException;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrx;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrxHelper;
import com.dtstep.lighthouse.common.ice.DataQueryInterfacePrx;
import com.dtstep.lighthouse.common.ice.DataQueryInterfacePrxHelper;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

final class DataQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataQueryHandler.class);

    private final AuxInterfacePrx auxInterfacePrx;

    private final DataQueryInterfacePrx dataQueryInterfacePrx;

    public DataQueryHandler(Ice.Communicator ic){
        Ice.ObjectPrx auxBasePrx = ic.stringToProxy("identity_aux").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
        this.auxInterfacePrx = AuxInterfacePrxHelper.checkedCast(auxBasePrx);
        Ice.ObjectPrx dataQueryBasePrx = ic.stringToProxy("identity_dataquery").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
        this.dataQueryInterfacePrx = DataQueryInterfacePrxHelper.checkedCast(dataQueryBasePrx);
    }

    public List<StatValue> dataQueryWithDimens(int statId, String secretKey, String dimens, long startTime, long endTime) throws Exception{
        StatExtEntity statExtEntity = null;
        try{
            statExtEntity = AuxHandler.queryStatEntity(auxInterfacePrx,statId);
        }catch (Exception ex){
            logger.error("query stat info error,statId:{}",statId,ex);
        }
        if(statExtEntity == null){
            return null;
        }
        GroupVerifyEntity groupVerifyEntity = null;
        try{
            groupVerifyEntity = AuxHandler.queryStatGroup(auxInterfacePrx, statExtEntity.getToken());
        }catch (Exception ex){
            logger.error("query group info error,groupId:{}", statExtEntity.getGroupId(),ex);
        }
        if(groupVerifyEntity == null){
            return null;
        }
        if(!groupVerifyEntity.getVerifyKey().equals(Md5Util.getMD5(secretKey))){
            logger.error("secret key validation failed,statId:{},key:{}",statId,secretKey);
            return null;
        }
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Ice.AsyncResult asyncResult = dataQueryInterfacePrx.begin_dataQuery(statId,dimens,startTime,endTime,new NotifyThread(lock,condition));
        lock.lock();
        try {
            condition.await(20000, TimeUnit.MILLISECONDS);
            if(!asyncResult.isCompleted()){
                throw new LightTimeOutException();
            }
        }finally {
            lock.unlock();
        }
        String result = dataQueryInterfacePrx.end_dataQuery(asyncResult);
        List<StatValue> valueList = null;
        if(!StringUtil.isEmpty(result)){
            valueList = JsonUtil.toJavaObjectList(result,StatValue.class);
        }
        return valueList;
    }


    public StatValue dataQueryWithDimens(int statId, String secretKey, String dimens, long batchTime) throws Exception{
        StatExtEntity statExtEntity = null;
        try{
            statExtEntity = AuxHandler.queryStatEntity(auxInterfacePrx,statId);
        }catch (Exception ex){
            logger.error("query stat info error,statId:{}",statId,ex);
        }
        if(statExtEntity == null){
            return null;
        }
        GroupVerifyEntity groupVerifyEntity = null;
        try{
            groupVerifyEntity = AuxHandler.queryStatGroup(auxInterfacePrx, statExtEntity.getToken());
        }catch (Exception ex){
            logger.error("query group info error,groupId:{}", statExtEntity.getGroupId(),ex);
        }

        if(groupVerifyEntity == null){
            return null;
        }

        if(!groupVerifyEntity.getVerifyKey().equals(Md5Util.getMD5(secretKey))){
            logger.error("secret key validation failed,statId:{},key:{}",statId,secretKey);
            return null;
        }
        long dayStartTime = DateUtil.getDayStartTime(batchTime);
        if((batchTime - dayStartTime) % statExtEntity.getTimeUnit().toMillis(statExtEntity.getTimeParamInterval()) != 0){
            logger.error("the request parameter is wrong batch time,statId:{},timeParam:{},batchTime:{}",statId, statExtEntity.getTimeParam()
                    ,DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
            return null;
        }

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Ice.AsyncResult asyncResult = dataQueryInterfacePrx.begin_dataQueryWithDimensList(statId, Collections.singletonList(dimens),batchTime,new NotifyThread(lock,condition));
        lock.lock();
        try {
            condition.await(10000, TimeUnit.MILLISECONDS);
            if(!asyncResult.isCompleted()){
                throw new LightTimeOutException();
            }
        }finally {
            lock.unlock();
        }
        String result = dataQueryInterfacePrx.end_dataQueryWithDimensList(asyncResult);
        StatValue statValue = null;
        if(!StringUtil.isEmpty(result)){
            List<StatValue> valueList = JsonUtil.toJavaObjectList(result,StatValue.class);
            if(valueList != null && valueList.size() > 0){
                statValue = valueList.get(0);
            }
        }
        return statValue;
    }


    public Map<String,StatValue> dataQueryWithMultiDimens(int statId, String secretKey, List<String> dimensList, long batchTime) throws Exception{
        StatExtEntity statExtEntity = null;
        try{
            statExtEntity = AuxHandler.queryStatEntity(auxInterfacePrx,statId);
        }catch (Exception ex){
            logger.error("query stat info error,statId:{}",statId,ex);
        }
        if(statExtEntity == null){
            return null;
        }
        GroupVerifyEntity groupVerifyEntity = null;
        try{
            groupVerifyEntity = AuxHandler.queryStatGroup(auxInterfacePrx, statExtEntity.getToken());
        }catch (Exception ex){
            logger.error("query group info error,groupId:{}", statExtEntity.getGroupId(),ex);
        }

        if(groupVerifyEntity == null){
            return null;
        }
        if(!groupVerifyEntity.getVerifyKey().equals(Md5Util.getMD5(secretKey))){
            logger.error("secret key validation failed,statId:{},key:{}",statId,secretKey);
            return null;
        }

        long dayStartTime = DateUtil.getDayStartTime(batchTime);
        if((batchTime - dayStartTime) % statExtEntity.getTimeUnit().toMillis(statExtEntity.getTimeParamInterval()) != 0){
            logger.error("the request parameter is wrong batch time,statId:{},timeParam:{},batchTime:{}",statId, statExtEntity.getTimeParam()
                    ,DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
            return null;
        }

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Ice.AsyncResult asyncResult = dataQueryInterfacePrx.begin_dataQueryWithDimensList(statId,dimensList,batchTime,new NotifyThread(lock,condition));
        lock.lock();
        try {
            condition.await(10000, TimeUnit.MILLISECONDS);
            if(!asyncResult.isCompleted()){
                throw new LightTimeOutException();
            }
        }finally {
            lock.unlock();
        }
        String result = dataQueryInterfacePrx.end_dataQueryWithDimensList(asyncResult);
        Map<String,StatValue> valueMap = null;
        if(!StringUtil.isEmpty(result)){
            List<StatValue> valueList = JsonUtil.toJavaObjectList(result,StatValue.class);
            assert valueList != null;
            valueMap = valueList.stream().collect(Collectors.toMap(StatValue::getDimens, x -> x));
        }
        return valueMap;
    }

    public long getBatchTime(String timeParam,long timestamp) {
        int index = timeParam.indexOf("-");
        if(index == -1){
            throw new IllegalArgumentException("time param format error,timeParam:" + timeParam);
        }
        String intervalStr = timeParam.substring(0,index);
        String unit = timeParam.substring(index + 1);
        if(!StringUtil.isNumber(intervalStr)){
            throw new IllegalArgumentException("time param format error,timeParam:" + timeParam);
        }
        int interval = Integer.parseInt(intervalStr);
        TimeUnit timeUnit;
        switch (unit) {
            case "second":
                timeUnit = TimeUnit.SECONDS;
                break;
            case "minute":
                timeUnit = TimeUnit.MINUTES;
                break;
            case "hour":
                timeUnit = TimeUnit.HOURS;
                break;
            case "day":
                timeUnit = TimeUnit.DAYS;
                break;
            default:
                throw new IllegalArgumentException("time param format error,timeParam:" + timeParam);
        }
        return DateUtil.batchTime(interval,timeUnit,timestamp);
    }

    public List<Long> getBatchTime(String timeParam,long startTime,long endTime,int limitSize) {
        long tempTime = endTime;
        List<Long> dateList = new ArrayList<>();
        while (tempTime > startTime){
            if(limitSize != -1 && dateList.size() >= limitSize){
                break;
            }
            long t = getBatchTime(timeParam,tempTime);
            dateList.add(t);
            tempTime = (t - 1);
        }
        return dateList;
    }

    public List<String> loadDimens(String token,String secretKey,String dimensName,String lastDimensValue,int limitSize) throws Exception{
        GroupVerifyEntity groupVerifyEntity = null;
        try{
            groupVerifyEntity = AuxHandler.queryStatGroup(auxInterfacePrx,token);
        }catch (Exception ex){
            logger.error("query group info error,token:{}",token,ex);
        }

        if(groupVerifyEntity == null){
            return null;
        }

        if(!groupVerifyEntity.getVerifyKey().equals(Md5Util.getMD5(secretKey))){
            logger.error("secret key validation failed,token:{},key:{}",token,secretKey);
            return null;
        }
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Ice.AsyncResult asyncResult = dataQueryInterfacePrx.begin_queryDimens(token,dimensName,lastDimensValue,limitSize,new NotifyThread(lock,condition));
        lock.lock();
        try {
            condition.await(20000, TimeUnit.MILLISECONDS);
            if(!asyncResult.isCompleted()){
                throw new LightTimeOutException();
            }
        }finally {
            lock.unlock();
        }
        return dataQueryInterfacePrx.end_queryDimens(asyncResult);
    }

}
