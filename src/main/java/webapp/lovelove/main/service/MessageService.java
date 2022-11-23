package webapp.lovelove.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webapp.lovelove.main.domain.Message;
import webapp.lovelove.main.dto.MessageUpdateDto;
import webapp.lovelove.main.repository.MessageRepository;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    public void sendMessage(String message, String nick, String email) {
        Member receivedMember = memberRepository.findByNickname(nick);
        Member sendMember = memberRepository.findByEmail(email);
        sendMessageCount();

        Message msg = Message.builder().sender(sendMember).receiver(receivedMember).likeOrDislike(false).
                deletedBySender(false).deletedByReceiver(false).contents(message).receivedTime(LocalDateTime.now()).sendingNickname(sendMember.getMemberProfile().getNickname()).build();
        messageRepository.save(msg);
    }

    public void sendMessageCount() {

    }
    @Transactional(readOnly = true)
    public List<MessageUpdateDto> receivedMessage(Member member) {
        // 받은 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        List<Message> messages = messageRepository.findAllByReceiver(member);
        List<MessageUpdateDto> messageDtos = new ArrayList<>();

        List<MessageUpdateDto> messageUpdateDtos = messages.stream().map(m -> new MessageUpdateDto(m)).collect(Collectors.toList());
        return messageUpdateDtos;
    }

    @Transactional(readOnly = true)
    public List<MessageUpdateDto> sentMessage(Member member) {

        List<Message> messages = messageRepository.findAllBySender(member);
        messages.stream().map(m -> new MessageUpdateDto(m)).collect(Collectors.toList());
        return messages.stream().map(m -> new MessageUpdateDto(m)).collect(Collectors.toList());
    }
}
