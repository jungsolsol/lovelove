package webapp.lovelove.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.locationtech.jts.geom.Point;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.Message;
import webapp.lovelove.main.dto.ProfileDto;
import webapp.lovelove.member.domain.dto.MemberCreateDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "member_Id")
    private Long id;

    @Column(nullable = false)
    private String name;

    public Member(Long id, String name, String email, String picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    @Column(nullable = false, unique = true)
    private String email;

    private String picture;

    private String role;
    private String provider;
    private String providerId;
    @Embedded
    private MemberProfile memberProfile;
    @Embedded
    private MemberPosition memberPosition;

    @Column(name = "member_point", length = 3000)
    private Point point;



    private int messageSendCount;

    @ColumnDefault("300.0") //@ColumnDefault사용
    private Double distance;




    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<Heart> heart = new ArrayList<>();


    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Message> message = new ArrayList<>();


    @Builder
    public Member(String name, String email, String role, String provider, String providerId, MemberProfile memberProfile) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.memberProfile = memberProfile;
    }


    @Builder
    public Member(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }

    public Member(String email, MemberPosition memberPosition, String name, MemberProfile memberProfile) {
        this.email = email;
        this.memberPosition = memberPosition;
        this.name = name;
        this.memberProfile = memberProfile;
    }

    public Member(String email, MemberPosition memberPosition, String name, MemberProfile memberProfile, Point point) {
        this.email = email;
        this.memberPosition = memberPosition;
        this.name = name;
        this.memberProfile = memberProfile;
        this.point = point;
    }

    public void updateProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }

    public void editProfile(ProfileDto profileDto) {
        this.memberProfile.setHave_smoking(profileDto.getHaveSmoke());
        this.memberProfile.setIntroduce(profileDto.getInformation());
        this.memberProfile.setAlcohol(profileDto.getHaveAlcohol());
        this.memberProfile.setReligion(profileDto.getHaveReligion());
    }
    public void updatePosition(MemberPosition memberPosition) {
        this.memberPosition = memberPosition;
    }

    public void updatePoint(Point point) {
        this.point = point;
    }

    //    public void updateMessage(List<Message> message) {
//        this.message = message;
//    }
    public static String messageSendCount(int messageSendCount) {

        messageSendCount += 1;

        if (messageSendCount == 5) {

            String message = "오늘은 더이상 메시지를 보낼수없습니다";

            return message;
        } else {
            String message = "오늘" + (5 - messageSendCount) + "회 메세지 전송 가능합니다";

            return message;
        }
    }


    public static Member createMember(MemberCreateDto memberCreateDto, PrincipalDetails principalDetails) {

        Member member = Member.builder().providerId(principalDetails.getName()).name(principalDetails.getAttribute("name"))
                .email(principalDetails.getAttribute("email")).role("ROLE_USER").build();
        return member;
    }


    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public MemberProfile getMemberProfile() {
        return this.memberProfile == null ? new MemberProfile() : this.memberProfile;
    }

    public void mappingHeart(Heart heart) {
        this.heart.add(heart);
    }

    public void setDistance(Double dt) {
        this.distance = dt;

    }
}
