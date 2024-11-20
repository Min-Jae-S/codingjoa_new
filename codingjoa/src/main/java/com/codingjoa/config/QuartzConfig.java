package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.obsolete.quartz.JobA;
import com.codingjoa.obsolete.quartz.JobB;
import com.codingjoa.obsolete.quartz.QuartzJob;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	/*	
	 * ## Quartz
	 * 	> enable in-memory job scheduler
	 * 	> clustering using database
	 * 	> Job, Trigger
	 * 	> JobDetails, JobDataMap, JobListener, TriggerListener
	 * 	> start (scheduleJob, resumeJob, triggerJob, addJob)
	 * 	> stop (interrupt, unscheduleJob, pauseJob, deleteJob)
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
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		return new SpringBeanJobFactory();
	}

	/*
	 * for working annotation @Autowired in job classes
	 * custom job factory of spring with DI support for @Autowired
	 */
	
//	@Bean
//	public SpringBeanJobFactory jobFactory() {
//		log.info("## AutowiringSpringBeanJobFactory");
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
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
	
}
