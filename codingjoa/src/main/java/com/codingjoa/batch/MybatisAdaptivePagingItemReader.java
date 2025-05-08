package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.annotation.AfterRead;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisAdaptivePagingItemReader<T> extends MyBatisPagingItemReader<T> {

	private static final int FIXED_PAGE = 0;
	private boolean logged = true;
	
	@Override
	public int getPage() {
		if (logged) {
			log.info("## {}.getPage, super: {}, fixed: {}", this.getClass().getSimpleName(), super.getPage(), FIXED_PAGE);
			logged = false;
		}
		
		return FIXED_PAGE;
	}
	
	@AfterRead
	public void afterRead() {
		logged = true;
	}
	
}
