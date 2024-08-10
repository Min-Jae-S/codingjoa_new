package com.codingjoa.security.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class UserDetailsDto implements UserDetails { // consider implementing OAuth2User 

	private Member member;						// from member
	private String memberRole;					// inner join auth
	private String memberImageUrl;				// left outer join member_image
	private List<Integer> myBoardLikes;			// left outer join board_likes
	private List<Integer> myCommentLikes;		// left outer join comment_likes

	//@JsonIgnore
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
}
