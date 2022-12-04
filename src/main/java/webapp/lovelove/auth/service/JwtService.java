package webapp.lovelove.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import webapp.lovelove.auth.exception.AppAuthExceptionCode;
import webapp.lovelove.auth.exception.AppAuthenticationException;
import webapp.lovelove.auth.util.AppAuthNames;
import webapp.lovelove.auth.util.CookieUtil;
import webapp.lovelove.auth.util.JwtClaims;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final Map<String,String> repository = new HashMap<>();

    private final JwtProvider jwtProvider;

    private ObjectMapper om = new ObjectMapper();

    public String issueAccessToken(long memberId) {return jwtProvider.createAccessToken(memberId);}

    public void issueRefreshToken(HttpServletResponse response, String accessToken) {
        String refreshToken = jwtProvider.createRefreshToken(accessToken);
        Cookie cookie = CookieUtil.createHttpOnlyCookie(AppAuthNames.REFRESH_TOKEN, refreshToken);
        response.addCookie(cookie);
        saveToken(refreshToken, accessToken);
    }

    public void refresh(HttpServletRequest request, HttpServletResponse response){
        String prevAccessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        verifyRefreshToken(refreshToken);
        verifyTokenPair(prevAccessToken, refreshToken);

        Long id = decodeClaim(prevAccessToken, JwtClaims.ID).asLong();
        String newAccessToken = jwtProvider.createAccessToken(id);
        saveToken(refreshToken, newAccessToken);
        response.setHeader(AppAuthNames.ACCESS_TOKEN, newAccessToken);
    }


    private String getAccessToken(HttpServletRequest request){
        String accessTokenHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_NOT_EXIST));
        return authorizationHeaderToAccessToken(accessTokenHeader);
    }

    private String getRefreshToken(HttpServletRequest request){
        return CookieUtil.getCookieValue(AppAuthNames.REFRESH_TOKEN, request.getCookies())
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_NOT_EXIST));
    }

    private void verifyRefreshToken(String refreshToken){
        try{
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(refreshToken);
        } catch( TokenExpiredException e ){
            repository.remove(refreshToken);
            throw new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    public String authorizationHeaderToAccessToken(String authorizationHeader){
        return authorizationHeader.replace("Bearer ", "");
    }

    private void verifyTokenPair(String accessToken, String refreshToken) {
        String savedAccessToken = Optional.ofNullable(repository.get(refreshToken))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN));

        if (!savedAccessToken.equals(accessToken))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
    }

    private void saveToken(String refreshToken, String accessToken) {repository.put(refreshToken, accessToken);}

    private Claim decodeClaim(String token, String claim){
        return JWT.decode(token)
                .getClaim(claim);
    }

    public void removeRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = AppAuthNames.REFRESH_TOKEN;
        CookieUtil.getCookieValue(cookieName, request.getCookies())
                .ifPresent(repository::remove);
        response.addCookie(CookieUtil.createExpiredCookie(cookieName));
    }

    public boolean isJwtAccessTokenHeader(String header){
        return StringUtils.hasText(header) && header.startsWith("Bearer");
    }

    public Long verifyJwtTokenAndGetId(String jwtToken){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(jwtToken)
                .getClaim("id")
                .asLong();
    }

}
