package com.datkthor.streamming.Request;

import jakarta.persistence.Entity;
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
