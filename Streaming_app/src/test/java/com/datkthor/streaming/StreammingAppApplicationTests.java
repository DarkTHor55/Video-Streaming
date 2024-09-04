package com.datkthor.streaming;

import com.datkthor.streaming.Service.Impl.VideoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StreammingAppApplicationTests {
@Autowired
	VideoServiceImpl videoService;
	@Test
	void contextLoads() throws InterruptedException{
		videoService.videoProcessing("031c5ff6-eb11-4f17-abea-7fccbdf9191a");
	}

}
