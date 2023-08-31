package com.codingjoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.scheduler.service.impl.QuartzServiceImpl;
import com.codingjoa.scheduler.service.impl.SchedulerServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestQuartzController {
	
	/*
	 * > enable in-memory job scheduler
	 * > clustering using database
	 * 
	 * 
	 * 
	 */
	
	@Autowired
	private QuartzServiceImpl quartzService;
	
	@ResponseBody
	@GetMapping("/quartz")
	public ResponseEntity<Object> quartz() {
		log.info("## quartz");
		return ResponseEntity.ok("quartz success");
	}
	
}
