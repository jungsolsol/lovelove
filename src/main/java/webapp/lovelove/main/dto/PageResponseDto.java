package webapp.lovelove.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberProfile;

@Getter
@NoArgsConstructor
public class PageResponseDto {

    private Double distance;
    private String nickname;

    @Builder
    public PageResponseDto(Member member) {
        this.distance = member.getDistance();
        this.nickname = member.getMemberProfile().getNickname();
    }
}
