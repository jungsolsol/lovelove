package webapp.lovelove;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.main.domain.Message;
import webapp.lovelove.main.dto.MessageUpdateDto;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.*;
import webapp.lovelove.member.util.GeometryUtil;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import java.io.File;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {


    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.db1();
        initService.db2();
        initService.db3();
        System.out.println(org.hibernate.Version.getVersionString());

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final ModelMapper modelMapper;

        public void db1() {
            MemberProfile memberProfile = createMemberProfile(Sex.여자, 26, "iu", 162,
                    "student", Education.대학원졸업, "안녕하세요! ㅎㅎ", Religion.기독교,
                    have_Smoking.전혀요, Alcohol.가끔먹어요);
            MemberPosition memberPosition = new MemberPosition(37.4775811, 126.9621047
            );

            org.locationtech.jts.geom.Point point = GeometryUtil.createPoint(37.4775811, 126.9621047);
            Member member = createMember("iu", memberPosition, "아이유", memberProfile, point);
            Images images = new Images(member.getId(), member, "seounghye", "photos/20221020/seonghye.jpeg", null, "seonghye", 1111);

            em.persist(member);
            em.persist(images);
//
//            MessageUpdateDto messageUpdateDto = new MessageUpdateDto("hihihi", member.getMemberProfile().getNickname(), "sol", LocalDateTime.now());
//            Message message = MessageUpdateDto.toEntity(messageUpdateDto);
//            em.persist(message);
        }

        public void db2() {
            MemberPosition memberPosition = new MemberPosition(37.477911, 126.9612047);
            MemberProfile memberProfile = createMemberProfile(Sex.여자, 26, "karina", 168,
                    "announcer", Education.대학교졸업, "안녕 ", Religion.없어요,
                    have_Smoking.전혀요, Alcohol.가끔먹어요);
            org.locationtech.jts.geom.Point point = GeometryUtil.createPoint(37.477911, 126.9612047);
            Member member = createMember("karina@naver.com", memberPosition, "카리나", memberProfile,point);
            Images images = new Images(member.getId(), member, "yuna", "photos/20221020/yuna.jpeg", null, "yuna", 1111);

            em.persist(member);
            em.persist(images);

        }

        public void db3() {
            MemberPosition memberPosition = new MemberPosition(37.477911, 126.9613047);
            MemberProfile memberProfile = createMemberProfile(Sex.남자, 26, "남자", 178,
                    "백수", Education.고등학교졸업, "g2", Religion.없어요,
                    have_Smoking.자주해요, Alcohol.가끔먹어요);
            org.locationtech.jts.geom.Point point = GeometryUtil.createPoint(37.477911, 126.9613047);
            Member member = createMember("skawk@naver.com", memberPosition, "남자", memberProfile,point);
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
