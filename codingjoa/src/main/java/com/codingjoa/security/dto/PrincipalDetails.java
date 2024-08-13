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

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
@Builder
public class PrincipalDetails implements UserDetails, OAuth2User { // consider implementing OAuth2User 

	private Member member;
	private String memberId;
	private String memberEmail;
	private String memberRole;	
	private String memberImageUrl;
	private String provider;
	private List<Integer> myBoardLikes;
	private List<Integer> myCommentLikes;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(memberRole));
		return authorities;
	}

	@Override
	public String getUsername() {
		return member.getMemberId();
	}

	@Override
	public String getPassword() {
		return member.getMemberPassword();
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

}
