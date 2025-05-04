package com.codingjoa.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileDeletingItemWriter<T> implements ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {
		log.info("## {}.write", this.getClass().getSimpleName());
		log.info("\t > items = {}", items);
	}

}
