package webapp.lovelove.main.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomHeartRepositoryImpl implements CustomHeartRepository {

    private final EntityManager em;

    @Override
    public List<Heart> findAllByMember(Member member) {
       return em.createQuery("select h from  Heart h , Member m where h.sender = m").getResultList();
    }
}
