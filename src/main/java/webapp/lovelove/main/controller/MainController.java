package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.coyote.Request;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/love/main")
    public void main(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        memberService.join(form, principalDetails);
//        memberService.join();
        model.addAttribute("member", principalDetails.getAttribute("name"));
    }

    @PostMapping("/love/main")
    public void main_AddingLocation(@RequestParam("lat") String lat, @RequestParam("lon") String  lon,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        Double lattoDouble = Double.parseDouble(lat);
        Double lontoDouble = Double.parseDouble(lon);
        memberService.setMemberPosition(lattoDouble, lontoDouble, principalDetails);
//        List<Member> target = memberService.findWomanOrManByPosition(lattoDouble, lontoDouble, 0.5);
        //위치에 따른 타겟 검색, 설정
        List<findMemberDto> find = (List<findMemberDto>) memberService.findWomanOrManByPosition(lattoDouble,lontoDouble, 300.0);

        System.out.println(find.get(0).getNickname());

        model.addAttribute("target", find);
        

    }
}
