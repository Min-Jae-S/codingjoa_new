package com.codingjoa.config;

import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.BoardImageCleanupQuartzJob;

@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext context) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		//AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(context);
		
		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setAutoStartup(true);
		
		// only applies under specific conditions and may not always work when creating Jobs dynamically.
		schedulerFactory.setOverwriteExistingJobs(true);
		
		//schedulerFactory.setJobDetails(jobDetail);
		//schedulerFactory.setTriggers(trigger);
		
		return schedulerFactory;
	}
	
	@Bean
	public JobDetailFactoryBean boardImageCleanupQuartzJobFactory() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(BoardImageCleanupQuartzJob.class);
		return factory;
	}

	@Bean
	public CronTriggerFactoryBean boardImageCleanupQuartzTriggerFactory(JobDetail boardImageCleanupQuartzJobDetail) {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(boardImageCleanupQuartzJobDetail);
		return factory;
	}
	
}
