package webapp.lovelove.main.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.coyote.Request;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.main.domain.findMemberDto;
import webapp.lovelove.main.dto.MessageDto;
import webapp.lovelove.main.dto.MessageUpdateDto;
import webapp.lovelove.main.repository.HeartRepository;
import webapp.lovelove.main.service.MessageService;
import webapp.lovelove.member.domain.Heart;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.MemberPosition;
import webapp.lovelove.member.repository.MemberRepository;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    @GetMapping("love/main")
    public void main(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        model.addAttribute("member", principalDetails.getAttribute("name"));

        String email = (String) principalDetails.getAttribute("email");
        Member byEmail = memberRepository.findByEmail(email);
        model.addAttribute("distance", byEmail.getDistance());
        model.addAttribute("nick", byEmail.getMemberProfile().getNickname());
    }

    @PostMapping("love/main")
    public void main_AddingLocation(@RequestParam("lat") String lat, @RequestParam("lon") String lon,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    HttpServletResponse rs) throws IOException, InterruptedException {

        try {
            Double lattoDouble = Double.parseDouble(lat);
            Double lontoDouble = Double.parseDouble(lon);
            memberService.setMemberPosition(lattoDouble, lontoDouble, principalDetails);
            //distance에 따른 탐색 거리 조정

        } catch (Exception e) {

            throw new IOException("Error");
        }
        finally {

            String email = (String) principalDetails.getAttribute("email");
            Member byEmail = memberRepository.findByEmail(email);
            MemberPosition memberPosition = byEmail.getMemberPosition();
            Double distance = byEmail.getDistance();
            //거리 지정이 아직 안되어있으면 기본 거리 3km
            List<findMemberDto> find = memberService.findWomanOrManByPosition(memberPosition.getLat(), memberPosition.getLon(), distance*0.01, principalDetails);
            if (find != null) {

                //JS에 json 형태로 값 전달
                parseJson(principalDetails, rs, find);
            } else {
                //Todo
                //주위에 아무도 없을때
                parseJson(principalDetails, rs, find);

            }

        }
    }

    private void parseJson(PrincipalDetails principalDetails, HttpServletResponse rs, List<findMemberDto> find) throws InterruptedException, IOException {
        JSONObject jso = new JSONObject();
        MultiValueMap<String, String> objects = new LinkedMultiValueMap<>();
        for (findMemberDto findMemberDto : find) {
            objects.add("name", findMemberDto.getName());
            objects.add("nickname", findMemberDto.getNickname());
            objects.add("introduce", findMemberDto.getIntroduce());
            objects.add("Clat", findMemberDto.getPosition().getLat().toString());
            objects.add("Clon", findMemberDto.getPosition().getLon().toString());
            objects.add("images", findMemberDto.getImagesUrl());
            objects.add("myname", principalDetails.getAttribute("name"));

            //하트저장소에서 하트보낸사람 찾아서 지도에 맵핑하기위함
            List<Heart> allBySender = heartRepository.findAllBySender(memberRepository.findByEmail(findMemberDto.getNickname()));
        }

        System.out.println(objects);
        jso.put("name", objects.get("name"));
        jso.put("Clat", objects.get("Clat"));
        jso.put("Clon", objects.get("Clon"));
        jso.put("introduce", objects.get("introduce"));
        jso.put("nickname", objects.get("nickname"));
        jso.put("images", objects.get("images"));
        jso.put("myname", objects.get("myname"));
        Thread.sleep(100);

        rs.setContentType("text/html;charset=utf-8");
        PrintWriter out = rs.getWriter();
        out.print(jso.toString());
    }




    @PostMapping("love/main/distance")
    public RedirectView settingDistance(@RequestParam("distance") String distance, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Double dt = Double.parseDouble(distance);
        String email = (String) principalDetails.getAttribute("email");
        Member m = memberRepository.findByEmail(email);
        m.setDistance(dt);
        memberRepository.saveAndFlush(m);
        return new RedirectView("/love/main");
    }
}
