package webapp.lovelove.member.domain.dto;

import lombok.*;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {


    @Enumerated(EnumType.STRING)
    private Sex sex;
    private int age;
    private String nickname;
    private int height;
    private String job;
    @Enumerated(EnumType.STRING)
    private Education education;
    private String introduce;
    @Enumerated(EnumType.STRING)
    private Religion religion;
    @Enumerated(EnumType.STRING)
    private have_Smoking have_smoking;
    @Enumerated(EnumType.STRING)
    private Alcohol alcohol;

    @Builder
    public MemberProfileResponse(Sex sex, int age, String nickname, int height, String job, Education education
            , String introduce, Religion religion, have_Smoking have_smoking, Alcohol alcohol) {
        this.sex = sex;
        this.nickname = nickname;
        this.age = age;
        this.height = height;
        this.job = job;
        this.education = education;
        this.introduce = introduce;
        this.religion = religion;
        this.have_smoking = have_smoking;
        this.alcohol = alcohol;
    }

    public static MemberProfileResponse from(MemberProfile memberProfile) {
        return MemberProfileResponse.builder().
                sex(memberProfile.getSex()).nickname(memberProfile.getNickname()).age(memberProfile.getAge())
                .height(memberProfile.getHeight()).job(memberProfile.getJob()).education(memberProfile.getEducation())
                .introduce(memberProfile.getIntroduce()).religion(memberProfile.getReligion()).have_smoking(memberProfile.getHave_smoking())
                .alcohol(memberProfile.getAlcohol()).build();
                    }
}
