package com.datkthor.streaming.Service;

import com.datkthor.streaming.Model.Video;
import com.datkthor.streaming.Request.VideoRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IVideoService {
//    save video
    Video savevideo(final VideoRequest video ,final MultipartFile multipartFile) throws IOException;

//    get all video
    List<Video> getAllVideo();

//    get by id
    Video getVideoById(final String id);
//    get by title
    Video getVideoByTitle(final String title);
//    video proceesing
    String videoProcessing(final String VideoId);

}
