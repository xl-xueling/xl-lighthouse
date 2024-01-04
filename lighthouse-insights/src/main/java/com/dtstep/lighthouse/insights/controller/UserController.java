package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.config.SeedAuthenticationToken;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/user/register")
    public ResultData<Integer> register(@Validated @RequestBody User userParam) {
        int result = userService.create(userParam,SystemConstant.REGISTER_NEED_APPROVE);
        if(result > 0){
            return ResultData.success(result);
        }else if(result == -1){
            return ResultData.failed(ResultCode.registerUserNameExist);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

    @RequestMapping("/user/fetchUserInfo")
    public ResultData<User> fetchUserInfo(){
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        User user = userService.queryById(userId);
        return ResultData.success(user);
    }

    @RequestMapping("/user/updateById")
    public ResultData<Integer> updateById(@Validated @RequestBody UserUpdateParam updateParam) {
        Integer userId = updateParam.getId();
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = authentication.getUserId();
        if(currentUserId.intValue() != userId.intValue()){
            return ResultData.failed(ResultCode.systemError);
        }
        int id = userService.update(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }


    @RequestMapping("/user/changePassword")
    public ResultData<Integer> changePassword(@Validated @RequestBody ChangePasswordParam updateParam) {
        Integer userId = updateParam.getId();
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = authentication.getUserId();
        if(currentUserId.intValue() != userId.intValue()){
            return ResultData.failed(ResultCode.systemError);
        }
        String originPassword = updateParam.getOriginPassword();
        User dbUser = userService.queryById(userId);
        if(dbUser == null || !passwordEncoder.matches(originPassword,dbUser.getPassword())){
            return ResultData.failed(ResultCode.systemError);
        }
        int id = userService.changePassword(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @PostMapping("/user/list")
    public ResultData<ListData<User>> list(
            @Validated @RequestBody ListSearchObject<UserQueryParam> searchObject) {
        Pagination pagination = searchObject.getPagination();
        ListData<User> listData = userService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }


    @PostMapping("/user/termList")
    public ResultData<List<User>> termList(@RequestBody TextParam text){
        String search = text.getText();
        if(!StringUtil.isLetterNumOrUnderLine(search)){
            return ResultData.failed(ResultCode.paramValidateFailed);
        }
        List<User> users = userService.termQuery(search);
        return ResultData.success(users);
    }

}
