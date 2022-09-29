package webapp.lovelove.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.service.MemberService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class InitController {

    private final MemberService memberService;

    @GetMapping("/")
    public String init(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        try {
            if (principalDetails.getAttribute("name") != null) {
        model.addAttribute(("member"),principalDetails.getAttribute("name"));
    } }catch (NullPointerException e) {}
            return "page/init";

        }


    @GetMapping("/member/profile")
    public String createProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("member", principalDetails.getAttribute("name"));
        model.addAttribute("form", new MemberCreateDto());
        return "member/profile";
    }

    @PostMapping("/member/profile")
    public String createMember(@AuthenticationPrincipal PrincipalDetails principalDetails,@Valid MemberCreateDto form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/member/profile";
        }
        memberService.join(form, principalDetails);
//        memberService.find();
//        model.addAttribute("member", principalDetails.getAttribute("name"));

        return "redirect:/love/main";
    }
}
