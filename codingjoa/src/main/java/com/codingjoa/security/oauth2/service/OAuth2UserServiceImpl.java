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
	
	private final UserService userService;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > request for userInfo");
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		Map<String, Object> attributes = loadedOAuth2User.getAttributes();
		log.info("\t > received userInfo: {}", FormatUtils.formatPrettyJson(attributes));
		
		OAuth2Attributes oAuth2Attributes = extractOAuth2Attributes(userRequest, attributes);
		log.info("\t > extract oAuth2Attributes: {}", FormatUtils.formatPrettyJson(oAuth2Attributes));
		
		String email = oAuth2Attributes.getEmail();
		PrincipalDetails principal = userService.getUserDetailsByEmail(email);
		log.info("\t > principal = {}", principal);
		
		// to apply spring transaction, move logic to UserService
		if (principal == null) {
			log.info("\t > no existing user found. Registering new user with OAuth2 account");
			userService.saveOAuth2User(oAuth2Attributes);
			principal = userService.getUserDetailsByEmail(email);
		}
		
//		} else if ("codingjoa".equals(principal.getProvider())) {
//			log.info("\t > existing user found with local account. Linking OAuth2 account to existing user");
//			userService.connectOAuth2User(oAuth2Attributes, principal.getId());
//			principal = userService.getUserDetailsByEmail(email);
//		} else {
//			log.info("\t > OAuth2 account is already linked to the existing user. Proceeding with login");
//		}
		
		return PrincipalDetails.from(principal, oAuth2Attributes.getAttributes(), oAuth2Attributes.getNameAttributeKey());
	}
	
	private OAuth2Attributes extractOAuth2Attributes(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String attributeKeyName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        return OAuth2Attributes.of(registrationId, attributeKeyName, attributes);
    }
	
	@SuppressWarnings("unused")
	private OAuth2User resolveOAuth2User(OAuth2UserRequest userRequest, OAuth2User loadedOAuth2User) {
		List<GrantedAuthority> mappedAuthorities = new ArrayList<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		return new DefaultOAuth2User(mappedAuthorities, loadedOAuth2User.getAttributes(), userNameAttributeName);
	}
	
}
