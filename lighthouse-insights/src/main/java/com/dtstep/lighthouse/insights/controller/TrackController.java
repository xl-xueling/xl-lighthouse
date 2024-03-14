package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.DebugConfig;
import com.dtstep.lighthouse.common.modal.GroupExtendConfig;
import com.dtstep.lighthouse.common.modal.IDParam;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.TrackParam;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.vo.DebugModeSwitchVO;
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

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/enableDebugMode")
    public ResultData<DebugConfig> enableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        GroupVO groupVO = groupService.queryById(id);
        DebugConfig debugConfig;
        if(groupVO.getDebugMode() == SwitchStateEnum.OPEN){
            debugConfig = groupVO.getExtendConfig().getDebugConfig();
        }else{
            GroupExtendConfig extendConfig = groupVO.getExtendConfig();
            debugConfig = extendConfig.getDebugConfig();
            if(debugConfig == null){
                debugConfig = new DebugConfig();
                extendConfig.setDebugConfig(debugConfig);
            }
            long now = System.currentTimeMillis();
            debugConfig.setStartTime(now);
            debugConfig.setEndTime(DateUtil.getMinuteAfter(now, StatConst.DEBUG_MODEL_EXPIRE_MINUTES));
            groupVO.setDebugMode(SwitchStateEnum.OPEN);
            groupVO.setRefreshTime(DateUtil.timestampToLocalDateTime(now));
            groupService.update(groupVO);
        }
        return ResultData.success(debugConfig);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/disableDebugMode")
    public ResultData<Integer> disableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        System.out.println("track success,id:" + id);
        GroupVO groupVO = groupService.queryById(id);
        if(groupVO.getDebugMode() == SwitchStateEnum.OPEN){

        }else{

        }
        return ResultData.success();
    }
}
