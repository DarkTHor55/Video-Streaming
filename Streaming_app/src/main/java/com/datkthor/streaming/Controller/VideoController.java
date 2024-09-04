package com.datkthor.streaming.Controller;

import com.datkthor.streaming.Configuration.AppConstants;
import com.datkthor.streaming.Model.Video;
import com.datkthor.streaming.Request.VideoRequest;
import com.datkthor.streaming.Response.VideoResponse;
import com.datkthor.streaming.Service.Impl.VideoServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.RemoteRef;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoServiceImpl videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoResponse> create(final @RequestParam("file") MultipartFile file, final @RequestParam("title") String title, final @RequestParam("description") String description) {
        VideoRequest request = VideoRequest.builder().title(title).description(description).build();
        Video video = videoService.savevideo(request, file);
        if (!Objects.isNull(video))
            return ResponseEntity.ok(VideoResponse.builder().status(true).message("Video saved successfully").build());
        return ResponseEntity.badRequest().body(VideoResponse.builder().status(false).message("Failed to save video").build());
    }

    //    stream video
    @GetMapping("/stream/{id}")
    public ResponseEntity<Resource> stream(final @PathVariable("id") String id) {
        Video video = videoService.getVideoById(id);
        String path = video.getFilepath().toString();
        String contentType = video.getContentType().toString();
        Resource resource = new FileSystemResource(path);
        if (Objects.isNull(contentType)) contentType = "application/octet-stream";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    //    get all video list
    @GetMapping
    public ResponseEntity<List<Video>> getAll() {
        return ResponseEntity.ok(videoService.getAllVideo());
    }

    //    stream video in chunks
    @GetMapping("/stream/range/{id}")
    public ResponseEntity<Resource> VideoStreamInChunk(final @PathVariable("id") String id, final @RequestHeader(value = "range", required = false) String range) {
        Video video = videoService.getVideoById(id);
        Path path = Paths.get(video.getFilepath());
        Resource resource = new FileSystemResource(path);
        String contentType = video.getContentType();
        if (Objects.isNull(contentType)) contentType = "application/octet-stream";

//        length of file
        long fileLength = path.toFile().length();
//       if range header is null
        if (Objects.isNull(range))
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
//        if range given
        long rangeSt;
        long rangeEd;
//        assume  range:bytes=1000-10000  (we get range in that form )
//        split range in array and assgin to rangest and rangeed
        String[] ranges = range.replace("bytes=", "").split("-");// now we have [st,ed]
        rangeSt = Long.parseLong(ranges[0]);

////        check if we dont have rangeend then we set file length as a rangeed and it will return full video
//        if (ranges.length > 1) {
//            rangeEd = Long.parseLong(ranges[1]);
//        } else {
//            rangeEd = fileLength - 1;
//        }
////        if given range is bigger then last range then file length then set rangeed to filelength
//        if (rangeEd > fileLength - 1) {
//            rangeEd = fileLength - 1;
//        }



//         set rangeEd of 1 mb
        rangeEd = rangeSt+ AppConstants.CHUNKS-1;
        if (rangeEd >= fileLength) {
            rangeEd = fileLength-1;
        }
        System.out.println(rangeSt +".............................................................................."+rangeEd);
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(path);
//            skip bytes before rangestart
            inputStream.skip(rangeSt);

            //      full length of content which we wanna return
            long contentLength = rangeEd - rangeSt + 1;

            byte[] data = new byte[(int)contentLength];
            int read=inputStream.read(data, 0, (int)data.length );

            System.out.println("read (number of bytes " + read);

//         create Header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Range", "bytes " + rangeSt + "-" + rangeEd + "/" + fileLength);
            // it will ensure that no cache will store and in each call new content willfetch
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");

            headers.add("Expires", "0");
            headers.add("X-Content-Type-Options", "nosniff");
//        how much data send to client by header
            headers.setContentLength(contentLength);


            return ResponseEntity.
                    status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data))
                    ;

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
}
