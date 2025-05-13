package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class BoardImagesCleanupListener {
	
	@AfterWrite
	public void afterWrite(List<? extends BoardImage> items) {
		List<Long> boardIds = items.stream()
				.map(item -> ((BoardImage) item).getId())
				.collect(Collectors.toList());
		log.info("[afterWrite] items size: {}, boardIds: {}", items.size(), boardIds);
	}
	
	@OnWriteError
	public void onWriteError(Exception exception, List<? extends BoardImage> items) {
		log.info("[onWriteError]");
		StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		ExecutionContext context = stepExecution.getExecutionContext();
	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("[afterChunk]");
		
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		log.info("\t > readCount: {}", stepExecution.getReadCount());
		log.info("\t > writeCount: {}", stepExecution.getWriteCount());
		log.info("\t > commitCount: {}", stepExecution.getCommitCount());
		log.info("\t > rollbackCount: {}", stepExecution.getRollbackCount());
		log.info("\t > skipCount: {}", stepExecution.getSkipCount());
	}

}
