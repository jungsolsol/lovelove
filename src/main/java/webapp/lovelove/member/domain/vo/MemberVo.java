package webapp.lovelove.member.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.memberprofiledomain.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberVo {
    private Sex sex;
    private int age;
    private int height;
    private String job;
    private Religion religion;

    private String nickname;
    private Education education;
    private have_Smoking have_smoking;
    private Alcohol alcohol;
    private String introduce;
}
