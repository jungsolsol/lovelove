package webapp.lovelove.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import webapp.lovelove.auth.exception.AppAuthExceptionCode;
import webapp.lovelove.auth.exception.AppAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        if(exception instanceof BadCredentialsException)
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ID_OR_PASSWORD);

        if(exception instanceof AppAuthenticationException)
            throw exception;

        if(exception.getCause() instanceof AppAuthenticationException){
            throw (AppAuthenticationException) exception.getCause();
        }

        log.info("authentication undefined exception", exception);
        throw new AppAuthenticationException(AppAuthExceptionCode.UNDEFINED);
    }
}
