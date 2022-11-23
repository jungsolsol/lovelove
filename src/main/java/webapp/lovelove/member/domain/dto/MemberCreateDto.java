package webapp.lovelove.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class MemberCreateDto {


    private Sex sex;

    @NotNull(message = "나이를 적어주세요")
    private int age;

    @NotBlank(message = "닉네임을 정해주세요")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @Range(min = 140, max = 220,message = "올바른 값 적어주세요")
    private int height;

    @NotBlank(message = "직업을 적어주세요")
    @Pattern(regexp = "^[가-힣]+[가-힣\\s]*$", message = "한글로 적어주세요")
    private String job;
    private Education education;
    @Size(max = 30 ,message = "30자 이내로 간단히 부탁드려요")
    private String introduce;
    private Religion religion;
    private have_Smoking have_smoking;
    private Alcohol alcohol;


    @Autowired
    private static ModelMapper modelMapper;

    //Dto -> Entity
    public MemberProfile createMemberProfile() {

        return modelMapper.map(this, MemberProfile.class);
    }

    //Entity -> dto
    public static MemberCreateDto of(MemberProfile memberProfile) {
        return modelMapper.map(memberProfile, MemberCreateDto.class);
    }


    @Builder
    public MemberCreateDto(Sex sex, int age, String nickname, int height, String job
            , Education education, String introduce, Religion religion, have_Smoking have_smoking, Alcohol alcohol) {
        this.sex = sex;
        this.age = age;
        this.nickname = nickname;
        this.height = height;
        this.job = job;
        this.education = education;
        this.religion = religion;
        this.have_smoking = have_smoking;
        this.alcohol = alcohol;
    }


}
