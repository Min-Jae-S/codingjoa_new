package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
	
	@Qualifier("chunkJob1")
	@Autowired(required = false)
	private Job chunkJob1;

	@Qualifier("chunkJob2")
	@Autowired(required = false)
	private Job chunkJob2;
	
	@Qualifier("itemReader1")
	@Autowired(required = false)
	private ItemReader<String> itemReader1;

	@Qualifier("itemReader2")
	@Autowired(required = false)
	private ItemReader<String> itemReader2;

	@Autowired(required = false)
	private ItemProcessor<String, String> itemProcessor;
	
	@Qualifier("itemWriter1")
	@Autowired(required = false)
	private ItemWriter<String> itemWriter1;

	@Qualifier("itemWriter2")
	@Autowired(required = false)
	private ItemWriter<String> itemWriter2;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
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
			@RequestParam(required = false) Boolean flowStatus) throws Exception {
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
			log.info("\t > found job = {}", job);
		}
		
		JobParametersBuilder builder = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
		if ("flowJob".equals(jobName)) {
			builder.addString("flowStatus", Objects.toString(flowStatus, null));
		}
		
		JobParameters jobParameters = builder.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## {} result: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus());
		
//		JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
//		log.info("## {} result: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitDescription());
		
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
		log.info("## {} result: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/chunk-job/config")
	public ResponseEntity<Object> configChunkJob() throws Exception {
		log.info("## configChunkJob");
		log.info("\t > chunckJob1 bean: {}", chunkJob1);
		inspect("itemReader1", itemReader1);
		inspect("itemProcessor", itemProcessor);
		inspect("itemWriter1", itemWriter1);
		
		log.info("-------------------------------------------------------------------------------------------------------------------------------");
		log.info("\t > chunckJob2 bean: {}", chunkJob2);
		inspect("itemReader2", itemReader2);
		inspect("itemProcessor", itemProcessor);
		inspect("itemWriter2", itemWriter2);
		
//		log.info("-------------------------------------------------------------------------------------------------------------------------------");
//		log.info("\t > applicationContext = {}", applicationContext.getClass().getName());
//		log.info("\t > parent = {}", applicationContext.getParent().getClass().getName());
//		
//		log.info("\t > webApplicationContext = {}", webApplicationContext.getClass().getName());
//		log.info("\t > serveletContext = {}", webApplicationContext.getServletContext().getClass().getName());
//
//		ConfigurableListableBeanFactory beanFactory = 
//				((ConfigurableApplicationContext) applicationContext).getBeanFactory();
//		log.info("\t > beanFactory from applicationContext = {}", beanFactory.getClass().getName());
//		
//		BeanFactory parentBeanFactory = applicationContext.getParentBeanFactory();
//		log.info("\t > beanFactory from applicationContext parent = {}", parentBeanFactory.getClass().getName());
//		
//		String[] scopeNames = beanFactory.getRegisteredScopeNames();
//		log.info("\t > scopeNames: {}", Arrays.toString(scopeNames));
		
//		log.info("\t > jobScope = {}", jobScope);
//		log.info("\t > stepScope = {}", stepScope);

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


}
