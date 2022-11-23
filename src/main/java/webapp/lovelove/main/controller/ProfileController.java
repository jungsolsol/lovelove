package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.dto.ProfileDto;
import webapp.lovelove.main.service.HeartService;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.repository.MemberRepository;
import webapp.lovelove.member.service.MemberService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final MemberRepository memberRepository;
    private final HeartService heartService;
    private final MemberService memberService;

    @GetMapping("love/main/{nick}")
    public String profile(@PathVariable("nick") String nick, @AuthenticationPrincipal PrincipalDetails principalDetails
            , Model model) {
        Member member = memberRepository.findByNickname(nick);
        model.addAttribute("nick", nick);
        model.addAttribute("profile", ProfileDto.toDto(member));
        model.addAttribute("revHeart", heartService.countReceivedHeart(member));
        model.addAttribute("sendHeart", heartService.countSendHeart(member));
        return "love/profile";
    }

    //수정 폼 조회
    @GetMapping("love/main/{nick}/edit")
    public String editProfile(@PathVariable("nick") String nick, Model model) {
        Member member = memberRepository.findByNickname(nick);
        model.addAttribute("nick", nick);
        model.addAttribute("profileDto",ProfileDto.toDto(member));
        return "love/profileEdit";
    }


    //수정
    @PostMapping("love/main/{nick}/edit")
    public RedirectView editProfilePut(@PathVariable("nick") String nick
            , RedirectAttributes attributes, @Valid @ModelAttribute  ProfileDto profileDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new RedirectView("love/main/{nick}/edit");
        }
        memberService.updateProfile(memberRepository.findByNickname(nick), profileDto);
        return new RedirectView("");
    }
}
