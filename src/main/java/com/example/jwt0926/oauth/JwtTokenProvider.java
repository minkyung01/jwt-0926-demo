package com.example.jwt0926.oauth;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
    @Value("${jwt.access-token.expire-length:10000}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length:10000}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key:secret-key}")
    private String secretKey;

    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        // ascii 문자로 제대로 인코딩하기 위해 StandardCharsets.UTF_8 활용
        String generatedString = new String(array, StandardCharsets.UTF_8);

        return createToken(generatedString, refreshTokenValidityInMilliseconds);
    }

    public String createToken(String payload, long expireLength) {
        // Jwts.claims()에 payload를 제목으로 담아 claims 생성
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        // setIssuedAt(), setExpiration(), setSubject()는 registered claim으로,
        // token에 대한 정보를 담기 위해 미리 정해진 claim >> 필수가 아닌 선택임
        return Jwts.builder() // Jwts를 활용해 JWT 생성
                .setClaims(claims) // 위에서 생성한 claims를 JWT에 설정
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact(); // 압축 & 서명
    }

    public boolean checkClaim(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token).getBody();

            return true;
        } catch (ExpiredJwtException e) { // 토큰이 만료된 경우의 예외 처리
            return false;
        } catch (JwtException e) { // 토큰이 변조된 경우의 예외 처리
            return false;
        }
    }

    public String getPayload(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
