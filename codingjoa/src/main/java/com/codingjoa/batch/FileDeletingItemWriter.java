package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileDeletingItemWriter<T> implements ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {
		log.info("## {}.write", this.getClass().getSimpleName());

		List<Long> boardImageIds = items.stream()
				.map(item -> ((BoardImage) item).getId())
				.collect(Collectors.toList());
		log.info("\t > boardImageIds = {}", boardImageIds);
	}

}
