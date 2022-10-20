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
import java.io.PrintWriter;
import java.util.HashMap;
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
    public void main_AddingLocation(@RequestParam("lat") String lat, @RequestParam("lon") String lon,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    HttpServletResponse rs) {

        try {
            Double lattoDouble = Double.parseDouble(lat);
            Double lontoDouble = Double.parseDouble(lon);
            memberService.setMemberPosition(lattoDouble, lontoDouble, principalDetails);

            //위치에 따른 타겟 검색, 설정
            List<findMemberDto> find = memberService.findWomanOrManByPosition(lattoDouble, lontoDouble, 0.3, principalDetails);

            for (findMemberDto findMemberDtos : find) {
                System.out.println(findMemberDtos.getName());
                System.out.println(findMemberDtos.getNickname());
            }

            //js에 값 반환
            String lat1 = find.get(0).getPosition().getLat().toString();
            String lon1 = find.get(0).getPosition().getLon().toString();

            Map<String, Object> mv = new HashMap<>();
            JSONObject jso = new JSONObject();
            jso.put("name", find.get(0).getName());
            jso.put("Clat", lat1);
            jso.put("Clon", lon1);

            rs.setContentType("text/html;charset=utf-8");
            PrintWriter out = rs.getWriter();
            out.print(jso.toString());

        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
