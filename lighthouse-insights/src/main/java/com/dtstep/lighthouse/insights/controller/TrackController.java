package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.DebugConfig;
import com.dtstep.lighthouse.common.modal.GroupExtendConfig;
import com.dtstep.lighthouse.common.modal.IDParam;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.TrackParam;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.vo.TrackMessageVO;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@ControllerAdvice
public class TrackController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StatService statService;

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/enableDebugMode")
    public ResultData<DebugConfig> enableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        GroupVO groupVO = groupService.queryById(id);
        DebugConfig debugConfig;
        if(groupVO.getDebugMode() == SwitchStateEnum.OPEN){
            debugConfig = groupVO.getExtendConfig().getDebugConfig();
            return ResultData.result(ServiceResult.result(ResultCode.debugModeSwitchAlreadyTurnON,debugConfig));
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
            return ResultData.success(debugConfig);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/disableDebugMode")
    public ResultData<Integer> disableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        GroupVO groupVO = groupService.queryById(id);
        groupVO.setDebugMode(SwitchStateEnum.CLOSE);
        groupVO.setRefreshTime(LocalDateTime.now());
        groupService.update(groupVO);
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "groupId")
    @PostMapping("/track/messages")
    public ResultData<List<LinkedHashMap<String,Object>>> messages(@Validated @RequestBody TrackParam trackParam) throws Exception {
        Integer groupId = trackParam.getGroupId();
        Integer statId = trackParam.getStatId();
        String key = RedisConst.TRACK_PREFIX + "_" + groupId;
        List<String> list = RedisHandler.getInstance().lrange(key,0,1000);
        List<LinkedHashMap<String,Object>> resultList = new ArrayList<>();
        StatExtEntity statExtEntity = statService.queryById(statId);
        Validate.notNull(statExtEntity);
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        for(int i=0;i<list.size();i++){
            LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
            LightMessage message = JsonUtil.toJavaObject(list.get(i),LightMessage.class);
            assert message != null;
            long messageTime = message.getTime();
            System.out.println("message:" + JsonUtil.toJSONString(message));
            linkedHashMap.put("seq",i + 1);
            linkedHashMap.put("messageTime",messageTime);
            linkedHashMap.put("processTime",message.getSystemTime());
            long batchTime = DateUtil.batchTime(statExtEntity.getTimeParamInterval(),statExtEntity.getTimeUnit(),messageTime);
            linkedHashMap.put("batchTime",batchTime);
            linkedHashMap.put("repeat",message.getRepeat());
            List<StatState> statStates = templateEntity.getStatStateList();
            Map<String,Object> envMap = new HashMap<>(message.getParamMap());
            for(StatState statState : statStates){
                long result = FormulaCalculate.calculate(statState,envMap,batchTime);
                linkedHashMap.put(statState.getStateBody(),result);
            }
            resultList.add(linkedHashMap);
        }
        return ResultData.success(resultList);
    }

}
