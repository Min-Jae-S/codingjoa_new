package com.codingjoa.batch;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImageCleanupChunkListener {
	
	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("## [afterChunk]");
	}

}
