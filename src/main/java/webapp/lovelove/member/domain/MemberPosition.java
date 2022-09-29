package webapp.lovelove.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class MemberPosition {

    private String lat;
    private String lon;

    @Builder
    public MemberPosition(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }


}
