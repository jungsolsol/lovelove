package webapp.lovelove.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import webapp.lovelove.auth.PrincipalDetails;
import webapp.lovelove.member.domain.dto.MemberCreateDto;
import webapp.lovelove.member.domain.dto.MemberCreateDto2;
import webapp.lovelove.member.domain.dto.MemberUpdateDto;
import webapp.lovelove.member.domain.memberprofiledomain.*;
import webapp.lovelove.member.domain.vo.MemberVo;
import webapp.lovelove.member.service.FileHandler;
import webapp.lovelove.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


//    @GetMapping("/member/profile")
//    public String createProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
//
//        model.addAttribute("member", principalDetails.getAttribute("name"));
//        model.addAttribute("form", new MemberCreateDto2());
//        return "member/profile";
//    }
    @GetMapping("/member/profile")
    public ModelAndView createProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("member", principalDetails.getAttribute("name"));
        mv.addObject("form", new MemberCreateDto2());
        mv.setViewName("member/profile");
        return mv;
    }



//    @PostMapping("/member/profile")
//    public String createMember(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid MemberCreateDto form,
//                               BindingResult result, Model model,
//                               @Valid @RequestParam("file") List<MultipartFile> files) {
//        if (result.hasErrors()) {
//            return "/member/profile";
//        }
//
//        memberService.join(form, principalDetails, files);
////        memberService.find();
////        model.addAttribute("member", principalDetails.getAttribute("name"));
//
//        return "redirect:/love/main";
//    }

//    @PostMapping("/member/profile")
//    public String createMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
//                               @ModelAttribute("filePost") MemberCreateDto2 memberCreateDto2) {
//
//
//        memberService.join(memberCreateDto2, principalDetails);
//        int age = memberCreateDto2.getAge();
//        System.out.println(age);
//        int height = memberCreateDto2.getHeight();
//        System.out.println(height);
//
//        return "redirect:/love/main";
//    }


    @PostMapping(value = "/member/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                               @RequestParam(value = "age") int age,
                               @RequestParam(value = "sex") Sex sex,
                               @RequestParam(value = "height") int height,
                               @RequestParam(value = "introduce") String introduce,
                               @RequestParam(value = "religion") Religion religion,
                               @RequestParam(value = "have_smoking") have_Smoking have_smoking,
                               @RequestParam(value = "alcohol") Alcohol alcohol,
                               @RequestParam(value = "nickname") String nickname,
                               @RequestParam(value = "job") String job,
                               @RequestParam(value = "education") Education education,
                               @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {

        MemberCreateDto memberCreateDto = new MemberCreateDto();
        memberCreateDto.setAge(age);
        memberCreateDto.setSex(sex);
        memberCreateDto.setEducation(education);
        memberCreateDto.setAlcohol(alcohol);
        memberCreateDto.setHeight(height);
        memberCreateDto.setIntroduce(introduce);
        memberCreateDto.setReligion(religion);
        memberCreateDto.setHave_smoking(have_smoking);
        memberCreateDto.setNickname(nickname);
        memberCreateDto.setJob(job);
        memberService.join(memberCreateDto,files,principalDetails);
        return "redirect:/love/main";
    }

}
