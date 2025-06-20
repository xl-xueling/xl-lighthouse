package com.dtstep.lighthouse.core.http;
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
import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.CallerStat;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
import com.dtstep.lighthouse.core.tools.ObjectSize;
import com.dtstep.lighthouse.core.wrapper.CallerDBWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HttpProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final RPCServer rpc = new RPCServerImpl();

    static {
        try{
            if(!LightHouse.isInit()){
                LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
            }
        }catch (Exception ex){
            logger.error("CallerStat initialization error!",ex);
        }
    }

    public static ApiResultData stats(String requestBody) throws Exception {
        List<Map<String, Object>> list;
        try{
            list = objectMapper.readValue(requestBody, new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(CollectionUtils.isEmpty(list)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        for(Map<String,Object> requestMap : list){
            ApiResultData apiResultData = stat(requestMap);
            if(!apiResultData.getCode().equals(ApiResultCode.Success.getCode())){
                return apiResultData;
            }
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage());
    }

    public static ApiResultData stat(String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        return stat(requestMap);
    }

    public static ApiResultData stat(Map<String,Object> requestMap) throws Exception {
        String token = (String) requestMap.get("token");
        String secretKey = (String) requestMap.get("secretKey");
        Map<String, Object> params = (Map<String, Object>) requestMap.get("params");
        Long timestamp = (Long) requestMap.get("timestamp");
        Integer repeat = (Integer) requestMap.get("repeat");
        if(token == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("token"));
        }
        if(secretKey == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("secretKey"));
        }
        if(params == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("params"));
        }
        if(timestamp != null && !StringUtil.isNumber(timestamp.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("timestamp"));
        }
        if(repeat != null && !StringUtil.isNumber(repeat.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("repeat"));
        }
        if(repeat != null && Integer.parseInt(repeat.toString()) <= 0){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("repeat <= 0"));
        }

        if (timestamp == null) timestamp = System.currentTimeMillis();
        if (repeat == null) repeat = 1;

        try{
            LightHouse.stat(token,secretKey,params,Integer.parseInt(repeat.toString()),Long.parseLong(timestamp.toString()));
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage());
    }

    public static ApiResultData dataQuery(String callerName,String callerKey,String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        Object statIdObj = requestMap.get("statId");
        Object dimensValueObj = requestMap.get("dimensValue");
        List<?> batchList = (List<?>)requestMap.get("batchList");
        if(statIdObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if(!StringUtil.isNumber(statIdObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if (StringUtil.isEmpty(callerName)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerName"));
        }
        if (StringUtil.isEmpty(callerKey)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerKey"));
        }
        if(CollectionUtils.isEmpty(batchList) || !(batchList.get(0) instanceof Long)){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchList"));
        }

        String dimensValue = null;
        if(dimensValueObj != null){
            dimensValue = dimensValueObj.toString();
        }
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,Integer.parseInt(statIdObj.toString()), ResourceTypeEnum.Stat);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.AuthorizationError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey) + ObjectSize.getObjectSize(batchList);
        long outBytes = 0;
        List<StatValue> data;
        try{
            data = rpc.dataQuery(Integer.parseInt(statIdObj.toString()),dimensValue,(List<Long>)batchList);
            outBytes = ObjectSize.getObjectSize(data);
            CallerStat.stat(callerId,"dataQuery",0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataQuery",1,inBytes,outBytes);
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }

    public static ApiResultData dataDurationQuery(String callerName,String callerKey,String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        Object statIdObj = requestMap.get("statId");
        Object dimensValueObj = requestMap.get("dimensValue");
        Object startTimeObj = requestMap.get("startTime");
        Object endTimeObj = requestMap.get("endTime");
        if(statIdObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if(!StringUtil.isNumber(statIdObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if (StringUtil.isEmpty(callerName)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerName"));
        }
        if (StringUtil.isEmpty(callerKey)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerKey"));
        }
        String dimensValue = null;
        if(dimensValueObj != null){
            dimensValue = dimensValueObj.toString();
        }
        if(startTimeObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime"));
        }
        if(!StringUtil.isNumber(startTimeObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime"));
        }
        if(endTimeObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("endTime"));
        }
        if(!StringUtil.isNumber(endTimeObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("endTime"));
        }
        long startTimeStamp = Long.parseLong(startTimeObj.toString());
        long endTimeStamp = Long.parseLong(endTimeObj.toString());
        if(startTimeStamp >= endTimeStamp){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime >= endTime"));
        }
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,Integer.parseInt(statIdObj.toString()), ResourceTypeEnum.Stat);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.AuthorizationError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey) + ObjectSize.getObjectSize(dimensValue);
        long outBytes = 0;
        List<StatValue> data;
        try{
            data = rpc.dataDurationQuery(Integer.parseInt(statIdObj.toString()),dimensValue,startTimeStamp,endTimeStamp);
            outBytes = ObjectSize.getObjectSize(data);
            CallerStat.stat(callerId,"dataDurationQuery",0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataDurationQuery",1,inBytes,outBytes);
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }

    public static ApiResultData dataQueryWithDimensList(String callerName,String callerKey,String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        Object statIdObj = requestMap.get("statId");
        List<String> dimensValueListObj = (List<String>)requestMap.get("dimensValueList");
        List<?> batchList = (List<?>)requestMap.get("batchList");
        if(statIdObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if(!StringUtil.isNumber(statIdObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if (StringUtil.isEmpty(callerName)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerName"));
        }
        if (StringUtil.isEmpty(callerKey)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerKey"));
        }
        if(dimensValueListObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("dimensValueList"));
        }
        if(CollectionUtils.isEmpty(batchList) || !(batchList.get(0) instanceof Long)){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchList"));
        }
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,Integer.parseInt(statIdObj.toString()), ResourceTypeEnum.Stat);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.AuthorizationError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey) + ObjectSize.getObjectSize(dimensValueListObj) + ObjectSize.getObjectSize(batchList);
        long outBytes = 0;
        Map<String,List<StatValue>> data;
        try{
            data = rpc.dataQueryWithDimensList(Integer.parseInt(statIdObj.toString()),dimensValueListObj,(List<Long>)batchList);
            outBytes = ObjectSize.getObjectSize(data);
            CallerStat.stat(callerId,"dataQueryWithDimensList",0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataQueryWithDimensList",1,inBytes,outBytes);
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }

    public static ApiResultData dataDurationQueryWithDimensList(String callerName,String callerKey,String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        Object statIdObj = requestMap.get("statId");
        Object dimensValueListObj = requestMap.get("dimensValueList");
        Object startTimeObj = requestMap.get("startTime");
        Object endTimeObj = requestMap.get("endTime");
        if(statIdObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if(!StringUtil.isNumber(statIdObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("statId"));
        }
        if (StringUtil.isEmpty(callerName)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerName"));
        }
        if (StringUtil.isEmpty(callerKey)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerKey"));
        }
        if(dimensValueListObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("dimensValueList"));
        }
        if(!(dimensValueListObj instanceof List)){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("dimensValueList"));
        }
        if(startTimeObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime"));
        }
        if(!StringUtil.isNumber(startTimeObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime"));
        }
        if(endTimeObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("endTime"));
        }
        if(!StringUtil.isNumber(endTimeObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("endTime"));
        }

        long startTimeStamp = Long.parseLong(startTimeObj.toString());
        long endTimeStamp = Long.parseLong(endTimeObj.toString());
        if(startTimeStamp >= endTimeStamp){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("startTime >= endTime"));
        }
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,Integer.parseInt(statIdObj.toString()), ResourceTypeEnum.Stat);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.AuthorizationError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey) + ObjectSize.getObjectSize(dimensValueListObj);
        long outBytes = 0;
        Map<String,List<StatValue>> data;
        try{
            data = rpc.dataDurationQueryWithDimensList(Integer.parseInt(statIdObj.toString()),(List<String>)dimensValueListObj,startTimeStamp,endTimeStamp);
            outBytes = ObjectSize.getObjectSize(data);
            CallerStat.stat(callerId,"dataDurationQueryWithDimensList",0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataDurationQueryWithDimensList",1,inBytes,outBytes);
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }

    public static ApiResultData limitQuery(String callerName,String callerKey,String requestBody) throws Exception {
        Map<String, Object> requestMap;
        try{
            requestMap =  objectMapper.readValue(requestBody,new TypeReference<>() {});
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ParametersParseException;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        if(MapUtils.isEmpty(requestMap)){
            ApiResultCode apiResultCode = ApiResultCode.MissingParams;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.getMessage());
        }
        Object statIdObj = requestMap.get("statId");
        Object batchTimeObj = requestMap.get("batchTime");
        if (statIdObj == null) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("statId"));
        }
        if (!StringUtil.isNumber(statIdObj.toString())) {
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("statId"));
        }
        if (StringUtil.isEmpty(callerName)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerName"));
        }
        if (StringUtil.isEmpty(callerKey)) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("callerKey"));
        }
        if(batchTimeObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchTime"));
        }
        if(!StringUtil.isNumber(batchTimeObj.toString())){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchTime"));
        }
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,Integer.parseInt(statIdObj.toString()), ResourceTypeEnum.Stat);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.AuthorizationError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey) + ObjectSize.getObjectSize(batchTimeObj);
        long outBytes = 0;
        List<LimitValue> data;
        try{
            data = rpc.limitQuery(Integer.parseInt(statIdObj.toString()),Long.parseLong(batchTimeObj.toString()));
            outBytes = ObjectSize.getObjectSize(data);
            CallerStat.stat(callerId,"limitQuery",0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"limitQuery",1,inBytes,outBytes);
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }
}
