package com.codingjoa.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
	
	@Resource(name = "batchJobA")
	private Job batchJobA;

	@Resource(name = "batchJobB")
	private Job batchJobB;
	
	@GetMapping("/batch")
	public String main() {
		log.info("## batch main");
		return "test/batch";
	}

	@ResponseBody
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() {
		log.info("## bacth config");
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/run/job-a")
	public ResponseEntity<Object> runJobA() throws Exception {
		log.info("## runJobA");
		jobLauncher.run(batchJobA, new JobParameters());
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/run/job-b")
	public ResponseEntity<Object> runJobB() throws Exception {
		log.info("## runJobB");
		jobLauncher.run(batchJobB, new JobParameters());
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-parameters")
	public ResponseEntity<Object> jobParameters() {
		log.info("## jobParameters");
		JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
				.getNextJobParameters(this.batchJobA)
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() {
		log.info("## jobExplorer");
		List<String> batchJobs = jobExplorer.getJobNames();
		if (!batchJobs.isEmpty()) {
			log.info("\t > batch jobs = {}", batchJobs);
			batchJobs.forEach(batchJob -> {
				List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(batchJob, 0, 10);
				log.info("\t    - {}", jobInstances);
			});
		} else {
			log.info("\t > NO batch jobs");
		}
		
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-registry")
	public ResponseEntity<Object> jobRegistry() {
		log.info("## jobRegistry");
		log.info("\t > batch jobs from jobRegistry = {}", jobRegistry.getJobNames());
		return ResponseEntity.ok("success");
	}
	
}