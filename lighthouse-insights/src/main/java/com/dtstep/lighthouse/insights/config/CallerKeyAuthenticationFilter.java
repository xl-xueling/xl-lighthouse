package com.dtstep.lighthouse.insights.config;

import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.wrapper.CallerDBWrapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CallerKeyAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String callerName = request.getHeader("Caller-Name");
        String callerKey = request.getHeader("Caller-Key");
        if(!StringUtil.isEmpty(callerName) && !StringUtil.isEmpty(callerKey)){
            Caller caller = CallerDBWrapper.queryByName(callerName);
            if(caller != null && caller.getSecretKey().equals(callerKey)){
                CallerKeyAuthenticationToken authentication = new CallerKeyAuthenticationToken(caller.getId(), callerKey);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
