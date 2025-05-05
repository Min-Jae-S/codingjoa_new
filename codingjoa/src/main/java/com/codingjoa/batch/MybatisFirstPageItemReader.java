package com.codingjoa.batch;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MybatisFirstPageItemReader<T> extends MyBatisPagingItemReader<T> {

	private static final int FIXED_PAGE = 0;
	
	@Override
	public int getPage() {
		log.info("## {}.getPage", this.getClass().getSimpleName());
		log.info("\t > page from super: {}, fixed page: {}", super.getPage(), FIXED_PAGE);
		return FIXED_PAGE;
	}
	
	@BeforeStep
	public void beforeStep() {
		log.info("## {}.beforeStep", this.getClass().getSimpleName());
	}

	@BeforeRead
	public void beforeRead() {
		log.info("## {}.beforeRead", this.getClass().getSimpleName());
	}
	
}
