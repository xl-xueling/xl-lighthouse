package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestLoginService {

    @Autowired
    private SystemEnvService systemEnvService;

    @Test
    public void testLogin() throws Exception{

    }

    @Test
    public void testAuth() throws Exception {
        String secretKey = systemEnvService.getParam(SystemConstant.PARAM_SIGN_KEY);
        String accessKey = "eyJhbGciOiJIUzUxMiJ9.eyJleHBpcmVkIjoxNzAzMTI0MTkwMDU2LCJzZWVkIjoiM2NiODkwZmYtNGNjZi00NjkxLWEwZjYtMTAzMzZjMmI1NzNjIn0.efmFiiUQET9ONbYOKy7faa9WuFZdDpt-T15hlxm9xG4v2d5fGAecMP3F_bHtiEkJnfzS6UArOI7bRGpSEsc7TQ";
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessKey);
        System.out.println("jws is:" + JsonUtil.toJSONString(jws));
        Long expired = (Long)jws.getBody().get("expired");
        System.out.println("expired:" + expired);
    }
}
