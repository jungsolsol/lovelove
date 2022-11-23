package webapp.lovelove.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.lovelove.main.domain.Message;
import webapp.lovelove.member.domain.Member;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    List<Message> findAllByReceiver(Member member);
    List<Message> findAllBySender(Member member);

}
