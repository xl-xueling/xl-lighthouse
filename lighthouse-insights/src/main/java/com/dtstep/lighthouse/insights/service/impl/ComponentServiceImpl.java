package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.service.ComponentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Override
    public ResultCode verify(String configuration) {
        JsonNode jsonNode;
        try{
            jsonNode = JsonUtil.readTree(configuration);
        }catch (Exception ex){
            return ResultCode.componentVerifyFormatError;
        }
        return validate(configuration);
    }

    private static final int MAX_DEPTH = 3;

    public static ResultCode validate(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode arrayNode = mapper.readTree(jsonData);
            return validateArray(arrayNode, 1);
        } catch (Exception e) {
            return ResultCode.componentVerifyFormatError;
        }
    }

    private static ResultCode validateArray(JsonNode arrayNode, int depth) {
        if (!arrayNode.isArray()) {
            return ResultCode.componentVerifyNonArrayStructure;
        }
        if (depth > MAX_DEPTH) {
            return ResultCode.componentVerifyLevelLimit;
        }

        for (JsonNode objectNode : arrayNode) {
            if (!objectNode.isObject()) {
                return ResultCode.componentVerifyFormatError;
            }
            ResultCode resultCode = validateObject(objectNode, depth + 1);
            if (resultCode != ResultCode.success) {
                return resultCode;
            }
        }

        return ResultCode.success;
    }

    private static ResultCode validateObject(JsonNode objectNode, int depth) {
        Iterator<String> iterator = objectNode.fieldNames();
        while (iterator.hasNext()){
            String fieldName = iterator.next();
            if (!fieldName.equals("label") && !fieldName.equals("value") && !fieldName.equals("children")) {
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyInvalidParams,new String[]{fieldName});
            }
            JsonNode fieldValue = objectNode.get(fieldName);
            if (fieldName.equals("children")) {
                ResultCode resultCode = validateArray(fieldValue, depth + 1);
                if (resultCode != ResultCode.success) {
                    return resultCode;
                }
            } else {
                if(StringUtil.isEmpty(fieldValue.asText())){
                    return ResultCode.getExtendResultCode(ResultCode.componentVerifyNotEmpty,new String[]{fieldName});
                }
                ResultCode resultCode = validateObject(fieldValue, depth);
                if (resultCode != ResultCode.success) {
                    return resultCode;
                }
            }
        }
        return ResultCode.success;
    }

}
