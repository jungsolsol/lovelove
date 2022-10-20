package webapp.lovelove.main.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.domain.MemberProfile;
import webapp.lovelove.member.domain.memberprofiledomain.Images;

@Data
@Builder
public class findMemberDto {

    private MemberPosition position;
    private MemberProfile profile;
    private String imagesUrl;
    private String introduce;
    private String nickname;
    private String name;

}
