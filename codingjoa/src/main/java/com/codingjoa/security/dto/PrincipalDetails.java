package com.codingjoa.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
	private final String provider;						// LEFT OUTER JOIN sns_info
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
		this.provider = (provider == null) ? "local" : provider;
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
	
	@SuppressWarnings("unchecked")
	public static PrincipalDetails from(Map<String, Object> map) { // from database
		List<String> roles = (List<String>) map.get("roles");
		return PrincipalDetails.builder()
				.id((long) map.get("id"))
				.email((String) map.get("email"))
				.password((String) map.get("password"))
				.nickname((String) map.get("nickname"))
				.imagePath((String) map.get("imagePath"))
				.provider((String) map.get("provider"))
				.authorities(convert(roles))
				.build();
	}

	public static PrincipalDetails from(PrincipalDetails principalDetails, Map<String, Object> attributes,
			String nameAttributeKey) { 
		principalDetails.setAttributes(attributes);
		principalDetails.setNameAttributeKey(nameAttributeKey);
		return principalDetails;
	}

	public static PrincipalDetails from(Claims claims) { // from JWT
		String roles = (String) claims.get("roles");
		return PrincipalDetails.builder()
				.id(Long.parseLong(claims.getSubject()))
				.email((String) claims.get("email"))
				.nickname((String) claims.get("nickname"))
				.imagePath((String) claims.get("image_path"))
				.provider((String) claims.get("provider"))
				.authorities(convert(roles))
				.build();
	}
	
	private void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	private void setNameAttributeKey(String nameAttributeKey) {
		this.nameAttributeKey = nameAttributeKey;
	}
	
	private static List<GrantedAuthority> convert(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
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
}
