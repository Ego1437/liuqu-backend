package com.omate.liuqu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// ... 其他导入 ...

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ... 注入或定义需要的依赖 ...

    @Value("${app.jwt.secretKey}")
    private String base64SecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(jwtToken);

                if (authentication != null) {
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 此处处理Token解析失败的情况
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        // 解析Token
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);

        // 验证有效性
        Claims claims = parsedToken.getBody();
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        if (username != null && expiration.after(new Date())) {
            // 从claims中提取角色或权限
            String rolesString = claims.get("roles", String.class);
            // 创建Authentication对象
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            // 根据claims获取用户权限
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }

    // ... 其他方法 ...
    public SecretKey getSecretKey() {
        // 解码Base64编码的密钥
        byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
        // 使用解码后的字节数组创建一个新的SecretKeySpec对象
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }
}
