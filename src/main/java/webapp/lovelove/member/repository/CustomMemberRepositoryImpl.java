package webapp.lovelove.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberProfileResponse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository{
    private final EntityManager em;

    @Override
    public String findByNickname(String nickname) {
        return em.createQuery("select m from Member m join m.memberProfile mp where mp.nickname like :nickname",
                        Member.class).
                setParameter("nickname", nickname).getSingleResult().getMemberProfile().getNickname().toString();
    }


}

