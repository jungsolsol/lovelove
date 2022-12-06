package webapp.lovelove.member.repository;

import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webapp.lovelove.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member> findById(Long Id);
    boolean existsById(Long id);
    Member findByEmail(String email);

    Optional<Member> findByEmailAndName(String email, String name);

    boolean existsByMemberProfile_Nickname(String nickname);


}
