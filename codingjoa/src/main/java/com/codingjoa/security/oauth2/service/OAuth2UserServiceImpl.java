package com.codingjoa.security.oauth2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;
import com.codingjoa.service.UserService;
import com.codingjoa.util.FormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private final UserService memberService;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > request for userInfo");
		
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		Map<String, Object> attributes = loadedOAuth2User.getAttributes();
		log.info("\t > received userInfo: {}", FormatUtils.formatPrettyJson(attributes));
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String attributeKeyName = userRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, attributeKeyName, attributes);
		log.info("\t > created oAuth2Attributes: {}", FormatUtils.formatPrettyJson(oAuth2Attributes));
		
		String email = oAuth2Attributes.getEmail();
		PrincipalDetails principal = memberService.getUserDetailsByEmail(email);
		log.info("\t > principal = {}", principal);
		
		if (principal == null) {
			log.info("\t > register new member using OAuth2 account");
			// to apply spring transaction, move save logic to "memberService"
			memberService.saveOAuth2Member(oAuth2Attributes);
			principal = memberService.getUserDetailsByEmail(email);
		} else if (principal.getProvider().equals("local")) {
			log.info("\t > connect existing member with OAuth2 account");
			memberService.connectOAuth2Member(oAuth2Attributes, principal.getId());
			principal = memberService.getUserDetailsByEmail(email);
		} else {
			log.info("\t > proceed with login using already registered member");
		}
		
		return PrincipalDetails.from(principal, attributes, attributeKeyName);
	}
	
	@SuppressWarnings("unused")
	private OAuth2User resolveOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		List<GrantedAuthority> mappedAuthorities = new ArrayList<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), userNameAttributeName);
	}
	
}
