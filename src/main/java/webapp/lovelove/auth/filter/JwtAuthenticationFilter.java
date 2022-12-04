package webapp.lovelove.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.auth.dto.LoginRequestDto;
import webapp.lovelove.auth.exception.AppAuthExceptionCode;
import webapp.lovelove.auth.exception.AppAuthenticationException;
import webapp.lovelove.auth.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.lovelove.auth.util.AppAuthNames.ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequest = requestBodyToLoginRequest(request);

        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId,password);
        return authenticationManager.authenticate(token);
    }

    private LoginRequestDto requestBodyToLoginRequest(HttpServletRequest request){
        try{
            return objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.DATA_DESERIALIZE_ERROR);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        Long id = principal.getMemberId();
        String accessToken = jwtService.issueAccessToken(id);

        response.setHeader(ACCESS_TOKEN, accessToken);

        System.out.println(accessToken);

        jwtService.issueRefreshToken(response,accessToken);

        objectMapper.writeValue(response.getWriter(), "Authentication Success!");
    }
}
