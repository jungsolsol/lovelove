package webapp.lovelove.member.domain.dto;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;

@Data
public class MemberUpdateDto {

    private String name;
    private String email;
    private String picture;
    private MemberProfile memberProfile;

    @Autowired
    private static ModelMapper modelMapper;

    //dto -> entity
    public Member memberUpdate(MemberProfile memberProfile) {
        return modelMapper.map(this, Member.class);
    }

    //entity-> dto
    public static MemberUpdateDto of(Member member, MemberProfile memberProfile) {
        return modelMapper.map(member, MemberUpdateDto.class);
    }

    public Member toEntity() {
        return Member.builder().email(email).name(name).memberProfile(memberProfile).build();
    }
}
