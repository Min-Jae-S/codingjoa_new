package com.codingjoa.controller.test;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.entity.User;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"unused", "rawtypes"})
@Slf4j
@RequestMapping("/test/batch-quartz")
@RestController
public class TestBatchQuartzController {
	
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Qualifier("itemReader1")
	@Autowired(required = false)
	private ItemReader itemReader1;

	@Qualifier("itemReader2")
	@Autowired(required = false)
	private ItemReader itemReader2;

	@Qualifier("itemProcessor")
	@Autowired(required = false)
	private ItemProcessor itemProcessor;
	
	@Qualifier("itemWriter")
	@Autowired(required = false)
	private ItemWriter itemWriter;

	@Autowired
	private JobScope jobScope;

	@Autowired
	private StepScope stepScope;
	
	@Qualifier("myBatisItemReader")
	@Autowired(required = false)
	private ItemReader myBatisItemReader;

	@Qualifier("myBatisItemProcessor")
	@Autowired(required = false)
	private ItemProcessor myBatisItemProcessor;
	
	@Qualifier("myBatisItemWriter")
	@Autowired(required = false)
	private ItemWriter myBatisItemWriter;

	@Qualifier("boardImageCleanupJob")
	@Autowired(required = false)
	private Job boardImageCleanupJob;
	
	@Qualifier("boardImageItemReader")
	@Autowired(required = false)
	private ItemReader boardImageItemReader;
	
	@Qualifier("compositeBoardImageItemWriter")
	@Autowired(required = false)
	private ItemWriter compositeBoardImageItemWriter;
	
	@Value("${upload.dir.board.image}")
	private String boardImageDir;
	
	@GetMapping("/config")
	public ResponseEntity<Object> config() {
		log.info("## config");
		log.info("\t > jobRepository = {}", jobRepository);
		log.info("\t > jobExplorer = {}", jobExplorer);
		log.info("\t > jobLauncher = {}", jobLauncher);
		log.info("\t > jobRegistry = {}", jobRegistry);
		log.info("\t > registered jobs = {}", jobRegistry.getJobNames());
		//log.info("\t > jobNames from jobExplorer = {}", jobExplorer.getJobNames());
		//log.info("\t > jobNames from jobRegistry = {}", jobRegistry.getJobNames());
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/jobs/{jobName}/run")
	public ResponseEntity<Object> runJob(@PathVariable String jobName, 
			@RequestParam(required = false) Boolean flowStatus) throws Exception {
		log.info("## runJob");
		log.info("\t > jobName = {}, flowStatus = {}", jobName, flowStatus);
		
		Job job = null;
		try {
			job = jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			log.info("\t > found job = {}", job);
		}
		
		JobParametersBuilder builder = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
		if ("flowJob".equals(jobName)) {
			builder.addString("flowStatus", Objects.toString(flowStatus, null));
		}
		
		JobParameters jobParameters = builder.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
//		JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
//		log.info("## result: {}", jobExecution.getExitStatus().getExitDescription());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/tasklet-job/run")
	public ResponseEntity<Object> runTaskletJob() throws Exception {
		log.info("## runTaskletJob");
		
		Job job = jobRegistry.getJob("taskletJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/chunk-job/config")
	public ResponseEntity<Object> configChunkJob() throws Exception {
		log.info("## configChunkJob");
		log.info("\t > job: {}", jobRegistry.getJob("chunkJob1"));
		log.info("\t > job: {}", jobRegistry.getJob("chunkJob2"));
		inspect("itemReader1", itemReader1);
		inspect("itemReader2", itemReader2);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/chunk-job/run")
	public ResponseEntity<Object> runChunkJob(@RequestParam(required = false) boolean useStepScope, 
			@RequestParam(name = "lastNames", required = false) String lastNamesStr) throws Exception {
		log.info("## runChunkJob");
		log.info("\t > useStepScope = {}, lastNamesStr = {}", useStepScope, lastNamesStr);
		
		Job job = useStepScope ? jobRegistry.getJob("chunkJob1") : jobRegistry.getJob("chunkJob2");
		log.info("\t > found job = {}", job);

		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.addString("lastNamesStr", lastNamesStr)
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## {} result: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/mybatis-job/run")
	public ResponseEntity<Object> runMyBatisJob() throws Exception {
		log.info("## runMyBatisJob");
		inspect("myBatisItemReader", myBatisItemReader);
		inspect("myBatisItemWriter", myBatisItemWriter);
		
		Job job = jobRegistry.getJob("myBatisJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/board-image-cleanup-job/config")
	public ResponseEntity<Object> configBoardImageCleanupJob() throws Exception {
		log.info("## configBoardImageCleanupJob");
		log.info("\t > job: {}", jobRegistry.getJob("boardImageCleanupJob"));
		
		inspect("boardImageItemReader", boardImageItemReader);
		inspect("compositeBoardImageItemWriter", compositeBoardImageItemWriter);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/board-image-cleanup-job/run")
	public ResponseEntity<Object> runBoardImageCleanupJob() throws Exception {
		log.info("## runBoardImageCleanupJob");
		
		Job job = jobRegistry.getJob("boardImageCleanupJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		//JobExecution jobExecution = jobLauncher.run(boardImagesCleanupJob, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/dummy-images-job/run")
	public ResponseEntity<Object> runDummyImagesJob() throws Exception {
		log.info("## runDummyImagesJob");
		
		Job job = jobRegistry.getJob("dummyImagesJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.addString("boardImageDir", boardImageDir)
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	private void inspect(String beanName, Object bean) {
		if (bean == null) {
			log.info("\t > {}: null", beanName);
			return;
		}
		
		boolean isProxy = AopUtils.isAopProxy(bean);
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		
		log.info("\t > {}", beanName);
		log.info("\t\t • proxy: {}", isProxy);
		
		if (isProxy) {
			log.info("\t\t • proxy class: {}", bean.getClass().getName());
			log.info("\t\t • proxy type: {}", AopUtils.isJdkDynamicProxy(bean) ? "JDK Dynamic Proxy" : "CGLIB Proxy");
			log.info("\t\t • target class: {}", targetClass.getName());
		} else {
			log.info("\t\t • this class: {}", bean);
		}
	}


}
