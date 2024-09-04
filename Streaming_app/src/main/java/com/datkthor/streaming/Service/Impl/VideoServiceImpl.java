package com.datkthor.streaming.Service.Impl;

import com.datkthor.streaming.Model.Video;
import com.datkthor.streaming.Repository.VideoRepository;
import com.datkthor.streaming.Request.VideoRequest;
import com.datkthor.streaming.Service.IVideoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class VideoServiceImpl implements IVideoService {
    private final VideoRepository videoRepository;
    @Value("${files.video}")
    String DIR;

    @Value("${files.video.hsl}")
    String HSL_DIR;
    @PostConstruct
    public void init() {
        File file = new File(DIR);
        if(!file.exists()){
            file.mkdir();
            log.info("Folder created  at: " + file.getAbsolutePath());

        }else {
            log.info("Folder already  created at : " + file.getAbsolutePath());

        }
//        File file1 = new File(HSL_DIR);
//        if(!file1.exists()){
//            file1.mkdir();
//            log.info("Folder created  at: " + file1.getAbsolutePath());
//        }
//        else {
//            log.info("Folder already  created at : " + file1.getAbsolutePath());
//        }
//         we can use createdirectories method also
        try {
            Files.createDirectories(Paths.get(HSL_DIR));
        }catch (IOException e){
            System.out.println("Already exits");
        }
    }
    @Override
    public Video savevideo(final VideoRequest v, final MultipartFile multipartFile) {




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
            Files.copy(contentStream,path, StandardCopyOption.REPLACE_EXISTING);  // first perameter from where we copying file and second perameter from where we copied file third handle if files already exites like replace it  or overwrite

//        v metadata


//         metadata save
            Video video=Video.builder().videoId(UUID.randomUUID().toString()).title(v.getTitle()).description(v.getDescription()).contentType(contentType).filepath(path.toString()).build();
//            save
            Video video1 = videoRepository.save(video);
            videoProcessing("031c5ff6-eb11-4f17-abea-7fccbdf9191a");
return video1;
        }catch (IOException e){
            System.out.println(e);
            return null;

        }
    }

    @Override
    public List<Video> getAllVideo() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(()->new RuntimeException("Video Not Found"));
    }

    @Override
    public Video getVideoByTitle(String title) {
        return null;
    }

    @Override
    public String videoProcessing(String id) {
        Video video = videoRepository.findById(id).orElseThrow(null);
        String filePath = video.getFilepath();

        //path where to store data:
        Path videoPath = Paths.get(filePath);


//        String output360=HSL_DIR+id+"/360p";
//        String output720=HSL_DIR+id+"/720p";
//        String output1080=HSL_DIR+id+"/1080p";

        try {
//            Files.createDirectories(Paths.get(output360));
//            Files.createDirectories(Paths.get(output720));
//            Files.createDirectories(Paths.get(output1080));

            Path outputPath = Paths.get(HSL_DIR, id);

            Files.createDirectories(outputPath);


            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
                    videoPath, outputPath, outputPath
            );

            System.out.println(ffmpegCmd);
            //file this command
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }

            return id;

        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
