package webapp.lovelove.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.main.dto.HeartDto;
import webapp.lovelove.main.repository.HeartRepository;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    private final MemberRepository memberRepository;


    //나를 좋아요 누른 사람
    public List<HeartDto> likeMeList(String email) {
        Member member = memberRepository.findByEmail(email);
        List<Heart> likeMe = heartRepository.findAllBySender(member);
        String noOneSendMeHeart = "하트를 보낸 사람이 아직없습니다";
        if (likeMe.isEmpty()) {
            new Exception("하트를 보낸 사람이 아직없습니다");

        }
        return likeMe.stream().map(h -> new HeartDto(h)).collect(Collectors.toList());
    }

    public List<HeartDto> meLikeList(String email) {
        List<Heart> member = heartRepository.findAllByMember(memberRepository.findByEmail(email));
        if (member.isEmpty()) {
            new Exception("하트를 보낸적이 없습니다");
        }
        return member.stream().map(h -> new HeartDto(h)).collect(Collectors.toList());
    }

    public int countReceivedHeart(Member member) {
        return heartRepository.findAllByMember(member).size();
    }
    public  int countSendHeart(Member member) {
        return heartRepository.findAllBySender(member).size();
    }
}
