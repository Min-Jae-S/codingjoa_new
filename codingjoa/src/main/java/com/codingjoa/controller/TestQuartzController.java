package com.codingjoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.scheduler.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestQuartzController {
	
	/*
	 * > enable in-memory job scheduler
	 * > clustering using database
	 * 
	 */
	
	@Autowired
	private QuartzService quartzService;
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## TestQuartz main");
		return "test/quartz";
	}
	
	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> start() {
		log.info("## start");
		return ResponseEntity.ok("start success");
	}
	
}
