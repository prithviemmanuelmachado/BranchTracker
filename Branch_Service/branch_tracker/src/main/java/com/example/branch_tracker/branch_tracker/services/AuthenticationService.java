package com.example.branch_tracker.branch_tracker.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
public class AuthenticationService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationMs;

    public Map<String, String> validateUser(String token){
        Map<String, String> result = new HashMap<>();
        try {
            Jws<Claims> decoded = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            //check if token is expired
            Date exp = decoded.getBody().getExpiration();
            if(new Date().after(exp)){
                throw new Exception();
            }

            //decode token
            String input = decoded.getBody().getSubject();
            input = input.substring(1, input.length() - 1);
            String[] keyValuePairs = input.split(", ");
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split("=");
                result.put(keyValue[0], keyValue[1]);
            }
        } catch (Exception e) {
           result.put("error", "Invalid token. Please login.");
        }
        return result;
    }
}
