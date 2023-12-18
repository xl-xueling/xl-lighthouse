package com.dtstep.lighthouse.insights.controller.login;

import cn.hutool.jwt.JWT;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.security.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@ControllerAdvice
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String register() {
        System.out.println("execute register");
        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestBody RequestUser req) {
        System.out.println("execute login");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String token = JWT.create()
                .setPayload("username", req.getUsername())
                .setKey(SystemConstant.SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .sign();
        return token;
    }

    @PostMapping("/index")
    public String index(){
        return "index";
    }
}
