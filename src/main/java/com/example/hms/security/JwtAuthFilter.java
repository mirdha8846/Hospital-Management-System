package com.example.hms.security;

import com.example.hms.entity.User;
import com.example.hms.repo.Userrepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final Userrepo userrepo;
    private final Authutil authutil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
   try {
       final String tokenHeader= request.getHeader("Authorization");
       if(tokenHeader==null||!tokenHeader.startsWith("Bearer")){
           filterChain.doFilter(request,response);
           return;
       }
       String token=tokenHeader.split("Bearer ")[1];
       String username= authutil.getUsernameFromToken(token);
       if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
           User user=userrepo.findByUsernam(username).orElseThrow();
           UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

       }
       filterChain.doFilter(request,response);





   }catch (Exception exception){
  handlerExceptionResolver.resolveException(request,response,null,exception);
   }
    }
}
