package com.codingjoa.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
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
	
	@PostMapping("/upload/board-image") 
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		
		BoardImage boardImage = imageService.uploadBoardImage(uploadFileDto.getFile());
		log.info("\t > uploaded boardImage = {}", boardImage);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadBoardImage")
				.data(new BoardImageDto(boardImage.getBoardImageIdx(), boardImage.getBoardImageName()))
				.build());
	}
	
	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
	//@GetMapping(value = { "/board/images", "/board/images/{boardImageName:.+}"}) 
	@GetMapping(value = { "/board/images", "/board/images/{boardImageName}"}) 
	public ResponseEntity<Object> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		log.info("\t > boardImageName = {}", boardImageName);
		
		BoardImage boardImage = imageService.findBoardImageByName(boardImageName);
		log.info("\t > find boardImage by boardImageName, {}", boardImage);
		
		UrlResource resource = new UrlResource("file:" + boardImage.getBoardImagePath());
		log.info("\t > respond with urlResource = {}", resource);
		
		return ResponseEntity.ok(resource);
	}

	@PostMapping("/upload/member-image")
	public ResponseEntity<Object> uploadMemberImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal UserDetailsDto principal, HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadMemberImage");
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		imageService.uploadMemberImage(uploadFileDto.getFile(), memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadMemberImage")
				.build());
	}
	
	@GetMapping(value = { "/member/images", "/member/images/{memberImageName}"}) 
	public ResponseEntity<Object> getMemberImageResource(@PathVariable String memberImageName) throws MalformedURLException {
		log.info("## getMemberImageResource");
		log.info("\t > memberImageName = {}", memberImageName);
		
		MemberImage memberImage = imageService.findMemberImageByName(memberImageName);
		log.info("\t > find memberImage by memberImageName, {}", memberImage);
		
		UrlResource resource = new UrlResource("file:" + memberImage.getMemberImagePath());
		log.info("\t > respond with urlResource = {}", resource);
		
		return ResponseEntity.ok(resource);
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(newAuthentication);
		
		//HttpSession session = request.getSession(true);
	    //session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
}
