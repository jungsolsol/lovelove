package webapp.lovelove.main.dto;

import lombok.Data;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.memberprofiledomain.Images;

import java.time.LocalDateTime;

@Data
public class HeartDto {

    private String senderNick;
    private LocalDateTime sendHeartTime;
    private String information;
    private String images;
    private String receiverNick;

    private String receiverInfo;
    private String receiverImages;

    private LocalDateTime receiverTime;

    public HeartDto(Heart h) {
        senderNick = h.getSender().getMemberProfile().getNickname();
        sendHeartTime = h.getSendHeartTime();
        information = h.getSender().getMemberProfile().getIntroduce();
        images = h.getSender().getMemberProfile().getImages().get(0).getImgUrl();
        receiverNick = h.getReceiver().getMemberProfile().getNickname();
        receiverInfo = h.getReceiver().getMemberProfile().getIntroduce();
        receiverImages = h.getReceiver().getMemberProfile().getImages().get(0).getImgUrl();
    }
}



