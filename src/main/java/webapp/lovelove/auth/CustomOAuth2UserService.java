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
        OAuth2User auth2User = super.loadUser(userRequest);
//        OAuth2User auth2User= (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // (1)
        String provider = userRequest.getClientRegistration().getRegistrationId();

//        OAuth2UserInfo userInfo = UserInfoFactory.create(provider, oAuth2User.getAttributes())
//                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.INVALID_OAUTH2_PROVIDER));


//        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = auth2User.getAttribute("sub");
        String name = auth2User.getAttribute("name");
        String email = auth2User.getAttribute("email");
        String role = "ROLE_USER";
        Member memberEntity = memberRepository.findByEmail(email);




        if (memberEntity == null) {
            Member createMember = Member.builder().name(name).email(email)
                    .provider(provider).providerId(providerId)
                    .role(role).memberProfile(MemberProfile.builder().build()).build();

            memberRepository.save(createMember);

        }
        return new PrincipalDetails(memberEntity, auth2User.getAttributes());
    }
}
