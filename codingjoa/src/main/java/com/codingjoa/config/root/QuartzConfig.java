package com.codingjoa.config.root;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.JobA;
import com.codingjoa.quartz.JobB;

@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		//AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		
		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setAutoStartup(true);
		
		// only applies under specific conditions and may not always work when creating Jobs dynamically.
		schedulerFactory.setOverwriteExistingJobs(true);
		
		//schedulerFactory.setJobDetails(jobDetail);
		//schedulerFactory.setTriggers(trigger);
		
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail jobDetailA() {
		return JobBuilder.newJob(JobA.class)
				.withIdentity("jobA", "myJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerA() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailA())
				.withIdentity("triggerA", "myTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}

	@Bean
	public JobDetail jobDetailB() {
		return JobBuilder.newJob(JobB.class)
				.withIdentity("jobB", "myJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerB() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailB())
				.withIdentity("triggerB", "myTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}
	
}
