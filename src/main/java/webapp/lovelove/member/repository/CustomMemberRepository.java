package webapp.lovelove.member.repository;

import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberProfileResponse;

import java.util.List;
import java.util.Optional;

public interface CustomMemberRepository {
    String findByNickname(String nickname);

    List<Member> findNearByMemberPostion(String pointFormat);



}
