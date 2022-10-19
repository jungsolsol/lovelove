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

    private Double lat;
    private Double lon;

    @Builder
    public MemberPosition(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }



}
