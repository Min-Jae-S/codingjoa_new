package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImageFileWriter<T> implements ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {
		List<Long> ids = items.stream()
				.map(item -> ((BoardImage)item).getId())
				.collect(Collectors.toList());
		log.info("## {}.write, items: {}", this.getClass().getSimpleName(), ids);
	}

}
