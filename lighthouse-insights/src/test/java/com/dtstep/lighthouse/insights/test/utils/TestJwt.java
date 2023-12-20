package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.modal.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestJwt {

    @Test
    public void testJwt() throws Exception {
        String key = "0F73nraAcmN1cAIm7gnWbPH3h4nVAH7IgKKcM4SNmqloY1Xyf1BdwfK8kchNBetF";
        System.out.println("key:" + key);
        User user = new User();
        user.setUsername("xl-xueling");
        user.setPassword("xl-xueling");
        long now = System.currentTimeMillis();
        Map<String,Object> accessMap = new HashMap<>();
        accessMap.put("seed", UUID.randomUUID().toString());
        accessMap.put("expired", DateUtil.getMinuteAfter(now,10));
        String accessKey = Jwts.builder().setClaims(accessMap).signWith(SignatureAlgorithm.HS512, key).compact();
        Map<String,Object> refreshMap = new HashMap<>();
        refreshMap.put("seed", UUID.randomUUID().toString());
        refreshMap.put("id",user.getId());
        refreshMap.put("username",user.getUsername());
        refreshMap.put("password",user.getPassword());
        refreshMap.put("expired", DateUtil.getHourAfter(now,24));
        String refreshKey = Jwts.builder().setClaims(refreshMap).signWith(SignatureAlgorithm.HS512,key).compact();
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessKey",accessKey);
        tokenMap.put("refreshKey",refreshKey);
        System.out.println("accessKey is:" + accessKey);
        System.out.println("refreshKey is:" + refreshKey);
        Jws<Claims> jws1 = Jwts.parser().setSigningKey(key).parseClaimsJws(accessKey);
        System.out.println("jws1:" + JsonUtil.toJSONString(jws1));
        Jws<Claims> jws2 = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshKey);
        System.out.println("jws2:" + JsonUtil.toJSONString(jws2));
    }
}
