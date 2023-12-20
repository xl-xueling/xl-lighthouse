package com.dtstep.lighthouse.insights.controller.login;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.entity.user.RequestUser;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
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

    @RequestMapping("/login")
    public ResultData<Map<String,String>> login(@RequestBody RequestUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(authenticationToken);
        long now = System.currentTimeMillis();
        Map<String,Object> accessMap = new HashMap<>();
        accessMap.put("username",user.getUsername());
        String accessKey = Jwts.builder().setClaims(accessMap).signWith(SignatureAlgorithm.HS512,SystemConstant.SIGN_KEY).compact();
        Map<String,Object> refreshMap = new HashMap<>();
        refreshMap.put("username",user.getUsername());
        refreshMap.put("password",user.getPassword());
        refreshMap.put("expired", DateUtil.getHourAfter(now,24));
        String refreshKey = Jwts.builder().setClaims(refreshMap).signWith(SignatureAlgorithm.HS512,SystemConstant.SIGN_KEY).compact();
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessKey",accessKey);
        tokenMap.put("refreshKey",refreshKey);
        System.out.println("accessKey is:" + accessKey);
        System.out.println("refreshKey is:" + refreshKey);
        return ResultData.success(tokenMap);
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
