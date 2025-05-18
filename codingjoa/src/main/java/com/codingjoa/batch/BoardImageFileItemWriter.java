package com.codingjoa.batch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardImageFileItemWriter implements ItemWriter<BoardImage> {
	
	private String boardImageDir;
	
	public void setBoardImageDir(String boardImageDir) {
		this.boardImageDir = boardImageDir;
	}

	@Override
	public void write(List<? extends BoardImage> items) throws Exception {
		log.info("## {}.write", this.getClass().getSimpleName());

		List<Long> ids = items.stream()
				.map(boardImage -> boardImage.getId())
				.collect(Collectors.toList());
		log.info("\t > items: {}", ids);
		
		for (BoardImage boardImage : items) {
			Path path = Paths.get(boardImageDir, boardImage.getName());
			try {
				boolean deleted = Files.deleteIfExists(path);
				log.info("\t > {} ({}, {})", path, boardImage.getId(), deleted ? "SUCCESS" : "NO FILE");
			} catch (Exception e) {
				log.info("\t > {}: error while deleting file ({})", e.getClass().getSimpleName(), path);
				throw new RuntimeException("failed to delete file: " + path, e);
			}
		}
	}

}
