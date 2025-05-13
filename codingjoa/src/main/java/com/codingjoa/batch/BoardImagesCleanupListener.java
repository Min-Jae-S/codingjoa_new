package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class BoardImagesCleanupListener {
	
	@OnWriteError
	public void onWriteError(Exception exception, List<? extends BoardImage> items) {
		log.info("## [onWriteError] item size: {}", items.size());
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(Object item, Throwable t) {
		log.info("## [onSkipInWrite]");
		log.info("\t > item: {}", item);
	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("## [afterChunk]");
		
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		log.info("\t > readCount: {}", stepExecution.getReadCount());
		log.info("\t > writeCount: {}", stepExecution.getWriteCount());
		log.info("\t > commitCount: {}", stepExecution.getCommitCount());
		log.info("\t > rollbackCount: {}", stepExecution.getRollbackCount());
		log.info("\t > skipCount: {}", stepExecution.getSkipCount());
	}

}
