package com.datkthor.streamming.Service.Impl;

import ch.qos.logback.core.util.StringUtil;
import com.datkthor.streamming.Model.Video;
import com.datkthor.streamming.Request.VideoRequest;
import com.datkthor.streamming.Service.IVideoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class VideoServiceImpl implements IVideoService {
    @Value("${files.video}")
    String DIR;

    @PostConstruct
    public void init() {
        File file = new File(DIR);
        if(!file.exists()){
            file.mkdir();
            log.info("Folder created  at: " + file.getAbsolutePath());

        }else {
            log.info("Folder already  created at : " + file.getAbsolutePath());

        }
    }
    @Override
    public Video savevideo(VideoRequest video, MultipartFile multipartFile) {




//        get original file name
        try {
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            InputStream contentStream =multipartFile.getInputStream();// it is use to read the data od multipart file and store that data in contentStream

//            file path
           String cleanFileName= StringUtils.cleanPath(fileName);  // it will clean path like remove extra . /  etc

//       folder path create
           String cleanFolder=StringUtils.cleanPath(DIR);

//      folder path with filename
           Path path= Paths.get(cleanFolder,cleanFileName);// it will return path by combining folder and file name
            System.out.println(path);

//         copy file to the folder
            Files.copy(contentStream,path, StandardCopyOption.REPLACE_EXISTING)  // first perameter from where we copying file and second perameter from where we copied file third handle if files already exites like replace it  or overwrite

//        video metadata

//         metadata save
            Video video1=Video.builder().videoId(UUID.randomUUID().toString()).title(video.getTitle()).description(video.getDescription()).build();
        }catch (IOException e){
            System.out.println(e);
        }

//
        return null;
    }

    @Override
    public List<Video> getAllVideo() {
        return null;
    }

    @Override
    public Video getVideoById(String id) {
        return null;
    }

    @Override
    public Video getVideoByTitle(String title) {
        return null;
    }
}
