package webapp.lovelove.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.have_Smoking;
import webapp.lovelove.member.repository.MemberRepository;

import java.util.Optional;
import java.util.function.Supplier;


@RequiredArgsConstructor
@Service
public class
CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보를 담고있음
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
        Member memberEntity = memberRepository.findByEmail(email);


        if (memberEntity == null) {
            Member createMember = Member.builder().name(name).email(email)
                    .provider(provider).providerId(providerId)
                    .role(role).memberProfile(MemberProfile.builder().build()).build();

            memberRepository.save(createMember);

        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
