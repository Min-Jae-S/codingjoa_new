package com.codingjoa.batch;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.scope.context.ChunkContext;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImagesCleanupListener {
	
	@AfterWrite
	public void afterWrite(List<? extends BoardImage> items) {
		log.info("## {}.afterWrite", this.getClass().getSimpleName());
	}

	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		log.info("## {}.afterChunk", this.getClass().getSimpleName());
		
		StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
		log.info("\t > readCount: {}", stepExecution.getReadCount());
		log.info("\t > writeCoutn: {}", stepExecution.getWriteCount());
		log.info("\t > commitCount: {}", stepExecution.getCommitCount());
	}

}
