package com.kk.blog.payloads;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtSecretKey {
    public static SecretKey generateKey() {
        // Generate a secure key for HS512 algorithm
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return key;
    }
}
