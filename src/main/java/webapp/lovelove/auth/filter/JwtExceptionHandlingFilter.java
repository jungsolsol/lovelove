package webapp.lovelove.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import webapp.lovelove.auth.exception.AppAuthExceptionCode;
import webapp.lovelove.auth.exception.AppAuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionHandlingFilter extends BasicAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtExceptionHandlingFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            chain.doFilter(request, response);
        } catch (AppAuthenticationException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AppAuthExceptionCode responseBody = e.getExceptionCode(); // 원본과 다름 안되면 수정 해야함
            objectMapper.writeValue(response.getWriter(),responseBody);
        } catch(Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String responseBody = e.getMessage(); //원본과 다름 안되면 수정해야함
            objectMapper.writeValue(response.getWriter(),responseBody);
        }
    }
}
