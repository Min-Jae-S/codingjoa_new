package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.scheduler.JobA;
import com.codingjoa.scheduler.JobB;
import com.codingjoa.scheduler.JobC;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@ComponentScan("com.codingjoa.scheduler")
@Configuration
public class SchedulerConfig {
	
	@Autowired
	private JobListener jobListener;

	@Autowired
    private TriggerListener triggerListener;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ SchedulerConfig init");
		log.info("===============================================================");
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		//schedulerFactory.setGlobalJobListeners(jobListener);
		//schedulerFactory.setGlobalTriggerListeners(triggerListener);
		schedulerFactory.setJobDetails(jobDetailA(), jobDetailB(), jobDetailC());
		schedulerFactory.setTriggers(triggerA(), triggerB(), triggerC());
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

	// for working annotation @Autowired in job classes
	// custom job factory of spring with DI support for @Autowired
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
	public JobDetail jobDetailC() {
		return JobBuilder.newJob(JobC.class)
				.withIdentity("jobC", "myJob")
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
	public Trigger triggerC() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailC())
				.withIdentity("triggerC", "myTrigger")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(15))
				.build();
	}

}
