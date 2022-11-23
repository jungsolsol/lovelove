package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.dto.MessageDto;
import webapp.lovelove.main.dto.MessageUpdateDto;
import webapp.lovelove.main.service.MessageService;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.repository.MemberRepository;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    @GetMapping("love/message/{nick}")
    public String makeMessage(@PathVariable("nick") String nick, Model model) {
        model.addAttribute("nick", nick);
        return "love/message";
    }

    @PostMapping("love/message/{nick}")
    public RedirectView sendMessage(MessageDto message,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    @PathVariable String nick) {
        String email = principalDetails.getAttribute("email");
        messageService.sendMessage(message.getMessage(), nick, email);
        return new RedirectView("/love/main");
    }

    @GetMapping("love/message/{nick}/received")
    public String receiveMessage(@PathVariable String nick, @AuthenticationPrincipal PrincipalDetails principalDetails
    ,Model model) {
        String email = principalDetails.getAttribute("email");
        model.addAttribute("nick", nick);
        Member member = memberRepository.findByEmail(email);
        List<MessageUpdateDto> receivedMessage = messageService.receivedMessage(member);
        model.addAttribute("rev", receivedMessage);
        List<MessageUpdateDto> sentMessage = messageService.sentMessage(member);
        model.addAttribute("sent", sentMessage);

        return "love/message/received";
    }


    @GetMapping("love/message/{nick}/sent")
    public String sentMessage(@PathVariable String nick, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String email = (String) principalDetails.getAttribute("email");
        Member member = memberRepository.findByEmail(email);
        List<MessageUpdateDto> messageUpdateDtos = messageService.sentMessage(member);
        return "love/message/sent";
    }


}
