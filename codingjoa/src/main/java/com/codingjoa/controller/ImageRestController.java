package com.codingjoa.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.MemberImage;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.ImageService;
import com.codingjoa.validator.UploadFileValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ImageRestController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	// 이미지 여러개 업로드로 변경하기
	@PostMapping("/board/image")
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		BoardImage boardImage = imageService.uploadBoardImage(uploadFileDto.getFile());
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.messageByCode("success.UploadBoardImage")
				.data(new BoardImageDto(boardImage.getBoardImageIdx(), boardImage.getBoardImageName()))
				.build());
	}
	
	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
	//@GetMapping(value = { "/board/images", "/board/images/{boardImageName}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
	@GetMapping(value = { "/board/images/", "/board/images/{boardImageName:.+}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
	public ResponseEntity<Object> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		BoardImage boardImage = imageService.getBoardImageByName(boardImageName);
		Path boardImagePath = Path.of(boardImage.getBoardImagePath());
		UrlResource urlResource = new UrlResource(boardImagePath.toUri());
		log.info("\t > create urlResource = {}", urlResource);
		
		return ResponseEntity.ok(urlResource);
	}
	
	@PostMapping("/member/image")
	public ResponseEntity<Object> uploadMemberImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal UserDetailsDto principal) throws IllegalStateException, IOException {
		log.info("## uploadMemberImage");
		imageService.uploadMemberImage(uploadFileDto.getFile(), principal.getMember().getMemberIdx());
		resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UploadMemberImage").build());
	}
	
	@GetMapping(value = { "/member/images/", "/member/images/{memberImageName:.+}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
	public ResponseEntity<Object> getMemberImageResource(@PathVariable String memberImageName, 
			@AuthenticationPrincipal UserDetailsDto principal) throws MalformedURLException {
		log.info("## getMemberImageResource");
		MemberImage memberImage = 
				imageService.getMemberImageByName(memberImageName, principal.getMember().getMemberIdx());
		UrlResource urlResource = new UrlResource("file:" + memberImage.getMemberImagePath());
		log.info("\t > create urlResource = {}", urlResource);
		
		return ResponseEntity.ok(urlResource);
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
