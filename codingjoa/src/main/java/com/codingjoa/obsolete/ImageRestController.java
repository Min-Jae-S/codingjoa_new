package com.codingjoa.obsolete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.ImageService;
import com.codingjoa.validator.UploadFileValidator;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
	
//	// 이미지 여러개 업로드로 변경하기
//	@PostMapping("/board/image")
//	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
//			HttpServletRequest request) throws IllegalStateException, IOException {
//		log.info("## uploadBoardImage");
//		BoardImage boardImage = imageService.uploadBoardImage(uploadFileDto.getFile());
//		
//		String boardImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//				.path("/api/board/images/")
//				.path(boardImage.getBoardImageName())
//				.build()
//				.getPath();
//		
//		return ResponseEntity.ok(SuccessResponse
//				.builder()
//				.messageByCode("success.UploadBoardImage")
//				.data(new BoardImageDto(boardImage.getBoardImageIdx(), boardImageUrl))
//				.build());
//	}
//	
//	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
//	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
//	//@GetMapping(value = { "/board/images", "/board/images/{boardImageName}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
//	@GetMapping(value = { "/board/images/", "/board/images/{boardImageName:.+}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
//	public ResponseEntity<Object> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
//		log.info("## getBoardImageResource");
//		BoardImage boardImage = imageService.getBoardImageByName(boardImageName);
//		Path boardImagePath = Path.of(boardImage.getBoardImagePath());
//		UrlResource urlResource = new UrlResource(boardImagePath.toUri());
//		log.info("\t > create urlResource using boardImagePath");
//		
//		return ResponseEntity.ok(urlResource);
//	}
//	
//	@PostMapping("/member/image")
//	public ResponseEntity<Object> uploadMemberImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
//			@AuthenticationPrincipal UserDetailsDto principal) throws IllegalStateException, IOException {
//		log.info("## uploadMemberImage");
//		MemberImage memberImage = imageService.uploadMemberImage(uploadFileDto.getFile(), principal.getMember().getMemberIdx());
//		//resetAuthentication(principal.getMember().getMemberId());
//		
//		String memberImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//				.path("/api/member/images/")
//				.path(memberImage.getMemberImageName())
//				.build()
//				.getPath();
//		
//		return ResponseEntity.ok(SuccessResponse
//				.builder()
//				.messageByCode("success.UploadMemberImage")
//				.data(Map.of("memberImageUrl", memberImageUrl))
//				.build());
//	}
//	
//	@GetMapping(value = { "/member/images/", "/member/images/{memberImageName:.+}"}, produces = MediaType.IMAGE_JPEG_VALUE) 
//	public ResponseEntity<Object> getMemberImageResource(@PathVariable String memberImageName, 
//			@AuthenticationPrincipal UserDetailsDto principal) throws MalformedURLException {
//		log.info("## getMemberImageResource");
//		MemberImage memberImage = imageService.getMemberImageByName(memberImageName, principal.getMember().getMemberIdx());
//		UrlResource urlResource = new UrlResource("file:" + memberImage.getMemberImagePath());
//		log.info("\t > create urlResource using memberImagePath");
//		
//		return ResponseEntity.ok(urlResource);
//	}
//	
//	@SuppressWarnings("unused")
//	private void resetAuthentication(String memberId) {
//		log.info("## resetAuthentication");
//		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
//		Authentication newAuthentication = 
//				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
//	}
}
