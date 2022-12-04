package webapp.lovelove.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import webapp.lovelove.member.domain.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@Getter
public class PrincipalDetails implements OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        String sub = attributes.get("sub").toString();
        return sub;
    }

    public Long getMemberId() {return member.getId();}

    public static PrincipalDetails general(Member member) {
        return new PrincipalDetails(member,null);
    }


}
