package webapp.lovelove.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {
    private String userId;
    private String email;
    private String name;
    private String nickname;
}
