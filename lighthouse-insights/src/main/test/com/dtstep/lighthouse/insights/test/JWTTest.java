package com.dtstep.lighthouse.insights.test;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JWTTest {

    @Test
    public void testJwt(){
//        String payload = "{\"user_id\":\"123\"}";
//        String key = "auc_token";
//        String s = Jwts.builder().setPayload(payload).signWith(SignatureAlgorithm.HS512,key).compact();
//        System.out.println("str is:" + s);
//        String tok = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImFkbWluIn0.Q66vfl_yGJAKrf2CDORarqHVVrhL9ZVzaqsYC5QQamZAZIg37B4WzvJhau30QK7AsNYobEpH3Jw7sGVQcDLNtA";
//        Jwt jwt = Jwts.parser().setSigningKey(key).parse(tok);
//        System.out.println("jwt is:" + JsonUtil.toJSONString(jwt));

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("username","admin");
        String token = Jwts.builder().setClaims(paramMap).signWith(SignatureAlgorithm.HS512, SystemConstant.SIGN_KEY).compact();
        System.out.println("token:" + token);

        Jws<Claims> jws = Jwts.parser().setSigningKey(SystemConstant.SIGN_KEY).parseClaimsJws(token);
        System.out.println("jwt is:" + jws.getBody().get("username7").toString());
    }
}
