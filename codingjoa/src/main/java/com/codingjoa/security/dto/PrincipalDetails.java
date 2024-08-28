package com.codingjoa.security.dto;

import java.util.Collection;
import java.util.HashSet;
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
public class PrincipalDetails implements UserDetails, OAuth2User {

	private final Integer idx;							// FROM member
	private final String email;					
	private final String password;
	private final String nickname;
	private final String imageUrl;						// LEFT OUTER JOIN member_iamge
	private final String provider;						// LEFT OUTER JOIN sns_info
	private final Set<GrantedAuthority> authorities;	// INNER JOIN auth

	@Builder
	private PrincipalDetails(Integer idx, String email, String password, String nickname, String imageUrl,
			String provider, Set<GrantedAuthority> authorities) {
		this.idx = idx;
		this.email = email;
		this.password = (password != null) ? password : "";
		this.nickname = nickname;
		this.imageUrl = (imageUrl != null) ? imageUrl : "";
		this.provider = (provider != null) ? provider : "local";
		this.authorities = authorities;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

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
	
	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}
	
	private static Set<GrantedAuthority> convertToAuthorites(Set<String> memberRoles) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for(String role : memberRoles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	private static Set<GrantedAuthority> convertToAuthorites(String roles) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (String role : roles.split(",")) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	@SuppressWarnings("unchecked")
	public static PrincipalDetails from(Map<String, Object> map) { // from database
		Set<String> memberRoles = (Set<String>) map.get("memberRoles");
		return PrincipalDetails.builder()
				.idx((Integer) map.get("memberIdx"))
				.email((String) map.get("memberEmail"))
				.password((String) map.get("memberPassword"))
				.nickname((String) map.get("memberNickname"))
				.imageUrl((String) map.get("memberImageUrl"))
				.provider((String) map.get("snsProvider"))
				.authorities(convertToAuthorites(memberRoles))
				.build();
	}

	public static PrincipalDetails from(Claims claims) { // from jwt
		String roles = (String) claims.get("roles");
		return PrincipalDetails.builder()
				.idx(Integer.parseInt(claims.getSubject()))
				.email((String) claims.get("email"))
				.nickname((String) claims.get("nickname"))
				.imageUrl((String) claims.get("image_url"))
				.provider((String) claims.get("provider"))
				.authorities(convertToAuthorites(roles))
				.build();
	}

}
