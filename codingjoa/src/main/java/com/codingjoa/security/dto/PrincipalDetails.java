package com.codingjoa.security.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User { // consider implementing OAuth2User 

	private final String email;					// FROM member
	private final String password;
	private final String nickname;
	private final String role;					// INNER JOIN auth	
	private final String imageUrl;				// LEFT OUTER JOIN member_iamge
	private final String provider;
	private final List<Integer> myBoardLikes;	// LEFT OUTER JOIN board_likes
	private final List<Integer> myCommentLikes;	// LEFT OUTER JOIN comment_likes

	@Builder
	private PrincipalDetails(String email, String password, String nickname, String role, String imageUrl,
			String provider, List<Integer> myBoardLikes, List<Integer> myCommentLikes) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
		this.imageUrl = imageUrl;
		this.provider = provider;
		this.myBoardLikes = myBoardLikes;
		this.myCommentLikes = myCommentLikes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	// more details(active, locked, expired...)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isMyBoardLikes(int boardIdx) {
		return myBoardLikes.contains(boardIdx);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static PrincipalDetails from(Map<String, Object> map) {
		return PrincipalDetails.builder()
				.email((String) map.get("memberEmail"))
				.password((String) map.get("memberPassword"))
				.nickname((String) map.get("memberNickname"))
				.role((String) map.get("memberRole"))
				.imageUrl((String) map.get("memberImageUrl"))
				.myBoardLikes((List<Integer>) map.get("myBoardLikes"))
				.myCommentLikes((List<Integer>) map.get("myCommentLikes"))
				.build();
	}

	public static PrincipalDetails from(Claims claims) {
		return PrincipalDetails.builder()
				.email((String) claims.get("email"))
				.nickname((String) claims.get("nickname"))
				.role((String) claims.get("role"))
				.imageUrl((String) claims.get("image_url"))
				.provider((String) claims.get("provider"))
				.build();
	}

	

}
