package com.codingjoa.batch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;

import com.codingjoa.entity.UserImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserImageFileItemWriter implements ItemWriter<UserImage> {
	
	private String userImageDir;
	
	public void setUserImageDir(String userImageDir) {
		this.userImageDir = userImageDir;
	}

	@Override
	public void write(List<? extends UserImage> items) throws Exception {
		log.info("## {}.write", this.getClass().getSimpleName());

		List<Long> ids = items.stream()
				.map(userImage -> userImage.getId())
				.collect(Collectors.toList());
		log.info("\t > items: {}", ids);
		
		for (UserImage userImage : items) {
			Path path = Paths.get(userImageDir, userImage.getName());
			try {
				boolean deleted = Files.deleteIfExists(path);
				log.info("\t > {} ({}, {})", path, userImage.getId(), deleted ? "SUCCESS" : "NO FILE");
			} catch (Exception e) {
				log.info("\t > {}: error while deleting file ({})", e.getClass().getSimpleName(), path);
				throw new RuntimeException("failed to delete file: " + path, e);
			}
		}
	}

}
