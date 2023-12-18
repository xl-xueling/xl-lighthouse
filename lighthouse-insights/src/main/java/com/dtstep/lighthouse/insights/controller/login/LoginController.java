package com.dtstep.lighthouse.insights.controller.login;

import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.entity.user.RequestUser;
import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/login")
    public String login(@RequestBody RequestUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(authenticationToken);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("username",user.getUsername());
        String token = Jwts.builder().setClaims(paramMap).signWith(SignatureAlgorithm.HS512,SystemConstant.SIGN_KEY).compact();
        return token;
    }

    @PostMapping("/index")
    public String index(){
        return "index";
    }
}
