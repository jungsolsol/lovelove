package webapp.lovelove.member.domain.dto;

import lombok.Data;
import webapp.lovelove.member.domain.MemberStatus;

@Data
public class MemberSearch {
    private String nickname;
    private MemberStatus memberStatus;
}
