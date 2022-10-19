package webapp.lovelove.member.service;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import webapp.lovelove.member.domain.Member;
import webapp.lovelove.member.domain.memberprofiledomain.Images;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FileHandler {
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

        String path = "photos/" + currentDate;
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
                        originalFileExtension = ".jpg";
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
                String new_file_name = Long.toString(System.nanoTime()) + originalFileExtension;


                Images images = Images.builder().member(member).imgName(multipartFile.getOriginalFilename())
                        .imgUrl(path +File.separator + new_file_name).file_size(multipartFile.getSize()).build();

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
