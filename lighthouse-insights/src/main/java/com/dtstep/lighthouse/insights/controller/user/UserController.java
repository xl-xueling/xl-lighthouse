package com.dtstep.lighthouse.insights.controller.user;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.UserCreateParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ControllerAdvice
public class UserController {

    @RequestMapping("/register")
    public String register(@Validated @RequestBody UserCreateParam userCreateParam) {
        System.out.println("userCreateParam is:" + JsonUtil.toJSONString(userCreateParam));
        return "register";
    }

}
