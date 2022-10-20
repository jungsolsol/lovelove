package webapp.lovelove.member.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.domain.memberprofiledomain.Images;
import webapp.lovelove.member.repository.ImagesRepository;
import webapp.lovelove.member.repository.MemberRepository;
import webapp.lovelove.member.util.Direction;
import webapp.lovelove.member.util.GeometryUtil;

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
        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
        memberRepository.save(member);
        member.updateProfile(mappedProfile);

        try {
            List<Images> images = fileHandler.parseFileInfo(member, fileList);

            if (images.isEmpty()) {
                System.out.println("noImages");
            } else {
                List<Images> imageBeans = new ArrayList<>();
                for (Images image : images) {
//                    image.s(member.getId());
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

    public boolean existsByMemberProfile_Nickname(String nickname) {
        return memberRepository.existsByMemberProfile_Nickname(nickname);
    }


    public void setMemberPosition(Double lat, Double lon,PrincipalDetails principalDetails) {
        Member findMember = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberPosition position = MemberPosition.builder().lat(lat).lon(lon).build();
//        Poin point = new Point(position.getLat(), position.getLon());

        Double lat1 = position.getLat();
        Double lon1 = position.getLon();
//        memberRepository.savePoint();
        Point point = GeometryUtil.createPoint(lat, lon);
        findMember.updatePosition(position);
        findMember.updatePoint(point);

    }


    public List<findMemberDto> findWomanOrManByPosition(Double lat, Double lon, Double distance,PrincipalDetails principalDetails) {

        MemberPosition northEast = GeometryUtil.calculate(lat, lon, distance, Direction.NORTHEAST.getBearing());
        MemberPosition southWest = GeometryUtil.calculate(lat, lon, distance, Direction.SOUTHWEST.getBearing());


        double x1 = northEast.getLat();
        double y1 = northEast.getLon();
        double x2 = southWest.getLat();
        double y2 = southWest.getLon();

        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);

        List<Member> nearByMemberPostion = memberRepository.findNearByMemberPostion(pointFormat);
        String name = nearByMemberPostion.get(0).getName();
        for (Member member : nearByMemberPostion) {
            System.out.println(member.getName() + "sol");
        }


        List<findMemberDto> findMemberDtoList = new ArrayList<>();
        for (Member member : nearByMemberPostion) {
            Images images = imagesRepository.findById(member.getId()).orElseThrow(() -> new IllegalStateException("member.getId())"));
            String imgUrl = images.getImgUrl();

            findMemberDto dto = findMemberDto.builder().profile(member.getMemberProfile()).position(member.getMemberPosition())
                            .imagesUrl(imgUrl).introduce(member.getMemberProfile().getIntroduce())
                            .nickname(member.getMemberProfile().getNickname()).name(member.getName())
                    .build();

            findMemberDtoList.add(dto);
        }
//        findMemberDto findDto =  modelMapper.map(nearByMemberPostion, findMemberDto.class);

        return findMemberDtoList;

    }




}
