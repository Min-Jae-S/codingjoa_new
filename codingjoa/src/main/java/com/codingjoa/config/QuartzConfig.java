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

import com.codingjoa.quartz.AutowiringJobFactory;
import com.codingjoa.quartz.SampleJob;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
		AutowiringJobFactory jobFactory = new AutowiringJobFactory();
		jobFactory.setApplicationContext(applicationContext);

		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory);
		
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail sampleJobDetail() {
		return JobBuilder.newJob(SampleJob.class)
				.withIdentity("sampleJob", "sampleJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger trigger1(@Qualifier("sampleJobDetail") JobDetail job) {
		return TriggerBuilder.newTrigger()
				.forJob(job)
				.withIdentity("trigger1", "sampleTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}
	
}
