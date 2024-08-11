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
	
	private final ImageMapper imageMapper;
	private final String boardImageDir; 	// D:/Dev/upload/board/images/
	private final String memberImageDir; 	// D:/Dev/upload/member/images/
	
	@Autowired
	public ImageServiceImpl(ImageMapper imageMapper, @Value("${upload.dir.board.image}") String boardImageDir,
			@Value("${upload.dir.member.image}") String memberImageDir) {
		this.imageMapper = imageMapper;
		this.boardImageDir = boardImageDir; 
		this.memberImageDir = memberImageDir;
	}

	@Override
	public BoardImage uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException {
		File uploadFolder = new File(boardImageDir);
		if (!uploadFolder.exists()) {
			if (!uploadFolder.mkdirs()) {
				throw new IOException("failed to create driectory : " + boardImageDir);
			}
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);
		
		String boardImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/board/images/{filename}")
				.buildAndExpand(uploadFilename)
				.toUriString();
		
		// absolutePath vs canonicalPath (https://dev-handbook.tistory.com/11)
		BoardImage boardImage = BoardImage.builder()
				.boardImageName(uploadFilename)
				.boardImageUrl(boardImageUrl)
				.build();
		log.info("\t > create boardImage entity");
		
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
		File uploadFolder = new File(memberImageDir);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File uploadFile = new File(uploadFolder, uploadFilename);
		file.transferTo(uploadFile);

		String memberImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/member/images/{filename}")
				.buildAndExpand(uploadFilename)
				.toUriString();
		
		MemberImage memberImage = MemberImage.builder()
				.memberIdx(memberIdx)
				.memberImageName(uploadFilename)
				.memberImageUrl(memberImageUrl)
				.build();
		log.info("\t > create memberImage entity");
		
		imageMapper.deactivateMemberImage(memberIdx);
		log.info("\t > deactivate oldMemberImage");
		
		imageMapper.insertMemberImage(memberImage);
		log.info("\t > after inserting memberImage, memberImageIdx = {}", memberImage.getMemberImageIdx());
		
		if (memberImage.getMemberImageIdx() == null) { 
			throw new ExpectedException("error.UploadMemberImage");
		}
		
		return memberImage;
	}
	
	private String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}

}
