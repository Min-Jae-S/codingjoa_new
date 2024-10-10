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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.BoardImageDto;
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
	
	private final ImageMapper imageMapper;
	private final String boardImageDir; 	// D:/Dev/upload/board/images/
	private final String memberImageDir; 	// D:/Dev/upload/member/images/
	
	@Autowired
	public ImageServiceImpl(ImageMapper imageMapper, 
			@Value("${upload.dir.board.image}") String boardImageDir,
			@Value("${upload.dir.member.image}") String memberImageDir) {
		this.imageMapper = imageMapper;
		this.boardImageDir = boardImageDir; 
		this.memberImageDir = memberImageDir;
	}

	@Override
	public BoardImageDto saveBoardImage(MultipartFile file) throws IllegalStateException, IOException {
		File uploadFolder = new File(boardImageDir);
		if (!uploadFolder.exists()) {
			if (!uploadFolder.mkdirs()) {
				throw new ExpectedException("error.UploadBoardImage");
			}
		}
		
		String boardImageName = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, boardImageName);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.UploadBoardImage");
		}
		
		String boardImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/board/images/{filename}")
				.buildAndExpand(boardImageName)
				.toUriString();
		
		// absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
		BoardImage boardImage = BoardImage.builder()
				.boardImageName(boardImageName)
				.boardImageUrl(boardImageUrl)
				.build();
		log.info("\t > create boardImage entity = {}", boardImage);
		
		boolean isSaved = imageMapper.insertBoardImage(boardImage);
		if (!isSaved) {
			throw new ExpectedException("error.UploadBoardImage");
		}
		
		return BoardImageDto.from(boardImage);
	}
	
	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return imageMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImages(List<Integer> boardImages, Integer boardIdx) {
		log.info("\t > target boardImages = {}", boardImages);
		if (boardImages.isEmpty()) {
			return;
		}
		
		log.info("\t > activate boardImages");
		imageMapper.activateBoardImages(boardImages, boardIdx);
	}
	
	@Override
	public void replaceBoardImages(List<Integer> boardImages, Integer boardIdx) {
		List<Integer> oldBoardImages = imageMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(BoardImage -> BoardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		log.info("\t > target oldBoardImages = {}", oldBoardImages);
		
		if (!oldBoardImages.isEmpty()) {
			log.info("\t > deactivate oldBoardImages");
			imageMapper.deactivateBoardImages(boardIdx);
		}
		
		log.info("\t > target boardImages = {}", boardImages);
		if (boardImages.isEmpty()) {
			return;
		}
		
		log.info("\t > activate boardImages");
		imageMapper.activateBoardImages(boardImages, boardIdx);
	}
	
	@Override
	public void replaceMemberImage(MultipartFile file, Integer memberIdx) {
		File uploadFolder = new File(memberImageDir);
		if (!uploadFolder.exists()) {
			if (!uploadFolder.mkdirs()) {
				throw new ExpectedException("error.UploadMemberImage");
			}
		}
		
		String memberImageName = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, memberImageName);
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			throw new ExpectedException("error.UploadMemberImage");
		}
		
		log.info("\t > deactivate oldMemberImage");
		imageMapper.deactivateMemberImage(memberIdx);
		
		String memberImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/member/images/{filename}")
				.buildAndExpand(memberImageName)
				.toUriString();
		
		MemberImage memberImage = MemberImage.builder()
				.memberIdx(memberIdx)
				.memberImageName(memberImageName)
				.memberImageUrl(memberImageUrl)
				.build();
		log.info("\t > create memberImage entity = {}", memberImage);
		
		boolean isSaved = imageMapper.insertMemberImage(memberImage);
		if (!isSaved) { 
			throw new ExpectedException("error.UpdateMemberImage");
		}
	}
	
	private static String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

}
