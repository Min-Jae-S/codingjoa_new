package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.scope.context.ChunkContext;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImagesCleanupListener {
	
	@BeforeWrite
	public void beforeWrite(List<? extends BoardImage> items) {
		List<Long> boardIds = items.stream()
				.map(item -> ((BoardImage) item).getId())
				.collect(Collectors.toList());
		log.info("[before write] items size: {}, boardIds: {}", items.size(), boardIds);
	}
	
	@AfterWrite
	public void afterWrite(List<? extends BoardImage> items) {
		List<Long> boardIds = items.stream()
				.map(item -> ((BoardImage) item).getId())
				.collect(Collectors.toList());
		log.info("[after write] items size: {}, boardIds: {}", items.size(), boardIds);
		
		//throw new RuntimeException("trigger runtimeException");
	}

	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		log.info("[after chunk]");
		
		StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
		log.info("\t > readCount: {}", stepExecution.getReadCount());
		log.info("\t > writeCoutn: {}", stepExecution.getWriteCount());
		log.info("\t > commitCount: {}", stepExecution.getCommitCount());
		log.info("\t > rollbackCount: {}", stepExecution.getRollbackCount());
		log.info("\t > skipCount: {}", stepExecution.getSkipCount());
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(Object item, Throwable t) {
		log.info("[on skip in write]");
		
		BoardImage skippedBoardImage = (BoardImage) item;
		log.info("\t > skipped: {}", skippedBoardImage.getId());
	}

}
