package webapp.lovelove.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class OauthController {

    public class OAuthController {

        @GetMapping("/loginInfo")
        public String oauthLoginInfo(Authentication authentication){
            //oAuth2User.toString() 예시 : Name: [2346930276], Granted Authorities: [[USER]], User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            //attributes.toString() 예시 : {id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}
            Map<String, Object> attributes = oAuth2User.getAttributes();
            return attributes.toString();
        }
    }
}
