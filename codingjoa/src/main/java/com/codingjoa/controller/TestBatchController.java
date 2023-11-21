package com.codingjoa.controller;

import javax.annotation.Resource;

import org.springframework.batch.core.Job;
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
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired(required = false)
	@Qualifier("batchJobA")
	private Job batchJobA;

	@Autowired(required = false)
	@Qualifier("batchJobB")
	private Job batchJobB;
	
	@Resource(name = "batchTransactionManager")
	private PlatformTransactionManager transactionManager;
	
	@Autowired(required = false)
	@Qualifier("jobBuilders")
	private JobBuilderFactory jobBuilders;

	@Autowired(required = false)
	@Qualifier("stepBuilders")
	private StepBuilderFactory stepBuilders;
	
	@GetMapping("/batch")
	public String main() {
		log.info("## batch main");
		return "test/batch";
	}

	@ResponseBody
	@GetMapping("/batch/config")
	public ResponseEntity<Object> config() throws Exception {
		log.info("## batch config");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > configurer = {}", batchConfigurer);
		log.info("\t > transactionManager from BatchConfigurer = {}", batchConfigurer.getTransactionManager());
		log.info("\t > transactionManager from @Resource = {}", transactionManager);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/default-config")
	public ResponseEntity<Object> defaultConfig() {
		log.info("\t > context = {}", context);
		log.info("\t > parent context = {}", context.getParent());
		try {
			log.info("\t > finding DefaultBatchConfigurer...");
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
	@GetMapping("/batch/builders")
	public ResponseEntity<Object> builders() {
		log.info("## builders");
		log.info("\t > jobBuilderFactory = {}", jobBuilders);
		log.info("\t > stepBuilderFactory = {}", stepBuilders);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/batch/job-repository")
	public ResponseEntity<Object> jobRepository() throws Exception {
		log.info("## jobRepository");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobRepository from BatchConfigurer = {}", batchConfigurer.getJobRepository());
		log.info("\t > jobRepository from @Autowired = {}", jobRepository);
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-explorer")
	public ResponseEntity<Object> jobExplorer() throws Exception {
		log.info("## jobExplorer");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobExplorer from BatchConfigurer = {}", batchConfigurer.getJobExplorer());
		log.info("\t > jobExplorer from @Autowired = {}", jobExplorer);
//		if (jobExplorer != null) {
//			log.info("\t > jobExplorer = {}", jobExplorer);
//			List<String> jobNames = jobExplorer.getJobNames();
//			if (!jobNames.isEmpty()) {
//				log.info("\t > batch jobs = {}", jobNames);
//				jobNames.forEach(jobName -> {
//					List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, 10);
//					log.info("\t     - {}", jobInstances);
//				List<Long> instanceIds = jobInstances.stream()
//						.map(JobInstance -> JobInstance.getInstanceId())
//						.collect(Collectors.toList());
//				log.info("\t     - {} = {}", jobName, instanceIds);
//				});
//			} else {
//				log.info("\t > NO batch jobs");
//			}
//		} else {
//			log.info("\t > No jobExplorer");
//		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/job-launcher")
	public ResponseEntity<Object> jobLauncher() throws Exception {
		log.info("## jobLauncher");
		BatchConfigurer batchConfigurer = context.getBean(BatchConfigurer.class);
		log.info("\t > jobLauncher from BatchConfigurer = {}", batchConfigurer.getJobLauncher());
		log.info("\t > jobLauncher from @Autowired = {}", jobLauncher);
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
//		log.info("\t > batchJobA = {}", batchJobA);
		jobLauncher.run(batchJobA, new JobParameters());
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/batch/run/job-b")
	public ResponseEntity<Object> runJobB() throws Exception {
		log.info("## runJobB");
//		log.info("\t > batchJobB = {}", batchJobB);
		jobLauncher.run(batchJobB, new JobParameters());
		return ResponseEntity.ok("success");
	}
	
}
