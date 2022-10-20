package webapp.lovelove;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.*;
import webapp.lovelove.member.util.GeometryUtil;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import java.io.File;

@Component
@RequiredArgsConstructor
public class InitDb {


    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.db1();
        initService.db2();
        System.out.println(org.hibernate.Version.getVersionString());

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void db1() {
            MemberProfile memberProfile = createMemberProfile(Sex.Female, 26, "seounghye", 162,
                    "student", Education.CollegeGraduate, "안녕하세요! ㅎㅎ", Religion.Christian,
                    have_Smoking.No, Alcohol.Sometime);
            MemberPosition memberPosition = new MemberPosition(37.585740751391, 127.0299015542);

            org.locationtech.jts.geom.Point point = GeometryUtil.createPoint(37.585740751391, 127.0299015542);
            Member member = createMember("seounghye", memberPosition, "이승혜", memberProfile, point);
            Images images = new Images(member.getId(), member, "seounghye", "photos/20221020/seonghye.jpeg", null, "seonghye", 1111);

            em.persist(member);
            em.persist(images);

        }

        public void db2() {
            MemberPosition memberPosition = new MemberPosition(37.4795498, 126.9524267);
            MemberProfile memberProfile = createMemberProfile(Sex.Female, 26, "yuna", 168,
                    "announcer", Education.CollegeGraduate, "안녕 ", Religion.None,
                    have_Smoking.Sometime, Alcohol.Sometime);
            org.locationtech.jts.geom.Point point = GeometryUtil.createPoint(37.4795498, 126.9524267);
            Member member = createMember("yuna@naver.com", memberPosition, "정유나", memberProfile,point);
            Images images = new Images(member.getId(), member, "yuna", "photos/20221020/yuna.jpeg", null, "yuna", 1111);

            em.persist(member);
            em.persist(images);

        }

        private MemberProfile createMemberProfile(Sex female, int i, String seounghye, int i1, String student, Education collegeGraduate,
                                                  String s, Religion christian, have_Smoking no, Alcohol sometime) {
            MemberProfile memberProfile = new MemberProfile(female, i, seounghye, i1, student, collegeGraduate
                    , s, christian, no, sometime);
            return memberProfile;
        }


        private Member createMember(String email, MemberPosition memberPosition, String name, MemberProfile memberProfile, org.locationtech.jts.geom.Point point) {
            Member member = new Member(email, memberPosition, name, memberProfile, point);
            return member;
        }

    }

}
