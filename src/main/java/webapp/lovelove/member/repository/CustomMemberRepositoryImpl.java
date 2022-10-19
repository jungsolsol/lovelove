package webapp.lovelove.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberProfileResponse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j

public class CustomMemberRepositoryImpl implements CustomMemberRepository{
    private final EntityManager em;

    @Override
    public String findByNickname(String nickname) {
        return em.createQuery("select m from Member m join m.memberProfile mp where mp.nickname like :nickname",
                        Member.class).
                setParameter("nickname", nickname).getSingleResult().getMemberProfile().getNickname().toString();
    }

    @Override
    public List<Member> findNearByMemberPostion(String pointFormat) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM member as m WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", m.member_point)", Member.class);
        List<Member> resultList = nativeQuery.getResultList();
//        List<findMemberDto> findMemberDtoList = query.getResultList();



        return resultList;
    }


}

