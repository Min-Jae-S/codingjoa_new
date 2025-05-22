package com.codingjoa.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInWrite;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkippedIdCatchListener {
	
	private final Map<String, Function<Object, Long>> extractors = new HashMap<>();
	private StepExecution stepExecution;
	
	public SkippedIdCatchListener() {
		extractors.put("boardImageCleanupStep", obj -> ((BoardImage) obj).getId());
		extractors.put("userImageCleanupStep", obj -> ((UserImage) obj).getId());
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	@OnSkipInWrite
	public void onSkipInWrite(Object item, Throwable t) {
		String stepName = stepExecution.getStepName();
		Function<Object, Long> extractor = extractors.get(stepName);
		
		Long skippedId = extractor.apply(item);
		log.info("## [onSkipInWrite, {}] skipped item: {}", stepName, skippedId);
		
		//StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		stepExecution.getExecutionContext().putLong("skippedId", skippedId);
	}

}
