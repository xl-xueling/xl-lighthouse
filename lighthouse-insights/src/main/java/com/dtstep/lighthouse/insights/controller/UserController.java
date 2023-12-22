package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        System.out.println("name is:" + name);
        ResultData<User> resultData = new ResultData<>();
        User user = userService.queryByUserName(name);
        return ResultData.success(user);
    }

}
