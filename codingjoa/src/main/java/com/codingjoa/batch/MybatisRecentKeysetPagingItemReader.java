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
	private boolean fixedPageEnabled = false;
	private int fixedPage = 0;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);
		this.executionContext = executionContext;
	}

	@Override
	protected void doReadPage() {
		Long skippedId = executionContext.containsKey("skippedId") ? executionContext.getLong("skippedId") : null;
		if (skippedId != null) {
			log.info("## {}, catched skippedId: {}", this.getClass().getSimpleName(), skippedId);
		}

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
		return fixedPageEnabled ? fixedPage : super.getPage();
	}

	public void enableFixedPage(int page) {
		this.fixedPageEnabled = true;
		this.fixedPage = page;
	}
	
}
