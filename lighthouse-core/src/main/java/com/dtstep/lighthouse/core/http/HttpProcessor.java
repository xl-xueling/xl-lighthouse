package com.dtstep.lighthouse.core.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ApiResultData stat(String requestBody) throws Exception {
        Map<String, Object> requestMap = objectMapper.readValue(requestBody,Map.class);
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
            LightHouse.stat(tokenObj.toString(),secretKeyObj.toString(),(Map<String,Object>)paramsObj,(Integer)repeat,(Long)timestamp);
        }catch (Exception ex){
            ApiResultCode apiResultCode = ApiResultCode.ProcessError;
            return new ApiResultData(apiResultCode.getCode(),ex.getMessage());
        }
        return new ApiResultData(ApiResultCode.Success.getCode(), ApiResultCode.Success.getMessage());
    }
}
