package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.TrackParam;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class TrackController {

    @Autowired
    private GroupService groupService;

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "groupId")
    @PostMapping("/track")
    public ResultData<ListData<OrderVO>> track(@Validated @RequestBody TrackParam trackStatParam) throws Exception {
        Integer id = trackStatParam.getGroupId();
        System.out.println("track success,id:" + id);
        GroupVO groupVO = groupService.queryById(id);
        if(groupVO.getDebugMode() == SwitchStateEnum.OPEN){

        }else{

        }
        return ResultData.success();
    }
}
