package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisAdaptivePagingItemReader<T> extends MyBatisPagingItemReader<T> {

	private static final String PAGE_KEY = "adaptivePage";
	private int adaptivePage = 0;
	
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		log.info("## {}.update", this.getClass().getSimpleName());
		super.update(executionContext);
		
		executionContext.putInt(PAGE_KEY, adaptivePage);
		log.info("\t > save current adaptivePage to context: {}", adaptivePage);
	}
	
	@Override
	public int getPage() {
		return adaptivePage;
	}
	
}
