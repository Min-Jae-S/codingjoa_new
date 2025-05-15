package com.codingjoa.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInWrite;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CleanupBoardImageListener {
	
	private StepExecution stepExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(BoardImage item, Throwable t) {
		log.info("## [onSkipInWrite] skipped item: {}", item.getId());
		
		//StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		stepExecution.getExecutionContext().putLong("lastSkippedId", item.getId());
	}

}
