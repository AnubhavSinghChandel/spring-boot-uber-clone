package com.project.uber.uberApp.security;

import com.project.uber.uberApp.entities.UserEntity;
import com.project.uber.uberApp.services.implementation.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            // 1. Get token from header
            // 2. Check if token exists in header
            // 3. extract userId from token
            // 4. If user exists pass it to spring security context holder
            // 5. pass to next filter chain filterchain.doFilter()

            final String header = request.getHeader("Authorization");
            if(header == null || !header.startsWith("Bearer")){
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.split("Bearer ")[1];

            Long userId = jwtService.getUserIdFromToken(token);

            if(userId!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserEntity user = userService.getUserById(userId);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // adding a few more details to the token before passing it to next filter
                // this step ensures to add details such as ip of the user and other details
                // that may be used to rate limit the requests such as to protect from DDoS attacks

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }
}
