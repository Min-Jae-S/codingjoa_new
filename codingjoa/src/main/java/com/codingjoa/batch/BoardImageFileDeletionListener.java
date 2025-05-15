package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.util.TransactionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImageFileDeletionListener {
	
	private StepExecution stepExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	@AfterWrite
	public void afterWrite(List<BoardImage> items) {
		log.info("## [afterWrite]");
		TransactionUtils.logTransaction();

		List<Long> ids = items.stream()
				.map(boardImage -> boardImage.getId())
				.collect(Collectors.toList());
		log.info("\t > save list of successfully deleted BoardImages to ExecutionContext");
		log.info("\t > items: {}", ids);
		
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put("deletedImages", items);
	}

	@SuppressWarnings("unchecked")
	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("## [afterChunk]");
		TransactionUtils.logTransaction();
		
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		List<BoardImage> deleteImages = (List<BoardImage>) executionContext.get("deletedImages");
		List<Long> ids = (deleteImages != null) ? 
				deleteImages.stream().map(boardImage -> boardImage.getId()).collect(Collectors.toList()) : null;
		log.info("\t > items: {}", ids);
		
		executionContext.remove("deletedImages");
	}
}
