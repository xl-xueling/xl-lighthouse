package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.dto.ChangePasswordParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/department/all")
    public ResultData<ArrayNode> all() {
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("id","1");
        objectNode.put("name","sss");
        ArrayNode arrayNode = JsonUtil.createArrayNode();
        arrayNode.add(objectNode);
        ResultData<ArrayNode> array = ResultData.success(arrayNode);
        System.out.println("array:" + JsonUtil.toJSONString(array));
        return array;
    }

    @RequestMapping("/department/all2")
    public ResultData<ArrayNode> all2() {
        return null;
    }

    @RequestMapping("/department/updateById")
    public ResultData<Integer> updateById(@RequestBody Department updateParam) {
        int id = departmentService.update(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.ERROR);
        }
    }

}
