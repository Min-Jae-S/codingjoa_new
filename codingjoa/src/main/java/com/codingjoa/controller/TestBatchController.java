package com.codingjoa.controller;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
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
	
	/*
	 * @ ApplicationContext.getBeansOfType -> BeanFactoryUtils.beansOfTypeIncludingAncestors
	 * 
	 * https://github.com/spring-projects/spring-framework/issues/15553
	 * Calling ApplicationContext.getBeansOfType(Class) intentionally does not consider the parent hierarchy (see the java doc). 
	 * You can use the BeanFactoryUtils class if you want to search the full hierarchy.
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired(required = false)
	@Qualifier("batchJobA")
	private Job batchJobA;

	@Autowired(required = false)
	@Qualifier("batchJobB")
	private Job batchJobB;
	
	@GetMapping("/batch")
	public String main() {
		log.info("## batch main");
		return "test/batch";
	}

	@ResponseBody
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() throws Exception {
		log.info("## batch config");
		log.info("\t > simpleBatchConfiguration = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SimpleBatchConfiguration.class).keySet());
		log.info("\t > jobRepository = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, JobRepository.class).values());
		log.info("\t =========================================================================================");
		SimpleBatchConfiguration simpleBatchConfiguration = context.getBean(SimpleBatchConfiguration.class);
		log.info("\t > batch transactionManager = {}", simpleBatchConfiguration.transactionManager());
		log.info("\t > batch jobRepository = {}", simpleBatchConfiguration.jobRepository());
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/job-repository")
	public ResponseEntity<Object> jobRepository() {
		log.info("## jobRepository");
		log.info("\t > jobRepository = {}", jobRepository);
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() {
		log.info("## jobExplorer");
		List<String> jobNames = jobExplorer.getJobNames();
		if (!jobNames.isEmpty()) {
			log.info("\t > batch jobs = {}", jobNames);
			jobNames.forEach(jobName -> {
				List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, 10);
				log.info("\t     - {}", jobInstances);
//				List<Long> instanceIds = jobInstances.stream()
//						.map(JobInstance -> JobInstance.getInstanceId())
//						.collect(Collectors.toList());
//				log.info("\t     - {} = {}", jobName, instanceIds);
			});
		} else {
			log.info("\t > NO batch jobs");
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-launcher")
	public ResponseEntity<Object> jobLauncher() {
		log.info("## jobLauncher");
		log.info("\t > jobLauncher = {}", jobLauncher);
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
	
}
