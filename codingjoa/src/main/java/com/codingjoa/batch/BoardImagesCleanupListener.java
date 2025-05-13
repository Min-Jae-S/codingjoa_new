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
	public void onWriteError(Exception exception, List<BoardImage> items) {
		log.info("## [onWriteError] error items size: {}", items.size());
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(BoardImage item, Throwable t) {
		log.info("## [onSkipInWrite] skipped item: {}", item.getBoardId());
		StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		ExecutionContext context = stepExecution.getExecutionContext();
	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("## [afterChunk]");
		
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		log.info("\t > readCount: {}, writeCount: {}", stepExecution.getReadCount(), stepExecution.getWriteCount());
		log.info("\t > commitCount: {}, rollbackCount: {}", stepExecution.getCommitCount(), stepExecution.getRollbackCount());
		log.info("\t > skipCount: {}", stepExecution.getSkipCount());
	}

}
