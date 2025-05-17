package com.codingjoa.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.ImageMapper;
import com.codingjoa.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ImageServiceImpl implements ImageService {
	
	private final ImageMapper imageMapper;
	private final String userImageDir; 	// D:/Dev/upload/user/images/
	private final String boardImageDir; // D:/Dev/upload/board/images/
	
	public ImageServiceImpl(ImageMapper imageMapper, 
			@Value("${upload.dir.user.image}") String userImageDir,
			@Value("${upload.dir.board.image}") String boardImageDir) {
		this.imageMapper = imageMapper;
		this.userImageDir = userImageDir;
		this.boardImageDir = boardImageDir; 
	}
	
	@Override
	public void saveUserImageWithUpload(MultipartFile file, Long userId) {
		File folder = new File(userImageDir);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new ExpectedException("error.user.uploadImage");
			}
		}
		
		String filename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(folder, filename);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.user.uploadImage");
		}
		
		String path = UriComponentsBuilder.fromPath("/user/images/{filename}")
				.buildAndExpand(filename)
				.toUriString();
		log.info("\t > path = {}", path);
		
		UserImage userImage = UserImage.builder()
				.userId(userId)
				.name(filename)
				.path(path)
				.build();
		log.info("\t > create userImage entity = {}", userImage);
		
		boolean isSaved = imageMapper.insertUserImage(userImage);
		log.info("\t > saved userImage = {}", userImage);
		
		if (!isSaved) { 
			throw new ExpectedException("error.user.saveImage");
		}
	}

	@Override
	public BoardImageDto saveBoardImageWithUpload(MultipartFile file) {
		File folder = new File(boardImageDir);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new ExpectedException("error.board.uploadImage");
			}
		}
		
		String filename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(folder, filename);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.board.uploadImage");
		}
		
		String path = UriComponentsBuilder.fromPath("/board/images/{filename}")
				.buildAndExpand(filename)
				.toUriString();
		log.info("\t > path = {}", path);
		
		// absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
		BoardImage boardImage = BoardImage.builder() 
				.name(filename)
				.path(path)
				.build();
		log.info("\t > create boardImage entity = {}", boardImage);
		
		boolean isSaved = imageMapper.insertBoardImage(boardImage);
		log.info("\t > saved boardImage = {}", boardImage);
		
		if (!isSaved) {
			throw new ExpectedException("error.board.saveImage");
		}
		
		return BoardImageDto.from(boardImage);
	}
	
	@Override
	public boolean isBoardImageUploaded(Long boardImageId) {
		return imageMapper.isBoardImageUploaded(boardImageId);
	}

	@Override
	public void activateBoardImages(List<Long> boardImages, Long boardId) {
		log.info("\t > activate boardImages = {}", boardImages);
		if (boardImages.isEmpty()) {
			return;
		}
		
		imageMapper.activateBoardImages(boardImages, boardId);
	}
	
	@Override
	public void updateBoardImages(List<Long> boardImages, Long boardId) {
		log.info("\t > deactivate old boardImages");
		imageMapper.deactivateBoardImages(boardId);
		
		log.info("\t > activate new boardImages = {}", boardImages);
		if (boardImages.isEmpty()) {
			return;
		}
		
		imageMapper.activateBoardImages(boardImages, boardId);
	}
	
	private static String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

	//@Async("boardImageTaskExecutor")
	@Override
	public void deleteBoardImageFiles(List<BoardImage> boardImages) {
		log.info("## deleteBoardImageFiles ({})", Thread.currentThread().getName());
		log.info("\t > boardImages = {}", boardImages.stream().map(boardImage -> boardImage.getId()).collect(Collectors.toList()));
		
		for (BoardImage boardImage : boardImages) {
			Path path = Paths.get(boardImageDir, boardImage.getName());
			try {
				boolean deleted = Files.deleteIfExists(path);
				log.info("\t > path: {}, status: {}", path, deleted ? "deleted successfully" : "no file to delete");
			} catch (IOException e) {
				log.info("\t > {}: error while deleting file ({})", e.getClass().getSimpleName(), path);
				throw new RuntimeException("failed to delete file: " + path, e);
			}
		}
	}

	@Override
	public void deleteBoardImageFile(BoardImage boardImage) {
		log.info("## deleteBoardImageFiles ({})", Thread.currentThread().getName());
		log.info("\t > boardImage: {}", boardImage.getId());
	}

}
