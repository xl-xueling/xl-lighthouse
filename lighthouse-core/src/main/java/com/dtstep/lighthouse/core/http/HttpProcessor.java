package com.dtstep.lighthouse.core.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HttpProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ApiResultData stats(String requestBody) throws Exception {
        List<Map<String, Object>> list = objectMapper.readValue(requestBody, new TypeReference<>() {});
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
        Map<String, Object> requestMap = objectMapper.readValue(requestBody,Map.class);
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

    public static ApiResultData dataQuery(String requestBody) throws Exception {
        Map<String, Object> requestMap = objectMapper.readValue(requestBody,Map.class);
        Object statIdObj = requestMap.get("statId");
        Object secretKeyObj = requestMap.get("secretKey");
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
        if(secretKeyObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("secretKey"));
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
            List<StatValue> list;
            try{
                list = LightHouse.dataQuery(Integer.parseInt(statIdObj.toString()),secretKeyObj.toString(),dimensValue,Long.parseLong(startTimeObj.toString()),Long.parseLong(endTimeObj.toString()));
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
                list = LightHouse.dataQuery(Integer.parseInt(statIdObj.toString()),secretKeyObj.toString(),dimensValue,(List<Long>)batchList);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),list);
        }
    }

    public static ApiResultData dataQueryWithDimensList(String requestBody) throws Exception {
        Map<String, Object> requestMap = objectMapper.readValue(requestBody,Map.class);
        Object statIdObj = requestMap.get("statId");
        Object secretKeyObj = requestMap.get("secretKey");
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
        if(secretKeyObj == null){
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(),apiResultCode.formatMessage("secretKey"));
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
            Map<String,List<StatValue>> data;
            try{
                data = LightHouse.dataQueryWithDimensList(Integer.parseInt(statIdObj.toString()),secretKeyObj.toString(),(List<String>)dimensValueListObj,Long.parseLong(startTimeObj.toString()),Long.parseLong(endTimeObj.toString()));
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
                data = LightHouse.dataQueryWithDimensList(Integer.parseInt(statIdObj.toString()),secretKeyObj.toString(),(List<String>)dimensValueListObj,(List<Long>)batchList);
            }catch (Exception ex){
                ApiResultCode apiResultCode = ApiResultCode.ProcessError;
                return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
        }
    }

    public static ApiResultData limitQuery(String requestBody) throws Exception {
        Map<String, Object> requestMap = objectMapper.readValue(requestBody, Map.class);
        Object statIdObj = requestMap.get("statId");
        Object secretKeyObj = requestMap.get("secretKey");
        Object batchTimeObj = requestMap.get("batchTime");
        if (statIdObj == null) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("statId"));
        }
        if (!StringUtil.isNumber(statIdObj.toString())) {
            ApiResultCode apiResultCode = ApiResultCode.IllegalParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("statId"));
        }
        if (secretKeyObj == null) {
            ApiResultCode apiResultCode = ApiResultCode.MissingParam;
            return new ApiResultData(apiResultCode.getCode(), apiResultCode.formatMessage("secretKey"));
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
            data = LightHouse.limitQuery(Integer.parseInt(statIdObj.toString()),secretKeyObj.toString(),Long.parseLong(batchTimeObj.toString()));
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage(),data);
    }
}
