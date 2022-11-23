package webapp.lovelove.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import webapp.lovelove.member.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j

public class CustomMemberRepositoryImpl implements CustomMemberRepository{
    private final EntityManager em;

    @Override
    public Member findByNickname(String nickname) {
        return em.createQuery("select m from Member m join m.memberProfile mp where mp.nickname like :nickname",
                        Member.class).
                setParameter("nickname", nickname).getSingleResult();
    }

    @Override
    public List<Member> findNearByMemberPostion(String pointFormat) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM member as m WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", m.member_point)", Member.class);
        List<Member> resultList = nativeQuery.getResultList();

//        List<findMemberDto> findMemberDtoList = query.getResultList();



        return resultList;
    }


}

