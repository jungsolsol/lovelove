package webapp.lovelove.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import webapp.lovelove.auth.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        jwtService.removeRefreshToken(request,response);
        try {
            objectMapper.writeValue(response.getWriter(), "Refresh Token Remove Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
