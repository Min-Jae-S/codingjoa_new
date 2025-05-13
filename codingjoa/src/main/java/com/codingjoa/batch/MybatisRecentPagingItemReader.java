package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisRecentPagingItemReader<T> extends MyBatisPagingItemReader<T> {

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		log.info("## {}.update", this.getClass().getSimpleName());
		super.update(executionContext);
	}
	
	@Override
	public int getPage() {
		return 0;
	}
	
}
