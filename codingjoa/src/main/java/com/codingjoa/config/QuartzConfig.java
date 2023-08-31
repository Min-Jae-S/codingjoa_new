package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.codingjoa.quartz.TestJob;

@Configuration
@PropertySource("/WEB-INF/properties/quartz.properties")
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		return schedulerFactory;
	}
	
	// for working annotation @Autowired in job classes
//	@Bean
//	public SpringBeanJobFactory springBeanJobFactory() {
//		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
	@Bean
	public JobDetail jobDetail() {
		return JobBuilder.newJob(TestJob.class)
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger trigger() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail())
				.withSchedule(null)
				.build();
	}

}
