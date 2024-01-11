package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.ComponentDao;
import com.dtstep.lighthouse.insights.dto.ComponentDto;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ComponentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentDao componentDao;

    @Autowired
    private BaseService baseService;

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

    private static <T> List<T> convertToList(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    private static ResultCode validateObject(JsonNode objectNode, int depth) {
        List<String> list = null;
        if(objectNode.isObject()){
            Iterator<String> iterator = objectNode.fieldNames();
            list = convertToList(iterator);
            if(CollectionUtils.isEmpty(list)){
                return ResultCode.componentVerifyFormatError;
            }
            if(!list.contains("label")){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyMissingParams,new String[]{"label"});
            }
            if(!list.contains("value")){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyMissingParams,new String[]{"value"});
            }
            for (String fieldName : list){
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
                    }else if(fieldValue.isTextual()){
                        continue;
                    }
                    ResultCode resultCode = validateObject(fieldValue, depth);
                    if (resultCode != ResultCode.success) {
                        return resultCode;
                    }
                }
            }
        }
        return ResultCode.success;
    }


    @Override
    public ResultCode create(Component component) {
        ResultCode resultCode = validate(component.getConfiguration());
        if(resultCode != ResultCode.success){
            return resultCode;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        component.setCreateTime(localDateTime);
        component.setUpdateTime(localDateTime);
        Integer id = componentDao.insert(component);
        if(id > 0){
            return ResultCode.success;
        }else{
            return ResultCode.systemError;
        }
    }

    @Override
    public ListData<ComponentDto> queryList(ComponentQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<ComponentDto> listData;
        try{
            List<Component> components = componentDao.queryList(queryParam);
            List<ComponentDto> dtoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(components)){
                for(Component component : components){
                    ComponentDto componentDto = new ComponentDto(component);
                    dtoList.add(componentDto);
                }
            }
            listData = baseService.translateToListData(dtoList);
        }finally {
            PageHelper.clearPage();
        }
        return listData;
    }
}
