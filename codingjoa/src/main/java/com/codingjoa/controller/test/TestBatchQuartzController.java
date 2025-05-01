package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.Objects;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@Qualifier("chunkJob")
	@Autowired(required = false)
	private Job chunkJob;
	
	@Autowired(required = false)
	private ItemReader<String> itemReader;

	@Autowired(required = false)
	private ItemProcessor<String, String> itemProcessor;
	
	@Autowired(required = false)
	private ItemWriter<String> itemWriter;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired(required = false)
	private JobScope jobScope;

	@Autowired(required = false)
	private StepScope stepScope;
	
	@GetMapping("/config")
	public ResponseEntity<Object> config() {
		log.info("## config");
		log.info("\t > jobRepository = {}", jobRepository);
		log.info("\t > jobExplorer = {}", jobExplorer);
		log.info("\t > jobLauncher = {}", jobLauncher);
		log.info("\t > jobRegistry = {}", jobRegistry);
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/jobs/{jobName}/run")
	public ResponseEntity<Object> runJob(@PathVariable String jobName, 
			@RequestParam(name = "flow_status", required = false) Boolean flowStatus) throws Exception {
		log.info("## runJob");
		log.info("\t > jobName = {}, flowStatus = {}", jobName, flowStatus);
		//log.info("\t > jobNames from jobExplorer = {}", jobExplorer.getJobNames());
		//log.info("\t > jobNames from jobRegistry = {}", jobRegistry.getJobNames());
		
		Job job = null;
		try {
			job = jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			log.info("\t > search job from jobRegistry, job = {}", job);
		}
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.addString("flowStatus", Objects.toString(flowStatus, null))
				.toJobParameters();
		
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
		log.info("\t > search job from jobRegistry, job = {}", job);
		
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
		log.info("\t > chunckJob bean: {}", chunkJob);
		
		inspect("itemReader", itemReader);
		inspect("itemProcessor", itemProcessor);
		inspect("itemWriter", itemWriter);
		
		ConfigurableListableBeanFactory beanFactory =
				((ConfigurableApplicationContext) applicationContext).getBeanFactory();
		
		String[] scopeNames = beanFactory.getRegisteredScopeNames();
		log.info("\t > scopeNames: {}", Arrays.toString(scopeNames));
		
		log.info("===========================================================================");
		log.info("\t > jobScope = {}", jobScope);
		log.info("\t > stepScope = {}", stepScope);
		
//		Scope stepScope = beanFactory.getRegisteredScope("stepScope");
//		log.info("\t > stepScope: {}", stepScope);
//		log.info("\t > stepScope class: {}", stepScope.getClass());
//		
//		Scope registered = beanFactory.getRegisteredScope("stepScope");
//		log.info("\t > registeredScope instance: {}", registered);
//		log.info("\t > registeredScope class  : {}", registered.getClass().getName());
//
//		boolean hasMyBean = applicationContext.containsBean("stepScope");
//		log.info("\t > containsBean(\"stepScope\"): {}", hasMyBean);
//		if (hasMyBean) {
//			Object mine = applicationContext.getBean("stepScope");
//			log.info("\t > myStepScope bean       : {}", mine);
//			log.info("\t > myStepScope class      : {}", mine.getClass().getName());
//			log.info("\t > registered == mine?    : {}", registered == mine);
//		}

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
			log.info("\t\t • this class: {}", bean.getClass().getSimpleName());
		}
	}

	@GetMapping("/chunk-job/run")
	public ResponseEntity<Object> runChunkJob() throws Exception {
		log.info("## runChunkJob");
		
		Job job = jobRegistry.getJob("chunkJob");
		log.info("\t > search job from jobRegistry, job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	

}
