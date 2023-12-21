package com.dtstep.lighthouse.insights.config;

import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SystemEnvService systemEnvService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("request url is:" + request.getRequestURI());
        String authToken = request.getHeader(SystemConstant.AUTH_ACCESS_PARAM);
        if (Objects.isNull(authToken)){
            filterChain.doFilter(request,response);
            return;
        }
        String secretKey = systemEnvService.getParam(SystemConstant.PARAM_SIGN_KEY);
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
        if(jws == null){
            filterChain.doFilter(request,response);
            return;
        }
        Long expired = (Long)jws.getBody().get("expired");
        if(expired == null || expired <= System.currentTimeMillis()){
            filterChain.doFilter(request,response);
            return;
        }
        String seed = (String) jws.getBody().get("seed");
        SeedAuthenticationToken authentication = new SeedAuthenticationToken(seed);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }

}
