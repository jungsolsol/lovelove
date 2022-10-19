package webapp.lovelove.member.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.*;

@Data
public class MemberCreateDto {


    private Sex sex;
    private int age;
    private String nickname;
    private int height;
    private String job;
    private Education education;
    private String introduce;
    private Religion religion;
    private have_Smoking have_smoking;
    private Alcohol alcohol;


    @Autowired
    private static ModelMapper modelMapper;

    //Dto -> Entity
    public MemberProfile createMemberProfile() {

        return modelMapper.map(this, MemberProfile.class);
    }

    //Entity -> dto
    public static MemberCreateDto of(MemberProfile memberProfile) {
        return modelMapper.map(memberProfile, MemberCreateDto.class);
    }


}
