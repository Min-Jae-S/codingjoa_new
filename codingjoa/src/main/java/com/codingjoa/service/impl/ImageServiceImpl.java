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
				.boardImagePath(uploadFile.getCanonicalPath()) // absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
				.build();
		imageMapper.insertBoardImage(boardImage);
		
		return boardImage;
	}
	
	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return imageMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImage(BoardDto boardDto) {
		List<Integer> boardImages = boardDto.getBoardImages();
		log.info("\t > activate board images = {}", boardImages);
		
		if (!CollectionUtils.isEmpty(boardImages)) {
			imageMapper.activateBoardImage(boardImages, boardDto.getBoardIdx());
		}
	}
	
	@Override
	public void deactivateBoardImage(BoardDto boardDto) {
		int boardIdx = boardDto.getBoardIdx();
		List<Integer> boardImages = imageMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		log.info("\t > deactivate board images = {}", boardImages);
		
		// deactive된 boardImage의 index를 update와 동시에...?
		imageMapper.deactivateBoardImage(boardIdx);
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

		imageMapper.deactivateMemberImage(memberIdx);
		imageMapper.insertMemberImage(memberImage);
		log.info("\t > uploaded memberImage = {}", memberImage);
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
		log.info("\t > find boardImage by boardImageName");
		log.info("\t > boardImage = {}", boardImage);
		
		if (boardImage == null) {
			throw new ExpectedException("error.NotFoundBoardImage");
		}
		
		return boardImage;
	}

	@Override
	public MemberImage findMemberImageByName(String memberImageName, Integer memberIdx) {
		MemberImage memberImage = imageMapper.findMemberImageByName(memberImageName);
		log.info("\t > find memberImage by memberImageName");
		log.info("\t > memberImage = {}", memberImage);
		
		if (memberImage == null) {
			throw new ExpectedException("error.NotFoundMemberImage");
		}
		
		log.info("\t > DB memberIdx = {}", memberImage.getMemberIdx());
		log.info("\t > MY memberIdx = {}", memberIdx);
		if (memberImage.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyMemberImage");
		}
		
		return memberImage;
	}
	
}
