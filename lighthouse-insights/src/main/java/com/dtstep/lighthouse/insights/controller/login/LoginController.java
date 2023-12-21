package com.dtstep.lighthouse.insights.controller.login;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.entity.user.RequestUser;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import com.dtstep.lighthouse.insights.service.UserService;
import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@ControllerAdvice
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private SystemEnvService systemEnvService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public ResultData<Map<String,String>> login(@RequestBody RequestUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String secretKey = systemEnvService.getParam(SystemConstant.PARAM_SIGN_KEY);
        long now = System.currentTimeMillis();
        Map<String,Object> accessMap = new HashMap<>();
        accessMap.put("seed", UUID.randomUUID().toString());
        accessMap.put("expired", DateUtil.getMinuteAfter(now,100000));
        String accessKey = Jwts.builder().setClaims(accessMap).signWith(SignatureAlgorithm.HS512, secretKey).compact();
        Map<String,Object> refreshMap = new HashMap<>();
        refreshMap.put("seed", UUID.randomUUID().toString());
        refreshMap.put("username",user.getUsername());
        refreshMap.put("password",passwordEncoder.encode(user.getPassword()));
        refreshMap.put("expired", DateUtil.getHourAfter(now,24));
        String refreshKey = Jwts.builder().setClaims(refreshMap).signWith(SignatureAlgorithm.HS512,secretKey).compact();
        Map<String,String> tokenMap = new HashMap<>();
        System.out.println("accessKey:" + accessKey);
        System.out.println("refreshKey:" + refreshKey);
        tokenMap.put("accessKey",accessKey);
        tokenMap.put("refreshKey",refreshKey);
        return ResultData.success(tokenMap);
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
