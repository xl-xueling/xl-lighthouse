package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.config.SeedAuthenticationToken;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/register")
    public ResultData<Integer> register(@Validated @RequestBody User userParam) {
        int id = userService.create(userParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.ERROR);
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
    public ResultData<Integer> updateById(@Validated @RequestBody UserUpdateParam userParam) {
        Integer userId = userParam.getId();
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = authentication.getUserId();
        if(currentUserId.intValue() != userId.intValue()){
            return ResultData.failed(ResultCode.ERROR);
        }
        int id = userService.update(userParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.ERROR);
        }
    }

}
