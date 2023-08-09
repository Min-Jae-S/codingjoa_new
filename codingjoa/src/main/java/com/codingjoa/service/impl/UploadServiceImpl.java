package com.codingjoa.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.ProfileImage;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Value("${upload.board.path}")
	private String boardPath;
	
	@Value("${upload.profile.path}")
	private String profilePath;
	
	@Override
	public BoardImage uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException {
		File uploadFolder = new File(boardPath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);
		
		BoardImage boardImage = BoardImage.builder()
				.boardImageName(uploadFilename)
				.boardImagePath(uploadFile.getCanonicalPath()) // absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
				.build();
		uploadMapper.insertBoardImage(boardImage);
		
		return boardImage;
	}
	
	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return uploadMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImage(BoardDto boardDto) {
		List<Integer> boardImages = boardDto.getBoardImages();
		log.info("\t > activate board images = {}", boardImages);
		
		if (!CollectionUtils.isEmpty(boardImages)) {
			uploadMapper.activateBoardImage(boardImages, boardDto.getBoardIdx());
		}
	}
	
	@Override
	public void deactivateBoardImage(BoardDto boardDto) {
		int boardIdx = boardDto.getBoardIdx();
		List<Integer> boardImages = uploadMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		log.info("\t > deactivate board images = {}", boardImages);
		
		// deactive된 boardImage의 index를 update와 동시에...?
		uploadMapper.deactivateBoardImage(boardIdx);
	}
	
	@Override
	public void uploadProfileImage(MultipartFile file, Integer memberIdx) throws IllegalStateException, IOException {
		File uploadFolder = new File(profilePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);
		
		ProfileImage profileImage = ProfileImage.builder()
				.memberIdx(memberIdx)
				.profileImageName(uploadFilename)
				.profileImagePath(uploadFile.getCanonicalPath())
				.build();

		uploadMapper.deactivateProfileImage(memberIdx);
		uploadMapper.insertProfileImage(profileImage);
		log.info("\t > uploaded profileImage = {}", profileImage);
	}
	
	private String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

	// test
	@Override
	public BoardImage findBoardImageByIdx(Integer boardIdx) {
		return uploadMapper.findBoardImageByIdx(boardIdx);
	}
	
}
