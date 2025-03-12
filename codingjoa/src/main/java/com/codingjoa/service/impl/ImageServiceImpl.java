package com.codingjoa.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.ImageMapper;
import com.codingjoa.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ImageServiceImpl implements ImageService {
	
	private final ImageMapper imageMapper;
	private final String boardImageDir; 	// D:/Dev/upload/board/images/
	private final String memberImageDir; 	// D:/Dev/upload/member/images/
	
	public ImageServiceImpl(ImageMapper imageMapper, 
			@Value("${upload.dir.board.image}") String boardImageDir,
			@Value("${upload.dir.member.image}") String memberImageDir) {
		this.imageMapper = imageMapper;
		this.boardImageDir = boardImageDir; 
		this.memberImageDir = memberImageDir;
	}

	@Override
	public BoardImageDto saveBoardImage(MultipartFile file) {
		File folder = new File(boardImageDir);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new ExpectedException("error.UploadBoardImage");
			}
		}
		
		String filename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(folder, filename);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.UploadBoardImage");
		}
		
		String path = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/board/images/{filename}")
				.buildAndExpand(filename)
				.toUriString();
		log.info("\t > path = {}", path);
		
		BoardImage boardImage = BoardImage.builder() // absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
				.name(filename)
				.path(path)
				.build();
		log.info("\t > create boardImage entity = {}", boardImage);
		
		boolean isSaved = imageMapper.insertBoardImage(boardImage);
		log.info("\t > saved boardImage = {}", boardImage);
		
		if (!isSaved) {
			throw new ExpectedException("error.SaveBoardImage");
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
	
	@Override
	public void updateUserImage(MultipartFile file, Long userId) {
		File folder = new File(memberImageDir);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new ExpectedException("error.UploadUserImage");
			}
		}
		
		String filename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(folder, filename);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.UploadUserImage");
		}
		
		log.info("\t > deactivate old userImage");
		imageMapper.deactivateUserImage(userId);
		
		String path = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/user/images/{filename}")
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
			throw new ExpectedException("error.SaveUserImage");
		}
		
	}
	
	private static String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

}
