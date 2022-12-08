package webapp.lovelove.main.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import webapp.lovelove.main.dto.MessageUpdateDto;
import webapp.lovelove.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String sendingNickname;
    private String contents;

    private boolean likeOrDislike;

    private LocalDateTime receivedTime;

    private boolean deletedBySender;

    private boolean deletedByReceiver;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member receiver;

    @Builder
    public Message(String sendingNickname, String contents, LocalDateTime receivedTime,
                   Member receiver, boolean likeOrDislike, boolean deletedByReceiver, boolean deletedBySender,
                   Member sender) {

        this.sendingNickname = sendingNickname;
        this.contents = contents;
        this.receivedTime = receivedTime;
        this.receiver = receiver;
        this.likeOrDislike = likeOrDislike;
        this.deletedByReceiver = deletedByReceiver;
        this.deletedBySender = deletedBySender;
        this.sender = sender;
    }

}
