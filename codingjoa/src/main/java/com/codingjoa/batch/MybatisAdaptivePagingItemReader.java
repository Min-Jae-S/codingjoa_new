package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisAdaptivePagingItemReader<T> extends MyBatisPagingItemReader<T> {

	private int adaptivePage = 0;
	
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		log.info("## {}.update", this.getClass().getSimpleName());
		super.update(executionContext);
	}
	
	@Override
	public int getPage() {
		return adaptivePage;
	}
	
}
