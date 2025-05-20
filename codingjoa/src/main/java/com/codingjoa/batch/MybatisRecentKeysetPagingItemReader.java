package com.codingjoa.batch;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisRecentKeysetPagingItemReader<T> extends MyBatisPagingItemReader<T> {
	
	private ExecutionContext executionContext;
	private Map<String, Object> baseParameterValues;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);
		this.executionContext = executionContext;
	}

	@Override
	protected void doReadPage() {
		Long skippedId = executionContext.containsKey("skippedId") ? executionContext.getLong("skippedId") : null;
		log.info("## {}.doReadPage, skippedId: {}", this.getClass().getSimpleName(), skippedId);

		Map<String, Object> parameters = new HashMap<>();
		if (baseParameterValues != null) {
			parameters.putAll(baseParameterValues);
		}
		parameters.put("skippedId", skippedId);
		
		super.setParameterValues(parameters);
		super.doReadPage();
	}
	
	@Override
	public void setParameterValues(Map<String, Object> parameterValues) {
		setBaseParameterValues(parameterValues);
		super.setParameterValues(parameterValues);
	}

	private void setBaseParameterValues(Map<String, Object> baseParameterValues) {
		this.baseParameterValues = baseParameterValues;
	}
	
	@Override
	public int getPage() {
		return 0;
	}
	
}
