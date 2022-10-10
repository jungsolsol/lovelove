package webapp.lovelove.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.memberprofiledomain.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto2 {
    private Sex sex;
    private int age;
    private String nickname;
    private int height;
    private String job;
    private Education education;
    private String introduce;
    private Religion religion;
    private have_Smoking have_smoking;
    private Alcohol alcohol;

    private List<MultipartFile> files;
}
