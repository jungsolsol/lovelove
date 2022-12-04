package webapp.lovelove.member.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.dto.ImageDto;
import webapp.lovelove.member.domain.memberprofiledomain.Images;
import webapp.lovelove.member.repository.ImagesRepository;
import webapp.lovelove.member.repository.MemberRepository;

import javax.annotation.PreDestroy;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Component
@AllArgsConstructor
@Service
public class FileHandler {

    private final ImagesRepository imagesRepository;
    private final MemberRepository memberRepository;


    public Images savePost(Images images) {
        imagesRepository.save(images);

        return images;
    }

    public List<Images> parseFileInfo(Member member, List<MultipartFile> files) throws Exception {
        List<Images> fileList = new ArrayList<>();



        //빈파일이 들어올땐 빈것을 반환
        if (files.isEmpty()) {
            return fileList;
        }

        //파일 이름을 변경해서 저장
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = simpleDateFormat.format(new Date());

        String abPath = new File("").getAbsoluteFile() +"/";

        String path = "src/main/resources/static/love/photos/" + currentDate;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) {
                String contentType = multipartFile.getContentType();
                String originalFileExtension;

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if(contentType.contains("image/jpeg")){
                        originalFileExtension = ".jpeg";
                    }
                    else if(contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    }
                    else if(contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    }
                    // 다른 파일 명이면 아무 일 하지 않는다
                    else{
                        break;
                    }
                }

                String new_file_name = member.getMemberProfile().getNickname() + originalFileExtension;


                Images images = Images.builder().member(member).imgName(multipartFile.getOriginalFilename())
                        .imgUrl(path.substring(31) +File.separator + new_file_name).file_size(multipartFile.getSize()).build();

                fileList.add(images);

                String fullpath = abPath + path + File.separator + new_file_name;
//                file = new File(abPath + path + "/" + new_file_name);
                Path path1 = Paths.get(fullpath).toAbsolutePath();
                multipartFile.transferTo(path1.toFile());
            }
        }
        return fileList;
    }

}
