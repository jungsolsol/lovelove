package webapp.lovelove.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.Role;
import webapp.lovelove.member.domain.dto.MemberCreateDto;

import java.io.Serializable;

    @Getter
    public class SessionMember implements Serializable {
        private String name;
        private String email;
        private String picture;

        private Role role;

        public SessionMember(Member member) {
            this.name = member.getName();
            this.email = member.getEmail();
            this.picture = member.getPicture();
        }


    }

