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
		log.info("\t > create boardImage = {}", boardImage);
		
		imageMapper.insertBoardImage(boardImage);
		log.info("\t > after inserting boardImage, boardImageIdx = {}", boardImage.getBoardImageIdx());
		
		if (boardImage.getBoardImageIdx() == null) { 
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
		log.info("\t > activate boardImages = {}", boardImages);
		
		if (!boardImages.isEmpty()) {
			imageMapper.activateBoardImages(boardImages, boardDto.getBoardIdx());
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
		log.info("\t > deactivate oldBoardImages = {}", oldBoardImages);
		
		if (!oldBoardImages.isEmpty()) {
			imageMapper.deactivateBoardImages(boardIdx);
		}
		
		List<Integer> newBoardImages = boardDto.getBoardImages();
		log.info("\t > activate newBoardImages = {}", newBoardImages);
		
		if (!newBoardImages.isEmpty()) {
			imageMapper.activateBoardImages(newBoardImages, boardIdx);
		}
	}
	
	@Override
	public MemberImage uploadMemberImage(MultipartFile file, Integer memberIdx) throws IllegalStateException, IOException {
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
		log.info("\t > create memberImage = {}", memberImage);
		
		imageMapper.deactivateMemberImage(memberIdx);
		log.info("\t > deactivate oldMemberImage");
		
		imageMapper.insertMemberImage(memberImage);
		log.info("\t > after inserting memberImage, memberImageIdx = {}", memberImage.getMemberImageIdx());
		
		if (memberImage.getMemberImageIdx() == null) { 
			throw new ExpectedException("error.UploadMemberImage");
		}
		
		return memberImage;
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
	public BoardImage getBoardImageByIdx(Integer boardIdx) {
		return imageMapper.findBoardImageByIdx(boardIdx);
	}

	@Override
	public BoardImage getBoardImageByName(String boardImageName) {
		BoardImage boardImage = imageMapper.findBoardImageByName(boardImageName);
		log.info("\t > find boardImage by name");
		log.info("\t > boardImage = {}", boardImage);
		
		if (boardImage == null) {
			throw new ExpectedException("error.NotFoundBoardImage");
		}
		
		return boardImage;
	}

	@Override
	public MemberImage getMemberImageByName(String memberImageName, Integer memberIdx) {
		MemberImage memberImage = imageMapper.findMemberImageByName(memberImageName);
		log.info("\t > find memberImage by name");
		log.info("\t > memberImage = {}", memberImage);
		
		if (memberImage == null) {
			throw new ExpectedException("error.NotFoundMemberImage");
		}
		
		Integer dbMemberIdx = memberImage.getMemberIdx();
		log.info("\t > memberIdx = {}, dbMemberIdx = {}", memberIdx, dbMemberIdx);
		
		if (dbMemberIdx != memberIdx) {
			throw new ExpectedException("error.NotMyMemberImage");
		}
		
		return memberImage;
	}
	
}
