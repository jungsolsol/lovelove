package webapp.lovelove.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import webapp.lovelove.auth.CustomOAuth2UserService;
import webapp.lovelove.auth.filter.JwtAuthenticationFilter;
import webapp.lovelove.auth.filter.JwtAuthorizationFilter;
import webapp.lovelove.auth.filter.JwtExceptionHandlingFilter;
import webapp.lovelove.auth.handler.AuthenticationExceptionEntryPoint;
import webapp.lovelove.auth.handler.JwtAuthenticationFailureHandler;
import webapp.lovelove.auth.handler.JwtLogoutHandler;
import webapp.lovelove.auth.handler.OAuth2SuccessHandler;
import webapp.lovelove.auth.service.JwtService;
import webapp.lovelove.member.domain.Role;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtLogoutHandler logoutHandler;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final AuthenticationExceptionEntryPoint authenticationExceptionEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.headers()
                .frameOptions()
                .disable();

        http.formLogin()
                .disable();

        http.httpBasic()
                .disable();

        http.cors()
                .configurationSource(corsConfigurationSource());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new CustomJwtConfigurer())
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionEntryPoint);

        http.oauth2Login()
                .userInfoEndpoint()// OAuth2 로그인 성공 후 가져올 설정들
                .userService(customOAuth2UserService) // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
                .and()
                .successHandler(oAuth2SuccessHandler);

        http.logout()
                .permitAll()
                .logoutUrl("/api/v1/log-out")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request,response,authentication) -> {response.setStatus(HttpServletResponse.SC_OK);});

        return http.build();
    }

    public class CustomJwtConfigurer extends AbstractHttpConfigurer<CustomJwtConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtService);

            jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/log-in");
            jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);

            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberService, jwtService);
            JwtExceptionHandlingFilter jwtExceptionHandlingFilter = new JwtExceptionHandlingFilter(authenticationManager);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilter(jwtAuthorizationFilter)
                    .addFilterBefore(jwtExceptionHandlingFilter, JwtExceptionHandlingFilter.class);
        }

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "access-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token","access-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

