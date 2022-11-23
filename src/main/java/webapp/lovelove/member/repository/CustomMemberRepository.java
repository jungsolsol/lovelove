package webapp.lovelove.member.repository;
import webapp.lovelove.member.domain.Member;
import java.util.List;

public interface CustomMemberRepository {
    Member findByNickname(String nickname);

    List<Member> findNearByMemberPostion(String pointFormat);



}
