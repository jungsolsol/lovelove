package webapp.lovelove.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import webapp.lovelove.auth.CustomOAuth2UserService;
import webapp.lovelove.member.domain.Role;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {


//    @Value("${spring.security.oauth2.client.registration.google.clientId}")  // (1)
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.google.clientSecret}") // (2)
//    private String clientSecret;

    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .authorizeRequests()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                )
                .oauth2Login()
//                .authorizationEndpoint().baseUri("/oauth2/authorization") // 소셜 로그인 Url
//                .authorizationRequestRepository(new HttpCookieOAuth2AuthorizationRequestRepository()) // 인증 요청을 쿠키에 저장하고 검색
//                .redirectionEndpoint().baseUri("/oauth2/callback/*") // 소셜 인증 후 Redirect Url
//                .and()\
//                .and()
//                .loginPage("/login")
//                .authorizationEndpoint()
//                .baseUri("/login/oauth2/code/google")
//                .redirectionEndpoint().baseUri("/login/oauth2/code/google")
//                .and()
//                ().baseUri("/")
//                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);


        return http.build();
    }

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        var clientRegistration = clientRegistration();    // (3-1)
//        return new InMemoryClientRegistrationRepository(clientRegistration);   // (3-2)
//    }
//    // (4)
//    private ClientRegistration clientRegistration() {
//        // (4-1)
//        return CommonOAuth2Provider
//                .GOOGLE
//                .getBuilder("google")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }



}

