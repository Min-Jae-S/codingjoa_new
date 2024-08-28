package com.codingjoa.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	private final List<GrantedAuthority> authorities;	// INNER JOIN auth

	@Builder
	private PrincipalDetails(Integer idx, String email, String password, String nickname, String imageUrl,
			String provider, List<GrantedAuthority> authorities) {
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
	
	private static List<GrantedAuthority> convert(List<String> memberRoles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(String role : memberRoles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	private static List<GrantedAuthority> convert(String roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles.split(",")) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	@SuppressWarnings("unchecked")
	public static PrincipalDetails from(Map<String, Object> map) { // from DB
		List<String> memberRoles = (List<String>) map.get("memberRoles");
		return PrincipalDetails.builder()
				.idx((Integer) map.get("memberIdx"))
				.email((String) map.get("memberEmail"))
				.password((String) map.get("memberPassword"))
				.nickname((String) map.get("memberNickname"))
				.imageUrl((String) map.get("memberImageUrl"))
				.provider((String) map.get("snsProvider"))
				.authorities(convert(memberRoles))
				.build();
	}

	public static PrincipalDetails from(Claims claims) { // from JWT
		String roles = (String) claims.get("roles");
		return PrincipalDetails.builder()
				.idx(Integer.parseInt(claims.getSubject()))
				.email((String) claims.get("email"))
				.nickname((String) claims.get("nickname"))
				.imageUrl((String) claims.get("image_url"))
				.provider((String) claims.get("provider"))
				.authorities(convert(roles))
				.build();
	}

}
