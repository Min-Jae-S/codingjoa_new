package com.codingjoa.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/*
 * This JobFactory autowires automatically the created quartz bean with spring @Autowired dependencies.
 * @@ https://homoefficio.github.io/2019/09/29/Quartz-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC-%EC%A0%81%EC%9A%A9-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B0%9C%EC%84%A0-2/
 */

public final class AutowiringJobFactory extends AdaptableJobFactory implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);
		return jobInstance;
	}
	
}
