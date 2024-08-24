package com.datkthor.streaming.Repository;

import com.datkthor.streaming.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video,String> {

//    find bt title
    Optional<Video> findByTitle(String title);
}
