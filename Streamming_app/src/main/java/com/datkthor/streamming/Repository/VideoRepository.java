package com.datkthor.streamming.Repository;

import com.datkthor.streamming.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video,String> {

//    find bt title
    Optional<Video> findByTitle(String title);
}
