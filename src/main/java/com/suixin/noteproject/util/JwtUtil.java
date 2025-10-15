package com.suixin.noteproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suixin.noteproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION = 24 * 60 * 60 * 1000; // 24小时
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 将对象转为只包含空字段的Map
    public static Map<String, Object> toNonBlankClaims(Object obj) {
        Map<String, Object> map = objectMapper.convertValue(obj, Map.class);
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null
                        && !entry.getValue().toString().isBlank())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String generateToken(User user) {
        Map<String,Object> claims = toNonBlankClaims(user);
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET,SignatureAlgorithm.HS512)
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从 Token 解析 Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    // 通用提取方法
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 获取 userId
    public Long getUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // 获取 roles
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return extractClaim(token, claims -> (List<String>) claims.get("roles"));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}