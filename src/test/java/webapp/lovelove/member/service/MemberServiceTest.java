package webapp.lovelove.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.Role;
import webapp.lovelove.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {


    @Autowired
    MemberRepository memberRepository;
    @Test
    public void oneToOne() {
        String name = "sol";
        String nickname = "solsol";
        String email = "ssss";


        Member member = Member.builder().name(name).email(email).role("ROLE_USER").build();
        MemberProfile memberProfile = MemberProfile.builder().nickname(nickname).height(155).age(20).build();
        memberRepository.save(member);
        System.out.println("member1 = " + member);
        member.updateProfile(email, memberProfile);
        System.out.println("member2 = " + member);

        System.out.println(memberRepository.findAll().get(0).getMemberProfile().getAge());

    }
}