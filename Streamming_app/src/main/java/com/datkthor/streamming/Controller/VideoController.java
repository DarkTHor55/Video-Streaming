package com.datkthor.streamming.Controller;

import com.datkthor.streamming.Model.Video;
import com.datkthor.streamming.Request.VideoRequest;
import com.datkthor.streamming.Response.VideoResponse;
import com.datkthor.streamming.Service.Impl.VideoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoServiceImpl videoService;
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoResponse> create( final @RequestParam("file")MultipartFile file, final @RequestParam("title") String title, final @RequestParam("description") String description){
        VideoRequest request = VideoRequest.builder().title(title).description(description).build();
        Video video =videoService.savevideo(request,file);
        if(!Objects.isNull(video))return ResponseEntity.ok(VideoResponse.builder().status(true).message("Video saved successfully").build());
        return ResponseEntity.badRequest().body(VideoResponse.builder().status(false).message("Failed to save video").build());
    }

}
