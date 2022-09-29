package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.coyote.Request;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/love/main")
    public void main(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, MemberCreateDto form
    ) {
//        memberService.join(form, principalDetails);
        model.addAttribute("member", principalDetails.getAttribute("name"));
    }

    @PostMapping("/love/main")
    public void main_AddingLocation(@RequestParam("lat") String lat, @RequestParam("lon") String lon,@AuthenticationPrincipal PrincipalDetails principalDetails){
        memberService.setMemberPosition(lat,lon,principalDetails);

    }
}
