package com.codingjoa.controller;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
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
import org.springframework.transaction.PlatformTransactionManager;
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
	 * @ context.getBeansOfType (can't find bean from parent context - rootContext)
	 * 
	 * https://github.com/spring-projects/spring-framework/issues/15553
	 * Calling ApplicationContext.getBeansOfType(Class) intentionally does not consider the parent hierarchy (see the java doc). 
	 * You can use the BeanFactoryUtils class if you want to search the full hierarchy.
	 * 
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired(required = false)
	private JobRepository jobRepository;
	
	@Autowired(required = false)
	private JobLauncher jobLauncher;
	
	@Autowired(required = false)
	private JobExplorer jobExplorer;
	
	@Autowired(required = false)
	@Qualifier("batchJobA")
	private Job batchJobA;

	@Autowired(required = false)
	@Qualifier("batchJobB")
	private Job batchJobB;
	
	@Autowired
	@Qualifier("batchDataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("batchTransactionManager")
	private PlatformTransactionManager transactionManager;
	
	@GetMapping("/batch")
	public String main() {
		log.info("## batch main");
		return "test/batch";
	}

	@ResponseBody
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() throws Exception {
		log.info("## batch config");
		log.info("\t > simpleBatchConfiguration from beansOfTypeIncludingAncestors = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SimpleBatchConfiguration.class).keySet());
		log.info("\t > jobRepository from beansOfTypeIncludingAncestors = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, JobRepository.class).values());
		log.info("\t ===============================================================================================================================");
		try {
			log.info("\t > looking for bean of SimpleBatchConfiguration...");
			SimpleBatchConfiguration config = context.getBean(SimpleBatchConfiguration.class);
			log.info("\t > transactionManager SimpleBatchConfiguration = {}", config.transactionManager());
			log.info("\t > transactionManager @Qualifier = {}", transactionManager);
			log.info("\t ===============================================================================================================================");
			log.info("\t > jobRepository SimpleBatchConfiguration = {}", config.jobRepository());
			log.info("\t > jobRepository @Autowired = {}", jobRepository);
		} catch (Exception e) {
			log.info("\t > error msg = {}", e.getMessage());
		}
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/default-config")
	public ResponseEntity<Object> defaultConfig() {
		log.info("## defaultConfig");
		log.info("\t > context = {}", context);
		log.info("\t > parent context = {}", context.getParent());
		try {
			log.info("\t > looking for bean of DefaultBatchConfigurer...");
			log.info("\t > configurer by getBeansOfType = {}", context.getBeansOfType(DefaultBatchConfigurer.class));
			log.info("\t > configurer by beansOfTypeIncludingAncestors = {}", 
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DefaultBatchConfigurer.class));
			log.info("\t > configurer by getBean = {}", context.getBean(DefaultBatchConfigurer.class));
		} catch (Exception e) {
			log.info("\t > can't find default configurer", e.getMessage());
		}
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/job-repository")
	public ResponseEntity<Object> jobRepository() {
		log.info("## jobRepository");
		if (jobRepository != null) {
			log.info("\t > jobRepository = {}", jobRepository);
		} else {
			log.info("\t > No jobRepository");
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() {
		log.info("## jobExplorer");
		if (jobExplorer != null) {
			log.info("\t > jobExplorer = {}", jobExplorer);
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
		} else {
			log.info("\t > No jobExplorer");
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-launcher")
	public ResponseEntity<Object> jobLauncher() {
		log.info("## jobLauncher");
		if (jobLauncher != null) {
			log.info("\t > jobLauncher = {}", jobLauncher);
		} else {
			log.info("\t > No jobLauncher");
		}
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
