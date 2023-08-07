package com.codingjoa.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.dto.BoardImageDto;
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
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${upload.board.path}")
	private String boardPath;
	
	@Value("${upload.profile.path}")
	private String profilePath;
	
	@Override
	public BoardImageDto uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException {
		// os에 따른 path 고려 추가하기 
		File uploadFolder = new File(boardPath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File saveFile = new File(uploadFolder, uploadFilename);
		file.transferTo(saveFile);
		
		BoardImage boardImage = new BoardImage();
		boardImage.setBoardImageName(uploadFilename);
		boardImage.setBoardImagePath(saveFile.getCanonicalPath()); // absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
		uploadMapper.insertBoardImage(boardImage);
		log.info("\t > uploaded, {}", boardImage);
		
		return modelMapper.map(boardImage, BoardImageDto.class);
	}
	
	private String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
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
		File saveFile = new File(uploadFolder, uploadFilename);
		file.transferTo(saveFile);
		
		ProfileImage profileImage = new ProfileImage();
		profileImage.setMemberIdx(memberIdx);
		profileImage.setProfileImageName(uploadFilename);
		profileImage.setProfileImagePath(saveFile.getCanonicalPath());

		uploadMapper.deactivateProfileImage(memberIdx);
		uploadMapper.insertProfileImage(profileImage);
		log.info("\t > uploaded, {}", profileImage);
	}
	
}
