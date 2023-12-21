package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class DepartmentController {

    @RequestMapping("/department/all")
    public ResultData<ArrayNode> register() {
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("id","1");
        objectNode.put("name","sss");
        ArrayNode arrayNode = JsonUtil.createArrayNode();
        arrayNode.add(objectNode);
        ResultData<ArrayNode> array = ResultData.success(arrayNode);
        System.out.println("array:" + JsonUtil.toJSONString(array));
        return array;
    }
}
