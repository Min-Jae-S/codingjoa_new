package com.codingjoa.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.BoardImageCleanupQuartzJob;

@SuppressWarnings("unused")
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
	public JobDetailFactoryBean boardImageCleanupQuartzJobDetail() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(BoardImageCleanupQuartzJob.class);
		factory.setDurability(true);
		return factory;
	}

	@Bean
	public CronTriggerFactoryBean boardImageCleanupQuartzTrigger(
			@Qualifier("boardImageCleanupJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(jobDetail);
		factory.setCronExpression("0 0 3 * * ?");
		return factory;
	}
	
	
}
