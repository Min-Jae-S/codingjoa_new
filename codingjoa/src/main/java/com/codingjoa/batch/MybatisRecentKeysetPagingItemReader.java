package com.codingjoa.batch;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import com.codingjoa.util.TransactionUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class MybatisRecentKeysetPagingItemReader<T> extends MyBatisPagingItemReader<T> {
	
	private ExecutionContext executionContext;
	private Map<String, Object> baseParameterValues;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		//log.info("## {}.open", this.getClass().getSimpleName());
		super.open(executionContext);
		this.executionContext = executionContext;
	}

	@Override
	protected void doReadPage() {
		Long lastSkippedId = executionContext.containsKey("lastSkippedId") ? executionContext.getLong("lastSkippedId") : null;
		//log.info("## {}.doReadPage, lastSkippedId: {}", this.getClass().getSimpleName(), lastSkippedId);
		//TransactionUtils.logTransaction();

		Map<String, Object> parameters = new HashMap<>();
		if (baseParameterValues != null) {
			parameters.putAll(baseParameterValues);
		}
		parameters.put("lastSkippedId", lastSkippedId);
		
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
