package com.codingjoa.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.codingjoa.dto.ProfileImageDto;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.entity.BoardImage;
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
	
	@Value("${upload.profile.path}")
	private String profilePath; // D:/Dev/upload/profile/
	
	@Autowired
	private ModelMapper modelMapper;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	@PostMapping("/upload/board-image") 
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		
		BoardImage boardImage = imageService.uploadBoardImage(uploadFileDto.getFile());
		String boardImageUrl = request.getContextPath() + "/api/board/images/" + boardImage.getBoardImageName();
		log.info("\t > uploaded boardImage = {}", boardImage);
		log.info("\t > boardImageUrl = {}", boardImageUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadBoardImage")
				.data(new BoardImageDto(boardImage.getBoardImageIdx(), boardImageUrl))
				.build());
	}
	
	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
	@GetMapping("/board/images/{boardImageName:.+}") 
	public ResponseEntity<Object> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		log.info("\t > boardImageName = {}", boardImageName);
		
		BoardImage boardImage = imageService.findBoardImageByName(boardImageName);
		UrlResource resource = new UrlResource("file:" + boardImage.getBoardImagePath());
		
		return ResponseEntity.ok(resource);
	}

	@PostMapping("/upload/profile-image")
	public ResponseEntity<Object> uploadProfileImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal UserDetailsDto principal, HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadProfileImage");
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		imageService.uploadProfileImage(uploadFileDto.getFile(), memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadProfileImage")
				.build());
	}
	
	@GetMapping("/profile/images/{profileImageName:.+}") 
	public ResponseEntity<Object> getProfileImageResource(@PathVariable String profileImageName) throws MalformedURLException {
		log.info("## getProfileImageResource");
		log.info("\t > profileImageName = {}", profileImageName);
		
		String profileImagePath = profilePath + profileImageName; 
		log.info("\t > profileImagePath = {}", profileImagePath);
		
		return ResponseEntity.ok(new UrlResource("file:" + profileImagePath));
	}
	
	@GetMapping("/profile/images/current")
	public ResponseEntity<Object> getCurrentProfileImage(@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## getCurrentProfileImage");
		ProfileImageDto currentProfileImage = modelMapper.map(principal.getProfileImage(), ProfileImageDto.class);
		return ResponseEntity.ok(SuccessResponse.builder().data(currentProfileImage).build());
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
