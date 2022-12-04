package webapp.lovelove.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import webapp.lovelove.auth.util.JwtClaims;
import webapp.lovelove.auth.util.JwtTokenType;

import java.util.Date;

@Service

public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-time-millis.access-token}")
    private long accessExpirationTimeMillis;

    @Value("${jwt.expiration-time-millis.refresh-token}")
    private long refreshExpirationTimeMillis;

    public String createAccessToken(Long id){
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withExpiresAt(getExpiredDate(accessExpirationTimeMillis))
                .withClaim(JwtClaims.ID,id)
                .sign(Algorithm.HMAC512(secret));
    }

    public String createRefreshToken(String accessToken){
        return JWT.create()
                .withSubject(JwtTokenType.REFRESH.getSubject())
                .withExpiresAt(getExpiredDate(refreshExpirationTimeMillis))
                .sign(Algorithm.HMAC512(secret));
    }

    private Date getExpiredDate(long expirationTime) { return new Date(System.currentTimeMillis() + expirationTime);}
}
