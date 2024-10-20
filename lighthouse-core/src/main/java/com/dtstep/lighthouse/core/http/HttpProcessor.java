package com.dtstep.lighthouse.core.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
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

    public static ApiResultData stats(String requestBody) throws Exception {
        try{
            if(!LightHouse.isInit()){
                LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
            }
        }catch (Exception ex){
            logger.error("CallerStat initialization error!",ex);
        }
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
        try{
            if(!LightHouse.isInit()){
                LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
            }
        }catch (Exception ex){
            logger.error("CallerStat initialization error!",ex);
        }
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
        Object tokenObj = requestMap.get("token");
        Object secretKeyObj = requestMap.get("secretKey");
        Object paramsObj = requestMap.get("params");
        Object timestamp = requestMap.get("timestamp");
        Object repeat = requestMap.get("repeat");
        if(tokenObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("token"));
        }
        if(secretKeyObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("secretKey"));
        }
        if(paramsObj == null){
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

        if(timestamp == null){
            timestamp = System.currentTimeMillis();
        }
        if(repeat == null){
            repeat = 1;
        }
        if(!(paramsObj instanceof Map)){
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("params"));
        }
        try{
            LightHouse.stat(tokenObj.toString(),secretKeyObj.toString(),(Map<String,Object>)paramsObj,Integer.parseInt(repeat.toString()),Long.parseLong(timestamp.toString()));
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
        Object startTimeObj = requestMap.get("startTime");
        Object endTimeObj = requestMap.get("endTime");
        Object batchList = requestMap.get("batchList");
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
        if(batchList == null){
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
            List<StatValue> list;
            try{
                list = rpc.dataDurationQuery(Integer.parseInt(statIdObj.toString()),dimensValue,startTimeStamp,endTimeStamp);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),list);
        }else{
            if(!(batchList instanceof List)){
                ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
                return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchList"));
            }
            List<StatValue> list;
            try{
                list = rpc.dataQuery(Integer.parseInt(statIdObj.toString()),dimensValue,(List<Long>)batchList);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),list);
        }
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
        Object dimensValueListObj = requestMap.get("dimensValueList");
        Object startTimeObj = requestMap.get("startTime");
        Object endTimeObj = requestMap.get("endTime");
        Object batchList = requestMap.get("batchList");
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
        if(batchList == null){
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

            Map<String,List<StatValue>> data;
            try{
//                data = LightHouse.dataQueryWithDimensList(callerName,callerKey,Integer.parseInt(statIdObj.toString()),(List<String>)dimensValueListObj,startTimeStamp,endTimeStamp);
                data = rpc.dataDurationQueryWithDimensList(Integer.parseInt(statIdObj.toString()),(List<String>)dimensValueListObj,startTimeStamp,endTimeStamp);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
        }else{
            if(!(batchList instanceof List)){
                ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
                return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("batchList"));
            }
            Map<String,List<StatValue>> data;
            try{
//                data = LightHouse.dataQueryWithDimensList(callerName,callerKey,Integer.parseInt(statIdObj.toString()),(List<String>)dimensValueListObj,(List<Long>)batchList);
                data = rpc.dataQueryWithDimensList(Integer.parseInt(statIdObj.toString()),(List<String>)dimensValueListObj,(List<Long>)batchList);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
        }
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
        List<LimitValue> data;
        try{
//            data = LightHouse.limitQuery(callerName,callerKey,Integer.parseInt(statIdObj.toString()),Long.parseLong(batchTimeObj.toString()));
            data = rpc.limitQuery(Integer.parseInt(statIdObj.toString()),Long.parseLong(batchTimeObj.toString()));
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }
}
