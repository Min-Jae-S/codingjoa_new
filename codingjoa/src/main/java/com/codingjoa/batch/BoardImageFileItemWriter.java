package com.codingjoa.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.service.ImageService;
import com.codingjoa.util.TransactionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImageFileItemWriter implements ItemWriter<BoardImage> {
	
	private final ImageService imageService;
	
	public BoardImageFileItemWriter(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public void write(List<? extends BoardImage> items) throws Exception {
		log.info("## {}.write ({})", this.getClass().getSimpleName(), Thread.currentThread().getName());
		log.info("\t > active: {}, tx hash: {}", TransactionUtils.isTransactionAcitve(), TransactionUtils.getTranscationHash());

		List<Long> ids = items.stream()
				.map(boardImage -> boardImage.getId())
				.collect(Collectors.toList());
		log.info("\t > items: {}", ids);
		
		imageService.deleteBoardImageFiles(new ArrayList<>(items));
	}

}
