package com.codingjoa.security.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.codingjoa.security.oauth2.OAuth2Attributes;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

	private final long id;
	private final String email;					
	private final String password;
	private final String nickname;
	private final String imagePath;						// LEFT OUTER JOIN user_image
	private final List<GrantedAuthority> authorities;	// INNER JOIN auth
	private Map<String, Object> attributes;				// OAuth2User
	private String nameAttributeKey;

	@Builder
	private PrincipalDetails(Long id, String email, String password, String nickname, String imagePath,
			String provider, List<GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.password = (password == null) ? "" : password;
		this.nickname = nickname;
		this.imagePath = (imagePath == null) ? "" : imagePath;
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
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@JsonIgnore
	@Override
	public String getName() {
		return this.getAttributes().get(this.nameAttributeKey).toString();
	}
	
	// from DB (userDetailsMap)
	@SuppressWarnings("unchecked")
	public static PrincipalDetails from(Map<String, Object> map) { 
		List<String> roles = (List<String>) map.get("roles");
		return PrincipalDetails.builder()
				.id((long) map.get("id"))
				.email((String) map.get("email"))
				.password((String) map.get("password"))
				.nickname((String) map.get("nickname"))
				.imagePath((String) map.get("imagePath"))
				.authorities(toGrantedAuthorities(roles))
				.build();
	}
	
	// from JWT
	public static PrincipalDetails from(Claims claims) { 
		String roles = (String) claims.get("roles");
		return PrincipalDetails.builder()
				.id(Long.parseLong(claims.getSubject()))
				.email((String) claims.get("email"))
				.nickname((String) claims.get("nickname"))
				.imagePath((String) claims.get("image_path"))
				.authorities(toGrantedAuthorities(roles))
				.build();
	}

	// from OAuth2
	public static PrincipalDetails from(PrincipalDetails principalDetails, OAuth2Attributes oAuth2Attributes) {
		principalDetails.setAttributes(oAuth2Attributes.getAttributes());
		principalDetails.setNameAttributeKey(oAuth2Attributes.getNameAttributeKey());
		return principalDetails;
	}
	
	private void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	private void setNameAttributeKey(String nameAttributeKey) {
		this.nameAttributeKey = nameAttributeKey;
	}
	
	// from DB(UserDetailsMap) to PrincipalDetails
	private static List<GrantedAuthority> toGrantedAuthorities(List<String> roles) {
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(role))
			.collect(Collectors.toList());
	}

	// from JWT to PrincipalDetails
	private static List<GrantedAuthority> toGrantedAuthorities(String roles) {
		return Arrays.stream(roles.split(","))
			.map(role -> new SimpleGrantedAuthority(role))
			.collect(Collectors.toList());
	}
	
}
