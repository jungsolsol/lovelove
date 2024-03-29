package webapp.lovelove.member.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.Message;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.main.dto.PageResponseDto;
import webapp.lovelove.main.dto.ProfileDto;
import webapp.lovelove.main.repository.HeartRepository;
import webapp.lovelove.main.repository.MessageRepository;
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
import javax.validation.constraints.Null;
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

    private final MessageRepository messageRepository;
    private final S3Service s3Service;
    private final EntityManager em;

    /**
     * 회원 가입
     */
    public void join(MemberCreateDto fileDto, List<MultipartFile> file, PrincipalDetails principalDetails) {

        Member member = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberProfile mappedProfile = modelMapper.map(fileDto, MemberProfile.class);
        if (existsByMemberProfile_Nickname(mappedProfile.getNickname()) == true) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
        memberRepository.save(member);
        member.updateProfile(mappedProfile);


        /**
         * S3에 이미지 업로드 처리
         */

        try {
            List<Images> images = s3Service.parseFileInfo(member, file);
            if (images.isEmpty()) {
                /**
                 * 이미지를 안넣었을때 기본이미지 처리
                 * 성별별 기본이미지 처리
                 */
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

    }

    /**
     * 닉네임 중복 검사
     */
    public boolean existsByMemberProfile_Nickname(String nickname) {
        return memberRepository.existsByMemberProfile_Nickname(nickname);
    }


    /**
     * DB에 현재 내위치를 저장하는 메소드
     */
    public void setMemberPosition(Double lat, Double lon,PrincipalDetails principalDetails) {
        Member findMember = memberRepository.findByEmail(principalDetails.getAttribute("email"));
        MemberPosition position = MemberPosition.builder().lat(lat).lon(lon).build();
        Double lat1 = position.getLat();
        Double lon1 = position.getLon();
        Point point = GeometryUtil.createPoint(lat, lon);
        findMember.updatePosition(position);
        findMember.updatePoint(point);

    }


    /**
     * @param lat 현재 내 위도
     * @param lon 현재 내 경도
     * @param distance 주변 거리
     * @param principalDetails securitycontext에 있는 내 정보
     * @return 현재 내 위도,경도를 통해 찾아낸 주변 이성 데이터
     */
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
            //성별과 거리에 따른 주변사람 탐색
            findAllMemberByMyGenderAndPosition(nearByMemberPostion, findMemberDtoList ,m);
                }, () ->
                {

            System.out.println("GenderSelectionError");
                }

        );



        return findMemberDtoList;

    }

    /**
     * 남자는 여자만 여자는 남자만 보이게 하는 메소드
     * @param nearByMemberPostion
     * @param findMemberDtoList
     * @param me
     */
    private void findAllMemberByMyGenderAndPosition(List<Member> nearByMemberPostion, List<findMemberDto> findMemberDtoList, Member me
    )

    {
        try {
            for (Member member : nearByMemberPostion) {

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

    /**
     * 좋아요 버튼 클릭시
     * 좋아요가 이미 눌러져있으면
     * 좋아요를 삭제
     */
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

    /**
     * 좋아요 기능구현
     *
     */
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

    /**
     * 프로필 수정
     * @param member
     * @param profileDto
     */

    public void updateProfile(Member member, ProfileDto profileDto) {
        member.editProfile(profileDto);
        em.persist(member);
        em.flush();
        em.clear();
    }


    /**
     *
     * 회원가입이 안되어있을시에는 Member/profile로 리턴해서 회원가입 작성
     * 이미 회원가입되어있을시엔 love/main 으로 리다이렉트
     */
    public boolean existMemberProfileByEmailAndName(String email, String name) {

        Optional<Member> findMember = memberRepository.findByEmailAndName(email, name);
        if (findMember.get().getMemberProfile().getNickname() == null) {
            return true;
        }
        else
            return false;
    }


    /**
     *
     * 멤버 회원탈퇴
     */
    public void secessionMember(Long id) {
        Member findMember = memberRepository.findById(id).get();
        try {
            List<Message> findMessages = findMember.getMessage();
            List<Heart> findHearts = findMember.getHeart();

            List<Message> allBySender = messageRepository.findAllBySender(findMember);
            allBySender.forEach(s -> messageRepository.deleteAll());
            heartRepository.findAllBySender(findMember).forEach(s-> heartRepository.deleteAll());

        }

        finally {

            imagesRepository.findById(id).ifPresentOrElse(
                    message -> imagesRepository.deleteById(id), () -> System.out.println("이미지값이 없음"));

            memberRepository.findById(id).ifPresent(
                    member -> memberRepository.deleteById(id)
            );
        }



    }


    @Transactional
    public PageResponseDto getMap(String email) {
        Member member = memberRepository.findByEmail(email);
        return new PageResponseDto(member);
    }
}
