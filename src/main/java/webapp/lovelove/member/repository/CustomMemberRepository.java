package webapp.lovelove.member.repository;

import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberProfileResponse;

import java.util.List;
import java.util.Optional;

public interface CustomMemberRepository {
    String findByNickname(String nickname);


}
