package com.androidmage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedditController {

	@Autowired
	public RedditProducer redditProducer;

	@GetMapping("/test")
	public void greeting() {
		redditProducer.produceRedditData();
	}

	@Scheduled(cron = "0 */5 * * * *")
	public void execute() {
		redditProducer.produceRedditData();
	}
}
