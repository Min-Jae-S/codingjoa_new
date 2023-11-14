package com.codingjoa.configurer;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MyBatchConfigurer extends DefaultBatchConfigurer {

	public MyBatchConfigurer(@Qualifier("batchDataSource") DataSource dataSource) {
		super(dataSource);
	}

}
