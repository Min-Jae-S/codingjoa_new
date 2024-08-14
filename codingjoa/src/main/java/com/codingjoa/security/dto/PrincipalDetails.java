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

import com.codingjoa.entity.Member;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PrincipalDetails implements UserDetails, OAuth2User { // consider implementing OAuth2User 

	private final Member member;
	private final String role;					// INNER JOIN auth	
	private final String imageUrl;				// LEFT OUTER JOIN member_iamge
	private final List<Integer> myBoardLikes;	// LEFT OUTER JOIN board_likes
	private final List<Integer> myCommentLikes;	// LEFT OUTER JOIN comment_likes

	private final String id;
	private final String password;
	private final String email;
	private final String provider;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
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
		Member member = (Member) map.get("member");
		return PrincipalDetails.builder()
				.member(member)
				.role((String) map.get("memberRole"))
				.imageUrl((String) map.get("memberImageUrl"))
				.myBoardLikes((List<Integer>) map.get("myBoardLikes"))
				.myCommentLikes((List<Integer>) map.get("myCommentLikes"))
				.id(member.getMemberId())
				.password(member.getMemberPassword())
				.email(member.getMemberEmail())
				.provider(null)
				.build();
	}

	public static PrincipalDetails from(Claims claims) {
		return PrincipalDetails.builder()
				.id(claims.getSubject())
				.role((String) claims.get("role"))
				.email((String) claims.get("email"))
				.imageUrl((String) claims.get("image_url"))
				.provider((String) claims.get("provider"))
				.build();
	}

}
