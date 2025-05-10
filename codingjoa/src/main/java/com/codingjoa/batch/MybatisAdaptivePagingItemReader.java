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
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		log.info("## {}.open", this.getClass().getSimpleName());
		super.open(executionContext);
		
		if (executionContext.containsKey(PAGE_KEY)) {
			adaptivePage = executionContext.getInt(PAGE_KEY);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		log.info("## {}.update", this.getClass().getSimpleName());
		super.update(executionContext);
		
		executionContext.putInt(PAGE_KEY, adaptivePage);
		log.info("\t > saved adaptivePage: {}", adaptivePage);
	}
	
	@Override
	public int getPage() {
		return adaptivePage;
	}
	
}
