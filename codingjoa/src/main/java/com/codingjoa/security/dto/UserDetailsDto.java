package com.codingjoa.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codingjoa.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto implements UserDetails {

	private Member member;
	private String memberRole;
	private String memberImageName;
	private List<Integer> myBoardLikes;
	private List<Integer> myCommentLikes;

	//@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new SimpleGrantedAuthority(memberRole));
		return collection;
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
}
