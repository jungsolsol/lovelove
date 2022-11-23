package webapp.lovelove.main.repository;

import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;

import java.util.List;

public interface CustomHeartRepository {

    List<Heart> findAllByMember(Member member);

}
