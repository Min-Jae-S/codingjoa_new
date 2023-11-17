package com.codingjoa.configurer;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MyBatchConfigurer extends DefaultBatchConfigurer {

	@Autowired
	@Override
	public void setDataSource(@Qualifier("batchDataSource") DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
}
