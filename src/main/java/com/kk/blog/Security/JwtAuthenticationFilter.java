package com.kk.blog.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //1 get Token

        String requestToken = request.getHeader("Authorization");
        System.out.println("requestToken : "+requestToken);

        String username = null;
        String token = null;

        if(requestToken != null && requestToken.startsWith("Bearer")){
            //token without Bearer
             token = requestToken.substring(7);
        try{
            username = this.jwtTokenHelper.getUsernameFromToken(token);
        }catch (IllegalArgumentException e){
            System.out.println("Unable to get Jwt Token");
        }catch (ExpiredJwtException e){
            System.out.println("Jwt Token has expired");
        }catch (MalformedJwtException e){
            System.out.println("Invalid Jwt");
        }

        }else{
            System.out.println("JWT token does not begin with Bearer");
        }

        //once we get the token , now validate
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if(this.jwtTokenHelper.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //All good need to Authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                System.out.println("Invalid JWT Token");
            }
        }else{
            System.out.println("Username is null or context is not null");
        }

        filterChain.doFilter(request,response);
    }
}
