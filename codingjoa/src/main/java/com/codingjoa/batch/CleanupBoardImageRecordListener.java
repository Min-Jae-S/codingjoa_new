package com.codingjoa.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CleanupBoardImageRecordListener {
	
	private static final String LAST_SKIPPED_ID_KEY = "lastSkippedId";
	private StepExecution stepExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(BoardImage item, Throwable t) {
		log.info("## [onSkipInWrite] skipped item: {}", item.getId());
		
		//StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		stepExecution.getExecutionContext().putLong(LAST_SKIPPED_ID_KEY, item.getId());
	}
	
	@OnWriteError
	public void onWriteError(Exception exception, List<BoardImage> items) {
		if (items.isEmpty()) {
			return;
		} else if (items.size() == 1) {
			log.info("## [onWriteError] error item: {}", items.get(0).getId());
		} else {
			log.info("## [onWriteError] error items: {}", 
					items.stream().map(boardImage -> boardImage.getId()).collect(Collectors.toList()));
		}
	}

}
