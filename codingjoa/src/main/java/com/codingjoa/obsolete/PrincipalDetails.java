//package com.codingjoa.obsolete;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import io.jsonwebtoken.Claims;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.ToString;
//
//@SuppressWarnings("serial")
//@ToString
//@Getter
//public class PrincipalDetails implements UserDetails, OAuth2User {
//
//	private final Integer idx;					// FROM member
//	private final String email;					
//	private final String password;
//	private final String nickname;
//	private final String role;					// INNER JOIN auth	
//	private final String imageUrl;				// LEFT OUTER JOIN member_iamge
//	private final String provider;				// LEFT OUTER JOIN sns_info
//	private final List<Integer> myBoardLikes;	// LEFT OUTER JOIN board_likes
//	private final List<Integer> myCommentLikes;	// LEFT OUTER JOIN comment_likes
//
//	@Builder
//	private PrincipalDetails(Integer idx, String email, String password, String nickname, String role, String imageUrl,
//			String provider, List<Integer> myBoardLikes, List<Integer> myCommentLikes) {
//		this.idx = idx;
//		this.email = email;
//		this.password = (password != null) ? password : "";
//		this.nickname = nickname;
//		this.role = role;
//		this.imageUrl = (imageUrl != null) ? imageUrl : "";
//		this.provider = (provider != null) ? provider : "local";
//		this.myBoardLikes =  (myBoardLikes != null) ? myBoardLikes : Collections.emptyList();
//		this.myCommentLikes = (myCommentLikes != null) ? myCommentLikes : Collections.emptyList();
//	}
//	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<GrantedAuthority> authorities = new HashSet<>();
//		authorities.add(new SimpleGrantedAuthority(role));
//		return authorities;
//	}
//
//	@Override
//	public String getUsername() {
//		return this.email;
//	}
//
//	@Override
//	public String getPassword() {
//		return this.password;
//	}
//
//	// more details(active, locked, expired...)
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//	
//	public boolean isMyBoardLikes(int boardIdx) {
//		return myBoardLikes.contains(boardIdx);
//	}
//
//	@Override
//	public Map<String, Object> getAttributes() {
//		return null;
//	}
//
//	@Override
//	public String getName() {
//		return null;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static PrincipalDetails from(Map<String, Object> map) { // from database
//		return PrincipalDetails.builder()
//				.idx((Integer) map.get("memberIdx"))
//				.email((String) map.get("memberEmail"))
//				.password((String) map.get("memberPassword"))
//				.nickname((String) map.get("memberNickname"))
//				.role((String) map.get("memberRole"))
//				.imageUrl((String) map.get("memberImageUrl"))
//				.provider((String) map.get("snsProvider"))
//				.myBoardLikes((List<Integer>) map.get("myBoardLikes"))
//				.myCommentLikes((List<Integer>) map.get("myCommentLikes"))
//				.build();
//	}
//
//	public static PrincipalDetails from(Claims claims) { // from jwt
//		return PrincipalDetails.builder()
//				.idx(Integer.parseInt(claims.getSubject()))
//				.email((String) claims.get("email"))
//				.nickname((String) claims.get("nickname"))
//				.role((String) claims.get("role"))
//				.imageUrl((String) claims.get("image_url"))
//				.provider((String) claims.get("provider"))
//				.build();
//	}
//
//}
