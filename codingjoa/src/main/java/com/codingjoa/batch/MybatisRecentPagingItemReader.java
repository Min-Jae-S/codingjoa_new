package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisRecentPagingItemReader<T> extends MyBatisPagingItemReader<T> {
	
	private static final String LAST_SKIPPED_ID_KEY = "lastSkippedId";
	private ExecutionContext executionContext;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);
		this.executionContext = executionContext;
	}

	@Override
	protected void doReadPage() {
		log.info("## {}.doReadPage", this.getClass().getSimpleName());
		
		Long lastSkippedId = executionContext.containsKey(LAST_SKIPPED_ID_KEY) ? 
				executionContext.getLong(LAST_SKIPPED_ID_KEY) : null;
		log.info("\t > lastSkippedId: {}", lastSkippedId);
		
		// setParams
		
		super.doReadPage();
	}
	
	@Override
	public int getPage() {
		return 0;
	}
	
	
}
