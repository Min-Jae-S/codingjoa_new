package com.codingjoa.controller.test;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestBatchController {
	
	/*
	 * @ ApplicationContext.getBeansOfType -> BeanFactoryUtils.beansOfTypeIncludingAncestors
	 * @ context.getBeansOfType (can't find bean from parent context - rootContext)
	 * 
	 * # https://github.com/spring-projects/spring-framework/issues/15553
	 * Calling ApplicationContext.getBeansOfType(Class) intentionally does not consider the parent hierarchy (see the java doc). 
	 * You can use the BeanFactoryUtils class if you want to search the full hierarchy.
	 * 
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobLauncher jobLauncher;
	
	@Qualifier("batchJobA")
	@Autowired(required = false)
	private Job batchJobA;

	@Qualifier("batchJobB")
	@Autowired(required = false)
	private Job batchJobB;
	
	@Autowired(required = false)
	private JobBuilderFactory jobBuilders;

	@Autowired(required = false)
	private StepBuilderFactory stepBuilders;
	
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() throws Exception {
		log.info("## batch config");
		//log.info("\t > context = {}", context);
		//log.info("\t > parent context = {}", context.getParent());
		
		try {
			log.info("\t > finding BatchConfigurer...");
			log.info("\t > context.getBeansOfType(BatchConfigurer.class) = {}", context.getBeansOfType(BatchConfigurer.class)); // only current context
			log.info("\t > context.getParent().getBeansOfType(BatchConfigurer.class) = {}", context.getParent().getBeansOfType(BatchConfigurer.class));
			log.info("\t > BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DefaultBatchConfigurer.class) = {}", 
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DefaultBatchConfigurer.class));
			log.info("\t > context.getBean(DefaultBatchConfigurer.class) = {}", context.getBean(DefaultBatchConfigurer.class));
			log.info("\t > context.getBean(BatchConfigurer.class) = {}", context.getBean(BatchConfigurer.class));
		} catch (Exception e) {
			log.info("\t > can't find batch configurer", e.getMessage());
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/batch/builders")
	public ResponseEntity<Object> builders() {
		log.info("## builders");
		log.info("\t > jobBuilderFactory = {}", jobBuilders);
		log.info("\t > stepBuilderFactory = {}", stepBuilders);
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/batch/job-repository")
	public ResponseEntity<Object> jobRepository() throws Exception {
		log.info("## jobRepository");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobRepository from BatchConfigurer = {}", batchConfigurer.getJobRepository());
		log.info("\t > jobRepository from @Autowired = {}", jobRepository);
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() throws Exception {
		log.info("## jobExplorer");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobExplorer from BatchConfigurer = {}", batchConfigurer.getJobExplorer());
		log.info("\t > jobExplorer from @Autowired = {}", jobExplorer);
		
		if (jobExplorer != null) {
			List<String> jobNames = jobExplorer.getJobNames();
			if (!jobNames.isEmpty()) {
				log.info("\t > batch jobs:");
				jobNames.forEach(jobName -> {
					List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, 10);
					log.info("\t\t - {}", jobInstances);
				});
			} else {
				log.info("\t > no batch jobs");
			}
		}
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/batch/job-launcher")
	public ResponseEntity<Object> jobLauncher() throws Exception {
		log.info("## jobLauncher");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobLauncher from BatchConfigurer = {}", batchConfigurer.getJobLauncher());
		log.info("\t > jobLauncher from @Autowired = {}", jobLauncher);
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/batch/job-parameters")
	public ResponseEntity<Object> jobParameters() {
		log.info("## jobParameters");
		JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
				.getNextJobParameters(this.batchJobA)
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/batch/run/job-a")
	public ResponseEntity<Object> runJobA() throws Exception {
		log.info("## runJobA");
		jobLauncher.run(batchJobA, new JobParameters());
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/batch/run/job-b")
	public ResponseEntity<Object> runJobB() throws Exception {
		log.info("## runJobB");
		jobLauncher.run(batchJobB, new JobParameters());
		return ResponseEntity.ok(SuccessResponse.create());
	}
}
