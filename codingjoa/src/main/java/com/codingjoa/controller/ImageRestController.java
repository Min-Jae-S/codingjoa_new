package com.codingjoa.controller;

import java.net.MalformedURLException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.ProfileImageDto;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ImageRestController {
	
	@Value("${upload.board.path}")
	private String boardPath;	// D:/Dev/upload/board/

	@Value("${upload.profile.path}")
	private String profilePath; // D:/Dev/upload/profile/
	
	@Autowired
	private ModelMapper modelMapper;

	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
	@GetMapping("/board/images/{boardImageName:.+}") 
	public ResponseEntity<Object> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		log.info("\t > boardImageName = {}", boardImageName);
		
		// db에서 가져온 절대경로로 resource를 얻도록 수정하기
		String boardImagePath = boardPath + boardImageName; 
		log.info("\t > boardImagePath = {}", boardImagePath);
		
		// Path.get(boardImagePath) --> file:C:\ ... ?
		// Path.of(boardImagePath) --> file:C:\ ... ?
		
		return ResponseEntity.ok(new UrlResource("file:" + boardImagePath));
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
}
