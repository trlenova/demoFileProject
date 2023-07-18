package com.ets.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ets.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
	private  JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    	String requestURI = request.getRequestURI();
    	
    	if(requestURI.equals("/auth/generateToken")) {
    		filterChain.doFilter(request, response);
    		return;
    	}
    	
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        jwt = authHeader.substring(7);
        try {
        	 userEmail = jwtService.extractUserName(jwt);
             if (StringUtils.isNotEmpty(userEmail)
                     && SecurityContextHolder.getContext().getAuthentication() == null) {
                 UserDetails userDetails =new User("develop_user","123456",Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
                 if (jwtService.isTokenValid(jwt, userDetails)) {
                     SecurityContext context = SecurityContextHolder.createEmptyContext();
                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                             userDetails, null, userDetails.getAuthorities());
                     authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                     context.setAuthentication(authToken);
                     SecurityContextHolder.setContext(context);
                 }
                 else {
                 	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 	return;
                 }
             }
             else {
             	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             	return;
             }
             filterChain.doFilter(request, response);
		} catch (Exception e) {
         	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
         	return;
		}
       
    }
}
