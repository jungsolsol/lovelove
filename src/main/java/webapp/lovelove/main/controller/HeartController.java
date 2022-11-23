package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.dto.HeartDto;
import webapp.lovelove.main.repository.HeartRepository;
import webapp.lovelove.main.service.HeartService;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.repository.MemberRepository;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @GetMapping("love/main/heart/{nick}")
    public String sendHeart(@PathVariable("nick") String nick, @AuthenticationPrincipal PrincipalDetails principalDetails
            , Model model) {

        // 하트를 보냈는지 확인

        try {
            String email = principalDetails.getAttribute("email");
            memberService.memberLike(nick, email);


            HashMap alreadyAddLike = memberService.isAlreadyAddLike(nick, email);

            model.addAttribute("alreadyAddLike", alreadyAddLike);

        } catch (Exception e) {
            System.out.println("error");

        }

        return "redirect:love/main";
    }

    @GetMapping("love/main/heart")
    public String likedMeList(@AuthenticationPrincipal PrincipalDetails principalDetails
    ,Model model) {
        String email = principalDetails.getAttribute("email");
        model.addAttribute("nick", memberRepository.findByEmail(email).getMemberProfile().getNickname());
        List<HeartDto> heartDtos = heartService.likeMeList(email);
        List<HeartDto> melikeList = heartService.meLikeList(email);
        model.addAttribute("heartList", heartDtos);
        model.addAttribute("sendHeart", melikeList);
        return "love/heart";
    }

}
