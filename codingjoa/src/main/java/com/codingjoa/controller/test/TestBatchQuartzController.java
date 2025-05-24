package com.codingjoa.controller.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
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
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
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

	@Value("${upload.dir.user.image}")
	private String userImageDir;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
	@Autowired
	private Scheduler scheduler;
	
	@GetMapping("/batch/config")
	public ResponseEntity<Object> configBatch() {
		log.info("## configBatch");
		log.info("\t > jobRepository = {}", jobRepository);
		log.info("\t > jobExplorer = {}", jobExplorer);
		log.info("\t > jobLauncher = {}", jobLauncher);
		log.info("\t > jobRegistry = {}", jobRegistry);
		log.info("\t > registered jobs = {}", jobRegistry.getJobNames());
		//log.info("\t > jobNames from jobExplorer = {}", jobExplorer.getJobNames());
		//log.info("\t > jobNames from jobRegistry = {}", jobRegistry.getJobNames());
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/quartz/config")
	public ResponseEntity<Object> configQuartz() throws SchedulerException {
		log.info("## configQuartz");
		log.info("\t > schedulerFactory = {} (autoSetup: {}, running: {})", 
				schedulerFactory, schedulerFactory.isAutoStartup(), schedulerFactory.isRunning());
		log.info("\t > scheduler = {} (started: {}, standByMode: {}, shutdown: {})", 
				scheduler, scheduler.isStarted(), scheduler.isInStandbyMode(), scheduler.isShutdown());
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		log.info("\t > jobKeys = {}", jobKeys);
		
		if (jobKeys.isEmpty()) {
			log.info("\t > no scheduled jobs");
		}
		
		for (JobKey jobKey : jobKeys) {
			log.info("\t > {}", jobKey);
			for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
				TriggerKey triggerKey = trigger.getKey();
				log.info("\t\t - trigger = {}", triggerKey);
				log.info("\t\t - state = {}", scheduler.getTriggerState(triggerKey));
				
				Date previousFireTime = trigger.getPreviousFireTime();
				Date nextFireTime = trigger.getNextFireTime();
				
				String formattedPrevious = (previousFireTime != null) ? sdf.format(previousFireTime) : "N/A";
				String formattedNext = (nextFireTime != null) ? sdf.format(nextFireTime) : "N/A";
				log.info("\t\t - previous = {}", formattedPrevious);
				log.info("\t\t - next = {}", formattedNext);
			}
		}
		
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
	
	@GetMapping("/board-image-dummy-job/run")
	public ResponseEntity<Object> runBoardImageDummyJob() throws Exception {
		log.info("## runBoardImageDummyJob");
		
		Job job = jobRegistry.getJob("boardImageDummyJob");
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

	@GetMapping("/user-image-dummy-job/run")
	public ResponseEntity<Object> runUserImageDummyJob() throws Exception {
		log.info("## runUserImageDummyJob");
		
		Job job = jobRegistry.getJob("userImageDummyJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.addString("userImageDir", userImageDir)
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
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

	@GetMapping("/user-image-cleanup-job/run")
	public ResponseEntity<Object> runUserImageCleanupJob() throws Exception {
		log.info("## runUserImageCleanupJob");
		
		Job job = jobRegistry.getJob("userImageCleanupJob");
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
	
	@GetMapping("/board-count-column-sync-job/run")
	public ResponseEntity<Object> runBoardCountColumnSyncJob() throws Exception {
		log.info("## runBoardCountColumnSyncJob");
		
		Job job = jobRegistry.getJob("boardCountColumnSyncJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();
		log.info("\t > jobParameters = {}", jobParameters);
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("## result: {}", jobExecution.getExitStatus());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/comment-count-column-sync-job/run")
	public ResponseEntity<Object> runCommentCountColumnSyncJob() throws Exception {
		log.info("## runCommentCountColumnSyncJob");
		
		Job job = jobRegistry.getJob("commentCountColumnSyncJob");
		log.info("\t > found job = {}", job);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
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
