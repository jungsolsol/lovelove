package webapp.lovelove.main.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.MemberProfile;

@Data
public class findMemberDto {

    private MemberProfile profile;
    private MultipartFile images;
    private String introduce;
    private String nickname;
    private String name;
}
