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
import org.springframework.util.CollectionUtils;
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
		log.info("\t > insert boardImage = {}", boardImage);
		imageMapper.insertBoardImage(boardImage);
		
		return boardImage;
	}
	
	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return imageMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImage(BoardDto boardDto) {
		log.info("## activateBoardImage");
		List<Integer> boardImages = boardDto.getBoardImages();
		if (!CollectionUtils.isEmpty(boardImages)) {
			log.info("\t > activate boardImages = {}", boardImages);
			imageMapper.activateBoardImage(boardImages, boardDto.getBoardIdx());
		} else {
			log.info("\t > no boardImages to activate");
		}
	}
	
	@Override
	public void deactivateBoardImage(BoardDto boardDto) {
		log.info("## deactivateBoardImage");
		imageMapper.deactivateBoardImage(boardDto.getBoardIdx());
		
		int boardIdx = boardDto.getBoardIdx();
		List<Integer> boardImages = imageMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		log.info("\t > find boardImages = {}", boardImages);
		
		if (!boardImages.isEmpty()) { 
			// deactive된 boardImage의 index를 동시에 update?
			log.info("\t > deactivate boardImages = {}", boardImages);
			imageMapper.deactivateBoardImage(boardIdx);
		} else {
			log.info("\t > no boardImages to deactivate");
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
		
		log.info("\t > deactivate memberImage");
		imageMapper.deactivateMemberImage(memberIdx);
		
		MemberImage memberImage = MemberImage.builder()
				.memberIdx(memberIdx)
				.memberImageName(uploadFilename)
				.memberImagePath(uploadFile.getCanonicalPath())
				.build();
		log.info("\t > insert memberImage = {}", memberImage);
		imageMapper.insertMemberImage(memberImage);
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
		log.info("\t > find boardImage = {}", boardImage);
		
		if (boardImage == null) {
			throw new ExpectedException("error.NotFoundBoardImage");
		}
		
		return boardImage;
	}

	@Override
	public MemberImage findMemberImageByName(String memberImageName, Integer memberIdx) {
		MemberImage memberImage = imageMapper.findMemberImageByName(memberImageName);
		log.info("\t > find memberImage = {}", memberImage);
		
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
