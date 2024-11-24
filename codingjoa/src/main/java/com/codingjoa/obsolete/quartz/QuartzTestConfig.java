package com.codingjoa.obsolete.quartz;

import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan("com.codingjoa.quartz") // DI for SchedulerService, JobListener, TriggerListener
@RequiredArgsConstructor
@Configuration
public class QuartzTestConfig {
	
	/*	
	 * ## Quartz
	 * 		- enable in-memory job scheduler
	 * 		- clustering using database
	 * 		- Job, Trigger
	 * 		- JobDetails, JobDataMap, JobListener, TriggerListener
	 * 		- start (scheduleJob, resumeJob, triggerJob, addJob)
	 * 		- stop (interrupt, unscheduleJob, pauseJob, deleteJob)
	 * 
	 *	## Scheduler
	 * 		- manage multiple jobs and execute them based on triggers. The scheduler controls the execution time and frequency of jobs, 
	 * 		  and can pause or resume jobs as needed
	 * 		- additionally, the scheduler tracks the execution status of jobs and handles follow-up tasks after job completion
	 */
	
	//private final JobListener jobListener;
    //private final TriggerListener triggerListener;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		//schedulerFactory.setGlobalJobListeners(jobListener);
		//schedulerFactory.setGlobalTriggerListeners(triggerListener);
		schedulerFactory.setJobDetails(jobDetailA(), jobDetailB(), quartzJobDetail());
		schedulerFactory.setTriggers(triggerA(), triggerB(), quartzTrigger());
		schedulerFactory.setAutoStartup(false);
		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		//schedulerFactory.setQuartzProperties(quartzProperties());
		return schedulerFactory;
	}
	
	@Bean
	public Scheduler scheduler() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean().getObject();
		scheduler.pauseAll();
		scheduler.start();
		return scheduler;
	}
	
	/*
	 * for working annotation @Autowired in job classes
	 * custom job factory of spring with DI support for @Autowired
	 */
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		log.info("## AutowiringSpringBeanJobFactory");
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
	    SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
	    return jobFactory;
	}
	
//	@Bean
//	public Properties quartzProperties() {
//		 PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//		 Properties properties = null;
//		 return properties;
//	}
	
	@Bean
	public JobDetail jobDetailA() {
		return JobBuilder.newJob(JobA.class)
				.withIdentity("jobA", "myJob")
				.storeDurably()
				.build();
	}

	@Bean
	public JobDetail jobDetailB() {
		return JobBuilder.newJob(JobB.class)
				.withIdentity("jobB", "myJob")
				.storeDurably()
				.build();
	}

	@Bean
	public JobDetail quartzJobDetail() {
		return JobBuilder.newJob(QuartzJob.class)
				.withIdentity("QuartzJob", "myJob")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerA() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailA())
				.withIdentity("triggerA", "myTrigger")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}

	@Bean
	public Trigger triggerB() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailB())
				.withIdentity("triggerB", "myTrigger")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}

	@Bean
	public Trigger quartzTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(quartzJobDetail())
				.withIdentity("quartzTrigger", "myTrigger")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(20))
				.build();
	}
	
	public void printQuartzConfig() throws SchedulerException {
		log.info("\t > schedulerFactoryBean = {}", schedulerFactoryBean());
		log.info("\t     - autoStartup   = {}", schedulerFactoryBean().isAutoStartup());
		log.info("\t     - running       = {}", schedulerFactoryBean().isRunning());
		log.info("\t > scheduler = {}", scheduler());
		log.info("\t     - inStandbyMode = {}", scheduler().isInStandbyMode());
		log.info("\t     - started       = {}", scheduler().isStarted());
		log.info("\t     - shutdown      = {}", scheduler().isShutdown());
		
		Set<String> jobs = scheduler().getJobKeys(GroupMatcher.anyJobGroup())
				.stream()
				.map(jobKey -> jobKey.getName())
				.collect(Collectors.toSet());
		Set<String> triggers = scheduler().getTriggerKeys(GroupMatcher.anyTriggerGroup())
				.stream()
				.map(triggerKey -> triggerKey.getName())
				.collect(Collectors.toSet());
		log.info("\t     - jobs          = {}", jobs);
		log.info("\t     - triggers      = {}", triggers);
	}

}
