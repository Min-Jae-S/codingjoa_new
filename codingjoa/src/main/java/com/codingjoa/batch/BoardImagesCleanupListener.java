package com.codingjoa.batch;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.core.scope.context.ChunkContext;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImagesCleanupListener {
	
	private static final String LAST_SKIPPED_ID_KEY = "lastSkippedId";
	private StepExecution stepExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	@BeforeChunk
	public void afterChunk(ChunkContext chunkContext) {
		log.info("## [afterChunk]");
		chunkContext.getStepContext().getStepExecutionContext().entrySet().forEach(entry -> {
			log.info("\t > {}: {}", entry.getKey(), entry.getValue());
		});
	}
	
	@OnWriteError
	public void onWriteError(Exception exception, List<BoardImage> items) {
		log.info("## [onWriteError] error items size: {}", items.size());
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(BoardImage item, Throwable t) {
		log.info("## [onSkipInWrite] skipped item: {}", item.getId());
		
		//StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

		Long skippedId = item.getId();
		stepExecution.getExecutionContext().putLong(LAST_SKIPPED_ID_KEY, skippedId);
	}

}
