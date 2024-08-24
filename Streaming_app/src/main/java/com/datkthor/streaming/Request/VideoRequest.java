package com.datkthor.streaming.Request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VideoRequest {
    private String title;
    private String description;

}
