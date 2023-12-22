package com.dtstep.lighthouse.insights.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.getClass() == SeedAuthenticationToken.class){
            return new SeedAuthenticationToken(((SeedAuthenticationToken) authentication).getUserId(),((SeedAuthenticationToken) authentication).getSeed());
        }else{
            String username = String.valueOf(authentication.getPrincipal());
            String password = String.valueOf(authentication.getCredentials());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(userDetails != null && passwordEncoder.matches(password,userDetails.getPassword())){
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            }
            throw new BadCredentialsException("Authentication Error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication) || SeedAuthenticationToken.class.equals(authentication);
    }
}
