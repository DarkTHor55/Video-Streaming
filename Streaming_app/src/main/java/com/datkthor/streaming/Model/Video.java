package com.datkthor.streaming.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "video_table")
public class Video {
    @Id
    private String videoId;
    private String title;
    private String description;
    private String filepath;
    private String contentType;
}
