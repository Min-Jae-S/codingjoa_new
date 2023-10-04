package com.codingjoa.controller;

import java.util.List;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestBatchController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@GetMapping("/batch")
	public String main() {
		log.info("## batch main");
		return "test/batch";
	}

	@ResponseBody
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() {
		log.info("## config");
		return ResponseEntity.ok("config success");
	}

	@ResponseBody
	@GetMapping("/batch/job-launcher")
	public ResponseEntity<Object> jobLauncher() {
		log.info("## jobLauncher");
		log.info("\t > {}", jobLauncher);
		return ResponseEntity.ok("jobLauncher success");
	}
	
	@ResponseBody
	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() {
		log.info("## jobExplorer");
		
		List<String> batchJobs = jobExplorer.getJobNames();
		log.info("\t > batch jobs from jobExplorer = {}", batchJobs);

		batchJobs.forEach(batchJob -> 
			log.info("\t > jobInstance = {}", jobExplorer.findJobInstancesByJobName(batchJob, 0, 10))
		);
		
		return ResponseEntity.ok("jobExplorer success");
	}

	@ResponseBody
	@GetMapping("/batch/job-registry")
	public ResponseEntity<Object> jobRegistry() {
		log.info("## jobRegistry");
		log.info("\t > batch jobs from jobRegistry = {}", jobRegistry.getJobNames());
		return ResponseEntity.ok("jobRegistry success");
	}
	
}
