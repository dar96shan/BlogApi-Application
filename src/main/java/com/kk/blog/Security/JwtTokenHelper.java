package com.kk.blog.Security;

import com.kk.blog.payloads.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {


    public static final long JWT_TOKEN_VALIDITY= 5 * 60 *60;

    private final SecretKey SECRETKEY = JwtSecretKey.generateKey();


    
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }

    public  <T> T getClaimFromToken(String token, Function<Claims,T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
      final  Date expirationDateFromToken = getExpirationDateFromToken(token);
        return expirationDateFromToken.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims= new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS512,SECRETKEY)
                .compact();
    }

    public Boolean validateToken(String token,UserDetails userDetails){
       final String usernameFromToken = getUsernameFromToken(token);
       return (usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
