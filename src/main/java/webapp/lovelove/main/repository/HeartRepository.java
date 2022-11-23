package webapp.lovelove.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;

import java.util.List;
import java.util.Optional;


@Repository
public interface HeartRepository extends JpaRepository<Heart, Long>, CustomHeartRepository {


    Optional<Heart> findByReceiver(Member member);

    List<Heart> findAllBySender(Member member);

}
