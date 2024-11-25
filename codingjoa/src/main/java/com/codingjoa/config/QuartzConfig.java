package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.AutowiringSpringBeanJobFactory;
import com.codingjoa.quartz.Job1;
import com.codingjoa.quartz.Job2;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@ComponentScan("com.codingjoa.quartz")
@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext, 
			JobDetail jobDetail, Trigger trigger) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//		jobFactory.setApplicationContext(applicationContext);
		
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);

		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setJobDetails(jobDetail);
		schedulerFactory.setTriggers(trigger);
		schedulerFactory.setAutoStartup(false);
		
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail jobDetail1() {
		return JobBuilder.newJob(Job1.class)
				.withIdentity("jobDetail1", "sampleJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger trigger1() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail1())
				.withIdentity("trigger1", "sampleTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}

//	@Bean
//	public JobDetail jobDetail2() {
//		return JobBuilder.newJob(Job2.class)
//				.withIdentity("jobDetail2", "sampleJobs")
//				.storeDurably()
//				.build();
//	}
//	
//	@Bean
//	public Trigger trigger2() {
//		return TriggerBuilder.newTrigger()
//				.forJob(jobDetail2())
//				.withIdentity("trigger2", "sampleTriggers")
//				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
//				.build();
//	}
	
}
