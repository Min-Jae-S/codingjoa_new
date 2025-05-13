package com.codingjoa.batch;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;

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
	
	@OnWriteError
	public void onWriteError(Exception exception, List<BoardImage> items) {
		log.info("## [onWriteError] error items size: {}", items.size());
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(BoardImage item, Throwable t) {
		log.info("## [onSkipInWrite] skipped item: {}", item.getId());
		
		//StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		stepExecution.getExecutionContext().putLong(LAST_SKIPPED_ID_KEY, item.getId());
	}

}
