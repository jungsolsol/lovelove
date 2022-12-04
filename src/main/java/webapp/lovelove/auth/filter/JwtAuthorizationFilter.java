package webapp.lovelove.auth.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.auth.exception.AppAuthExceptionCode;
import webapp.lovelove.auth.exception.AppAuthenticationException;
import webapp.lovelove.auth.service.JwtService;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtService jwtService){
        super(authenticationManager);
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtService.isJwtAccessTokenHeader(header)){
            chain.doFilter(request,response);
            return;
        }

        try{
            String accessToken = jwtService.authorizationHeaderToAccessToken(header);
            Long memberId = jwtService.verifyJwtTokenAndGetId(accessToken);

            if (!memberService.existsById(memberId))
                throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);

            PrincipalDetails principal = createPrincipalDetails(memberId);
            savePrincipalInSecurityContext(principal);

        } catch (TokenExpiredException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        } catch (JWTVerificationException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
        }

        chain.doFilter(request,response);
    }

    private PrincipalDetails createPrincipalDetails(Long memberId) {
        Member member = memberService.findById(memberId);
        return PrincipalDetails.general(member);
    }

    private void savePrincipalInSecurityContext(PrincipalDetails principal){
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
