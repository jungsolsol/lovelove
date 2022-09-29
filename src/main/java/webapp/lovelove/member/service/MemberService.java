package webapp.lovelove.member.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.domain.dto.MemberUpdateDto;
import webapp.lovelove.member.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    public void join(MemberCreateDto form, PrincipalDetails principalDetails) {
        MemberCreateDto memberCreateDto = new MemberCreateDto();
        memberCreateDto.setSex(form.getSex());
        memberCreateDto.setNickname(form.getNickname());
        memberCreateDto.setJob(form.getJob());
        memberCreateDto.setHave_smoking(form.getHave_smoking());
        memberCreateDto.setReligion(form.getReligion());
        memberCreateDto.setHeight(form.getHeight());
        memberCreateDto.setIntroduce(form.getIntroduce());
        memberCreateDto.setAlcohol(form.getAlcohol());
        memberCreateDto.setAge(form.getAge());
        memberCreateDto.setEducation(form.getEducation());

        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberProfile mappedProfile = modelMapper.map(memberCreateDto, MemberProfile.class);
        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
        else memberRepository.save(member);
        member.updateProfile(mappedProfile);

//        duplicatedNickNameCheck(member.getMemberProfile().getNickname());

    }

    public void duplicatedNickNameCheck(String nickname) {
        String byNickname = memberRepository.findByNickname(nickname);

        System.out.println(byNickname);
        if (!byNickname.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
    }


    public boolean existsByMemberProfile_Nickname(String nickname) {
        return memberRepository.existsByMemberProfile_Nickname(nickname);
    }


    public void setMemberPosition(String lat, String lon,PrincipalDetails principalDetails) {
        Member findMember = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberPosition position = MemberPosition.builder().lat(lat).lon(lon).build();
        findMember.updatePosition(position);

    }

    public void findWomanOrManByPosition() {

    }
}
