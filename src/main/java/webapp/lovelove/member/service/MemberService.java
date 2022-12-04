package webapp.lovelove.member.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.main.dto.ProfileDto;
import webapp.lovelove.main.repository.HeartRepository;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.domain.memberprofiledomain.Images;
import webapp.lovelove.member.domain.memberprofiledomain.Sex;
import webapp.lovelove.member.repository.ImagesRepository;
import webapp.lovelove.member.repository.MemberRepository;
import webapp.lovelove.member.util.Direction;
import webapp.lovelove.member.util.GeometryUtil;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    private final FileHandler fileHandler;
    private final ImagesRepository imagesRepository;

    private final HeartRepository heartRepository;

    private final EntityManager em;

    public void join(MemberCreateDto fileDto, List<MultipartFile> fileList, PrincipalDetails principalDetails) {

        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberProfile mappedProfile = modelMapper.map(fileDto, MemberProfile.class);
        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
            //ToDo List
            //CustomError 처리
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
        memberRepository.save(member);
        member.updateProfile(mappedProfile);


        try {
            List<Images> images = fileHandler.parseFileInfo(member, fileList);

            //ToDo List
            //기본이미지 설정
            if (images.isEmpty()) {
                //성별 남자일시 기본이미지
                if (fileDto.getSex() == Sex.남자) {
                    Images img = new Images(member.getId(), member, "남자기본", "photos/20221020/man.jpg", null, "man", 111);
                    List<Images> imageBeans = new ArrayList<>();
                    imageBeans.add(imagesRepository.save(img));
                    mappedProfile.setImagesUrl(imageBeans);
                    member.updateProfile(mappedProfile);
                    memberRepository.save(member);
                } else {

                    Images img = new Images(member.getId(), member, "여자기본", "photos/20221020/woman.jpg", null, "woman", 111);
                    List<Images> imageBeans = new ArrayList<>();
                    imageBeans.add(imagesRepository.save(img));
                    mappedProfile.setImagesUrl(imageBeans);
                    member.updateProfile(mappedProfile);
                    memberRepository.save(member);
                }
            } else {
                List<Images> imageBeans = new ArrayList<>();
                for (Images image : images) {
//                    image.s(member.getId());
                    /**
                     * 추가
                     */
                    imageBeans.add(fileHandler.savePost(image));

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
        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        List<findMemberDto> findMemberDtoList = new ArrayList<>();
        Optional.of(member).ifPresentOrElse(m -> {

            System.out.println(m.getMemberProfile().getSex());
            //성별과 거리에 따른 주변사람 탐색
            findAllMemberByMyGenderAndPosition(nearByMemberPostion, findMemberDtoList ,m);
                }, () -> {
            System.out.println("GenderSelectionError");
                }

        );


//        findMemberDto findDto =  modelMapper.map(nearByMemberPostion, findMemberDto.class);

        return findMemberDtoList;

    }

    private void findAllMemberByMyGenderAndPosition(List<Member> nearByMemberPostion, List<findMemberDto> findMemberDtoList, Member me
    )

    {
        try {
            for (Member member : nearByMemberPostion) {

                //여자는 남자만 남자는 여자만 보이게
                if (member.getMemberProfile().getSex() != me.getMemberProfile().getSex()) {
                    Images images = imagesRepository.findById(member.getId()).orElseThrow(() -> new IllegalStateException("member.getId())"));
                    String imgUrl = images.getImgUrl();
                    System.out.println(member.getMemberProfile().getSex());
                    findMemberDto dto = findMemberDto.builder().profile(member.getMemberProfile()).position(member.getMemberPosition())
                            .imagesUrl(imgUrl).introduce(member.getMemberProfile().getIntroduce())
                            .nickname(member.getMemberProfile().getNickname()).name(member.getName())
                            .build();

                    findMemberDtoList.add(dto);

                } else {

                    //이성이 없을시

                    //1. 테스트 데이터


                }

            }

        } finally {

            findMemberDtoList.add(findMemberDto.builder().profile(me.getMemberProfile()).position(me.getMemberPosition())
                    .introduce(me.getMemberProfile().getIntroduce())
                    .nickname(me.getMemberProfile().getNickname()).name(me.getName())
                    .build());

        }
    }

    public HashMap isAlreadyAddLike(String nick, String email) {
        Member sentMember = memberRepository.findByEmail(email);
        List<Heart> allByMember = heartRepository.findAllByMember(sentMember);
        HashMap map = new HashMap();
        for (Heart heart : allByMember) {
            if (heart.getSender().getId() == sentMember.getId()) {
                map.put(nick, true);
                return map;
            } else {
                map.put(nick, false);
                return map;
            }
        }

        return map;
    }
    public void memberLike(String nick, String email) {
        Member member = memberRepository.findByNickname(nick);
        Member sentMember = memberRepository.findByEmail(email);
        System.out.println(member.getName());
        Optional<Heart> heart = heartRepository.findByReceiver(member);
        heart.ifPresentOrElse(
                heartPresent -> {
                    heartRepository.delete(heartPresent);
                },
                () ->{
                    Heart heart1 = Heart.builder().sender(sentMember).receiver(member).sendHeartTime(LocalDateTime.now()).build();
                    heartRepository.save(heart1);
                    System.out.println(heart1.getSender());

                }
        );
    }

    public void updateProfile(Member member, ProfileDto profileDto) {
        member.editProfile(profileDto);
        em.persist(member);
        em.flush();
        em.clear();
    }


    public Double setfindMemberDistance(Double dt) {
        return dt;
    }

    public boolean existsById(Long Id) { return memberRepository.existsById(Id); }


    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. id = " + id));
    }


}
