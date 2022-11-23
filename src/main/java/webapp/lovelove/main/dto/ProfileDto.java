package webapp.lovelove.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.Alcohol;
import webapp.lovelove.member.domain.memberprofiledomain.Religion;
import webapp.lovelove.member.domain.memberprofiledomain.have_Smoking;

@Data
@AllArgsConstructor
public class ProfileDto {

//    private String nickname;
    private have_Smoking haveSmoke;
    private Alcohol haveAlcohol;
    private Religion haveReligion;
    @Length(max = 35)
    private String information;

    private String images;


    public static ProfileDto toDto(Member member) {


        return new ProfileDto(member.getMemberProfile().getHave_smoking()
                , member.getMemberProfile().getAlcohol(), member.getMemberProfile().getReligion(), member.getMemberProfile().getIntroduce()
                , member.getMemberProfile().getImages().get(0).getImgUrl());

    }
}
