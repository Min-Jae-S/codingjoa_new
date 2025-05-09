package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class MybatisAdaptivePagingItemReader<T> extends MyBatisPagingItemReader<T> {

	private static final int FIXED_PAGE = 0;
	
	@Override
	public int getPage() {
		return FIXED_PAGE;
	}
	
}
