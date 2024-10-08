package com.datkthor.streaming.Response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VideoResponse {
    private boolean status;
    private String message;
}
