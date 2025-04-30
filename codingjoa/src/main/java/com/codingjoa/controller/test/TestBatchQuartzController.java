package com.codingjoa.controller.test;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/batch-quartz")
@RestController
public class TestBatchQuartzController {
	
	@Autowired(required = false)
	private JobRepository jobRepository;

	@Autowired(required = false)
	private JobExplorer jobExplorer;

	@Autowired(required = false)
	private JobLauncher jobLauncher;
	
	@Autowired(required = false)
	private JobRegistry jobRegistry;
	
	@GetMapping("/config")
	public ResponseEntity<Object> config() {
		log.info("## config");
		log.info("\t > jobRepository = {}", jobRepository);
		log.info("\t > jobExplorer = {}", jobExplorer);
		log.info("\t > jobLauncher = {}", jobLauncher);
		log.info("\t > jobRegistry = {}", jobRegistry);
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/job/{jobName}/run")
	public ResponseEntity<Object> runJob(@PathVariable String jobName) throws Exception {
		log.info("## runJob");
		log.info("\t > jobName = {}", jobName);
		//log.info("\t > jobNames from jobExplorer = {}", jobExplorer.getJobNames());
		//log.info("\t > jobNames from jobRegistry = {}", jobRegistry.getJobNames());
		
		Job job = null;
		try {
			job = jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			log.info("\t > job = {}", job);
		}
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addDate("timestamp", new Date(System.currentTimeMillis()))
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("\t > result: jobId = {}, jobName = {}, exitStatus = {}, jobParameters = {}", 
				jobExecution.getJobId(), jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus(), jobExecution.getJobParameters());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

}
