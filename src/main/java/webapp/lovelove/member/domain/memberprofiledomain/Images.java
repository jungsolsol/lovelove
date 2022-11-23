package webapp.lovelove.member.domain.memberprofiledomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webapp.lovelove.member.domain.Member;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class
Images {

    @Id
    @GeneratedValue
    @Column(name = "images_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    private String imgName;

    @NotEmpty
    private String imgUrl;

    private Boolean repimgYn;

    private String oriImgName;

    private long file_size;



    public void imageUpdate(String imgUrl, String imgName, String oriImgName) {
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
    }

    public void setImageId(Long id) {
        this.id = id;
    }

}
