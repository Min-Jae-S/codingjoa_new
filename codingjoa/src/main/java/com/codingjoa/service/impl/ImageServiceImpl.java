package com.codingjoa.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.MemberImage;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.ImageMapper;
import com.codingjoa.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Value("${image.board.path}")
	private String boardImagePath;
	
	@Value("${image.member.path}")
	private String memberImagePath;
	
	@Override
	public BoardImage uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException {
		File uploadFolder = createUploadFolder(boardImagePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);
		
		BoardImage boardImage = BoardImage.builder()
				.boardImageName(uploadFilename)
				// absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
				.boardImagePath(uploadFile.getCanonicalPath()) 
				.build();
		log.info("\t > create boardImage");
		log.info("\t > {}", boardImage);
		
		imageMapper.insertBoardImage(boardImage);
		Integer boardImageIdx = boardImage.getBoardImageIdx();
		log.info("\t > after inserting boardImage, boardImageIdx = {}", boardImageIdx);
		
		// 업로드 실패 상황(insert)을 만들어 클라이언트에서 에러 확인하기
		if (boardImageIdx == null) { 
			throw new ExpectedException("error.UploadBoardImage");
		} 
		return boardImage;
	}
	
	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return imageMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImages(BoardDto boardDto) {
		log.info("## activateBoardImages");
		List<Integer> boardImages = boardDto.getBoardImages();
		if (!boardImages.isEmpty()) {
			log.info("\t > activate boardImages = {}", boardImages);
			imageMapper.activateBoardImages(boardImages, boardDto.getBoardIdx());
		} else {
			log.info("\t > no boardImages to activate");
		}
	}
	
	@Override
	public void modifyBoardImages(BoardDto boardDto) {
		log.info("## modifyBoardImages");
		int boardIdx = boardDto.getBoardIdx();
		List<Integer> oldBoardImages = imageMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(BoardImage -> BoardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		if (!oldBoardImages.isEmpty()) {
			log.info("\t > deactivate boardImages = {}", oldBoardImages);
			imageMapper.deactivateBoardImages(boardIdx);
		} else {
			log.info("\t > no boardImages to deactivate");
		}
		
		List<Integer> newBoardImages = boardDto.getBoardImages();
		if (!newBoardImages.isEmpty()) {
			log.info("\t > activate boardImages = {}", newBoardImages);
			imageMapper.activateBoardImages(newBoardImages, boardIdx);
		} else {
			log.info("\t > no boardImages to activate");
		}
	}
	
	@Override
	public void uploadMemberImage(MultipartFile file, Integer memberIdx) throws IllegalStateException, IOException {
		File uploadFolder = createUploadFolder(memberImagePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);
		
		MemberImage memberImage = MemberImage.builder()
				.memberIdx(memberIdx)
				.memberImageName(uploadFilename)
				.memberImagePath(uploadFile.getCanonicalPath())
				.build();
		log.info("\t > create memberImage");
		log.info("\t > {}", memberImage);
		
		// @@ deactivateMemberImage를 merge(upsert)로 수정하기  
		log.info("\t > deactivate memberImage by memeberIdx");
		imageMapper.deactivateMemberImage(memberIdx);
		
		imageMapper.insertMemberImage(memberImage);
		Integer memberImageIdx = memberImage.getMemberImageIdx();
		log.info("\t > after inserting memberImage, memberImageIdx = {}", memberImageIdx);
		
		// 업로드 실패 상황(insert)을 만들어 클라이언트에서 에러 확인하기
		if (memberImageIdx == null) { 
			throw new ExpectedException("error.UploadMemberImage");
		}
	}
	
	private File createUploadFolder(String path) {
		LocalDateTime date = LocalDateTime.now();
		String child = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return new File(path, child);
	}
	
	private String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

	// test
	@Override
	public BoardImage findBoardImageByIdx(Integer boardIdx) {
		return imageMapper.findBoardImageByIdx(boardIdx);
	}

	@Override
	public BoardImage findBoardImageByName(String boardImageName) {
		BoardImage boardImage = imageMapper.findBoardImageByName(boardImageName);
		log.info("\t > find boardImage by imageName");
		log.info("\t > boardImage = {}", boardImage);
		
		if (boardImage == null) {
			throw new ExpectedException("error.NotFoundBoardImage");
		}
		
		return boardImage;
	}

	@Override
	public MemberImage findMemberImageByName(String memberImageName, Integer memberIdx) {
		MemberImage memberImage = imageMapper.findMemberImageByName(memberImageName);
		log.info("\t > find memberImage by imageName");
		log.info("\t > memberImage = {}", memberImage);
		
		if (memberImage == null) {
			throw new ExpectedException("error.NotFoundMemberImage");
		}
		
		log.info("\t > My memberIdx = {}, DB memberIdx = {}", memberIdx, memberImage.getMemberIdx());
		if (memberImage.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyMemberImage");
		}
		
		return memberImage;
	}
	
}
