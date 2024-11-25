package com.codingjoa.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

import lombok.extern.slf4j.Slf4j;

/*
 * This JobFactory autowires automatically the created quartz bean with spring @Autowired dependencies.
 * @ author jelies (thanks to Brian Matthews: http://webcache.googleusercontent.com/search?q=cache:FH-N1i--sDgJ:blog.btmatthews.com/2011/09/24/inject-application-context-dependencies-in-quartz-job-beans/+&cd=7&hl=en&ct=clnk&gl=es)
 * 
 * @@ https://gist.github.com/ihoneymon/d5c59ea2f4b959d2888d
 * @@ https://homoefficio.github.io/2019/09/29/Quartz-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC-%EC%A0%81%EC%9A%A9-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B0%9C%EC%84%A0-2/
 */

@Slf4j
public final class AutowiringSpringBeanJobFactory extends AdaptableJobFactory implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		log.info("## {}.createJobInstance", this.getClass().getSimpleName());
		
		Object jobInstance = super.createJobInstance(bundle);
		log.info("\t > jobInstance = {}", jobInstance);
		
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		beanFactory.autowireBean(jobInstance);
		
		return jobInstance;
	}
}
