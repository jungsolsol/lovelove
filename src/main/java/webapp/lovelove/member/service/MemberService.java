package webapp.lovelove.member.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.controller.InitController;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.domain.dto.MemberCreateDto2;
import webapp.lovelove.member.domain.dto.MemberUpdateDto;
import webapp.lovelove.member.domain.memberprofiledomain.Images;
import webapp.lovelove.member.domain.memberprofiledomain.Sex;
import webapp.lovelove.member.domain.vo.MemberVo;
import webapp.lovelove.member.repository.ImagesRepository;
import webapp.lovelove.member.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    private final FileHandler fileHandler;
    private final ImagesRepository imagesRepository;
//
//    public void join(MemberCreateDto form, PrincipalDetails principalDetails) {
//        MemberCreateDto memberCreateDto = new MemberCreateDto();
//        memberCreateDto.setSex(form.getSex());
//        memberCreateDto.setNickname(form.getNickname());
//        memberCreateDto.setJob(form.getJob());
//        memberCreateDto.setHave_smoking(form.getHave_smoking());
//        memberCreateDto.setReligion(form.getReligion());
//        memberCreateDto.setHeight(form.getHeight());
//        memberCreateDto.setIntroduce(form.getIntroduce());
//        memberCreateDto.setAlcohol(form.getAlcohol());
//        memberCreateDto.setAge(form.getAge());
//        memberCreateDto.setEducation(form.getEducation());
//
//        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
//        MemberProfile mappedProfile = modelMapper.map(memberCreateDto, MemberProfile.class);
//        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
//            throw new IllegalStateException("이미 존재하는 닉네임입니다");
//        }
//        else memberRepository.save(member);
//        member.updateProfile(mappedProfile);
//
//        try {
//            List<Images> images = fileHandler.parseFileInfo(member.getId(), files);
//
//            if (images.isEmpty()) {
//                /**TO DO
//                 * 파일이 없을시 처리
//                 */
//            } else {
//                List<Images> imageBeans = new ArrayList<>();
//                for (Images image : images) {
//                    imageBeans.add(imagesRepository.save(image));
//                }
//                mappedProfile.setImagesUrl(imageBeans);
//                member.updateProfile(mappedProfile);
//                memberRepository.save(member);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//////        duplicatedNickNameCheck(member.getMemberProfile().getNickname());
//
//    }
    public void join(MemberCreateDto fileDto, List<MultipartFile> fileList, PrincipalDetails principalDetails) {


        System.out.println(fileDto.getSex());
        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberProfile mappedProfile = modelMapper.map(fileDto, MemberProfile.class);
//        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
//            throw new IllegalStateException("이미 존재하는 닉네임입니다");
//        }
        memberRepository.save(member);
        member.updateProfile(mappedProfile);

        try {
            List<Images> images = fileHandler.parseFileInfo(member.getId(), fileList);

            if (images.isEmpty()) {
                System.out.println("noImages");
            } else {
                List<Images> imageBeans = new ArrayList<>();
                for (Images image : images) {
                    imageBeans.add(imagesRepository.save(image));
                }
                mappedProfile.setImagesUrl(imageBeans);
                member.updateProfile(mappedProfile);
                memberRepository.save(member);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public void joinWithImage(Map<String, Object> param, List<MultipartFile> fileList) throws Exception {


    }
}
