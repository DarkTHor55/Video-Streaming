package com.datkthor.streamming.Service;

import com.datkthor.streamming.Model.Video;
import com.datkthor.streamming.Request.VideoRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IVideoService {
//    save video
    Video savevideo(VideoRequest video , MultipartFile multipartFile) throws IOException;

//    get all video
    List<Video> getAllVideo();

//    get by id
    Video getVideoById(String id);
//    get by title
    Video getVideoByTitle(String title);

}
