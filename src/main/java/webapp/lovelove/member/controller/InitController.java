package webapp.lovelove.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.service.FileHandler;
import webapp.lovelove.member.service.MemberService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InitController {

    private final MemberService memberService;

    private final FileHandler fileHandler;

    @GetMapping("/")
    public String init(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        try {
            if (principalDetails.getAttribute("name") != null) {
                model.addAttribute(("member"), principalDetails.getAttribute("name"));
            }
        } catch (NullPointerException e) {
        }
        return "page/init";

    }

    @GetMapping("/member/profile")
    public String createProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        model.addAttribute("member", principalDetails.getAttribute("name"));
        model.addAttribute("memberCreateDto", new MemberCreateDto());
        return "member/profile";
    }

    @PostMapping(value = "/member/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                               @RequestPart(value = "file") List<MultipartFile> files,
                               @Valid  MemberCreateDto memberCreateDto, BindingResult bindingResult
                               ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "member/profile";
        }
        log.info(memberCreateDto.toString(), memberCreateDto.getAge());
        memberService.join(memberCreateDto,files,principalDetails);
        return "redirect:/love/main";
    }


}
