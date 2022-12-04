package webapp.lovelove.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.auth.service.JwtService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static webapp.lovelove.auth.util.AppAuthNames.ACCESS_TOKEN;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler  extends SimpleUrlAuthenticationSuccessHandler  {
    private final JwtService jwtService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        String accessToken = jwtService.issueAccessToken(memberId);
        response.setHeader(ACCESS_TOKEN, accessToken);
        jwtService.issueRefreshToken(response,accessToken);
//        super.onAuthenticationSuccess(request, response, authentication);
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (response.isCommitted()) {
            this.logger.debug(LogMessage.format("Did not redirect to %s since response already committed."));
            return;
        }

        this.redirectStrategy.sendRedirect(request, response, "http://ec2-3-36-237-156.ap-northeast-2.compute.amazonaws.com:8080/");
    }
}
