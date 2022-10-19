package webapp.lovelove.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.dto.MemberCreateDto;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_Id")
    private Long id;

    @Column(nullable = false)
    private String name;

    public Member(Long id, String name, String email, String picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    @Column(nullable = false,unique = true)
    private String email;

    private String picture;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

    private String role;
    private String provider;
    private String providerId;
    @Embedded
    private MemberProfile memberProfile;

    @Embedded
    private MemberPosition memberPosition;

    @Column(name = "member_point")
    private Point point;


    @Builder
    public Member(String name, String email,String role,String provider, String providerId, MemberProfile memberProfile) {
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

    public Member(String email, MemberPosition memberPosition,String name, MemberProfile memberProfile) {
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

    public void updatePosition(MemberPosition memberPosition) {
        this.memberPosition = memberPosition;
    }

    public void updatePoint(Point point) {
        this.point = point;
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

//    public String getRoleKey() {
//        return this.role.getKey();
//    }


}
