package webapp.lovelove.main.dto;

import lombok.Data;
import webapp.lovelove.main.domain.Message;

import java.time.LocalDateTime;

@Data

public class MessageUpdateDto {
    private String contents;
    private String senderNick;
    private String receiverNick;
    private LocalDateTime receivedTime;


    public MessageUpdateDto(String contents, String senderNick, String receiverNick, LocalDateTime receivedTime) {

    }

    public MessageUpdateDto(Message m) {
        contents = m.getContents();
        senderNick = m.getSender().getMemberProfile().getNickname();
        receiverNick = m.getReceiver().getMemberProfile().getNickname();
        receivedTime = m.getReceivedTime();
    }


    public static MessageUpdateDto toDto(Message message) {
        return new MessageUpdateDto(
                message.getContents(),
                message.getSender().getMemberProfile().getNickname(),
                message.getReceiver().getMemberProfile().getNickname(),
                message.getReceivedTime()
        );
    }

    public static Message toEntity(MessageUpdateDto messageUpdateDto) {
        Message message = Message.builder().contents(messageUpdateDto.getContents())
                .sendingNickname(messageUpdateDto.getSenderNick()).deletedBySender(false).deletedByReceiver(false)
                .receivedTime(messageUpdateDto.getReceivedTime()).likeOrDislike(false).build();
        return message;
    }


}
