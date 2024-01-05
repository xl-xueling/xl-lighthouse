package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.dto.LoginParam;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import com.dtstep.lighthouse.insights.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/user/login")
    public ResultData<Map<String,String>> login(@Validated @RequestBody LoginParam user) {
        User dbUser = userService.queryByUserName(user.getUsername());
        if(dbUser == null || !passwordEncoder.matches(user.getPassword(),dbUser.getPassword())){
            return ResultData.failed(ResultCode.loginCheckFailed);
        }
        if(dbUser.getState() == UserStateEnum.USER_PEND){
            return ResultData.failed(ResultCode.userPendApprove);
        }else if(dbUser.getState() != UserStateEnum.USR_NORMAL){
            return ResultData.failed(ResultCode.userStateUnAvailable);
        }
        String signKey = systemEnvService.getParam(SystemConstant.PARAM_SIGN_KEY);
        long now = System.currentTimeMillis();
        Map<String,Object> accessMap = new HashMap<>();
        accessMap.put("id",dbUser.getId());
        accessMap.put("seed", UUID.randomUUID().toString());
        accessMap.put("expired", DateUtil.getMinuteAfter(now,10));
        String accessKey = Jwts.builder().setClaims(accessMap).signWith(SignatureAlgorithm.HS512, signKey).compact();
        Map<String,Object> refreshMap = new HashMap<>();
        refreshMap.put("id",dbUser.getId());
        refreshMap.put("seed", UUID.randomUUID().toString());
        refreshMap.put("username",user.getUsername());
        refreshMap.put("password",dbUser.getPassword());
        refreshMap.put("expired", DateUtil.getHourAfter(now,72));
        String refreshKey = Jwts.builder().setClaims(refreshMap).signWith(SignatureAlgorithm.HS512,signKey).compact();
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessKey",accessKey);
        tokenMap.put("refreshKey",refreshKey);
        return ResultData.success(tokenMap);
    }


    @RequestMapping("/refreshKey")
    public ResultData<Map<String,String>> refreshKey(HttpServletRequest request) {
        String refreshKey = request.getHeader(SystemConstant.AUTH_REFRESH_PARAM);
        if(StringUtil.isEmpty(refreshKey)){
            return ResultData.failed(ResultCode.authRenewalFailed);
        }
        String signKey = systemEnvService.getParam(SystemConstant.PARAM_SIGN_KEY);
        Jws<Claims> jws;
        try{
            jws = Jwts.parser().setSigningKey(signKey).parseClaimsJws(refreshKey);
            if(jws == null){
                return ResultData.failed(ResultCode.authRenewalFailed);
            }
        }catch (Exception ex){
            return ResultData.failed(ResultCode.authRenewalFailed);
        }
        Long expired = (Long)jws.getBody().get("expired");
        if(expired == null || expired <= System.currentTimeMillis()){
            return ResultData.failed(ResultCode.authRenewalFailed);
        }
        String username = (String)jws.getBody().get("username");
        String password = (String)jws.getBody().get("password");
        User dbUser = userService.queryByUserName(username);
        if(dbUser == null || !password.equals(dbUser.getPassword())){
            return ResultData.failed(ResultCode.paramValidateFailed);
        }
        if(dbUser.getState() != UserStateEnum.USR_NORMAL){
            return ResultData.failed(ResultCode.userStateUnAvailable);
        }
        User updateParam = new User();
        updateParam.setId(dbUser.getId());
        updateParam.setLastTime(LocalDateTime.now());
        userService.update(updateParam);
        long now = System.currentTimeMillis();
        Map<String,Object> accessMap = new HashMap<>();
        accessMap.put("id",dbUser.getId());
        accessMap.put("seed", UUID.randomUUID().toString());
        accessMap.put("expired", DateUtil.getMinuteAfter(now,10));
        String accessKey = Jwts.builder().setClaims(accessMap).signWith(SignatureAlgorithm.HS512, signKey).compact();
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessKey",accessKey);
        return ResultData.success(tokenMap);
    }


    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
