package webapp.lovelove.member.domain;

import lombok.*;
import webapp.lovelove.member.domain.memberprofiledomain.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@Setter
public class MemberProfile  {

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Positive
    private int age;
    private String nickname;
    @Positive
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

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public MemberProfile(Sex sex, int age, String nickname, int height, String job, Education education
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

}
