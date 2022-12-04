package webapp.lovelove.member.domain.dto;


import lombok.*;
import webapp.lovelove.member.domain.memberprofiledomain.Images;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageDto {

    private Long id;
    private String imgName;
    private String imgUrl;



    @Builder
    public ImageDto(Long id, String imgName, String imgUrl) {
        this.id = id;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
