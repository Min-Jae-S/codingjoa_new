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
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;
import com.codingjoa.service.MemberService;
import com.codingjoa.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String INVALID_REGISTRATION_ID_ERROR_CODE = "invalid_registration_id";
	private static final String MISSING_EMAIL_RESPONSE_ERROR_CODE = "missing_email_response";
	private static final String MISSING_NICKNAME_RESPONSE_ERROR_CODE = "missing_nickname_response";
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	private final MemberService memberService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > request for userInfo");
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		
		Map<String, Object> attributes = loadedOAuth2User.getAttributes();
		log.info("\t > received userInfo response = {}", Utils.formatPrettyJson(attributes));
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String attributeKeyName = userRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, attributeKeyName, attributes);
		log.info("\t > oAuth2Attributes = {}", Utils.formatPrettyJson(oAuth2Attributes));
		
		if (oAuth2Attributes == null) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_REGISTRATION_ID_ERROR_CODE,
					"An error occurred due to invalid registrationId: " + registrationId, null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		
		String email = oAuth2Attributes.getEmail();
		if (!StringUtils.hasText(email)) {
			OAuth2Error oAuth2Error = new OAuth2Error(MISSING_EMAIL_RESPONSE_ERROR_CODE, 
					"Missing required 'email' attribute", null);
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
		
		PrincipalDetails principalDetails = memberService.getUserDetailsByEmail(email);
		
		if (principalDetails == null) {
			log.info("\t > proceed with the registration");

			String nickname = oAuth2Attributes.getNickname();
			if (!StringUtils.hasText(nickname)) {
				OAuth2Error oAuth2Error = new OAuth2Error(MISSING_NICKNAME_RESPONSE_ERROR_CODE, 
						"Missing required 'nickname' attribute", null);
				throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
			}
			
			// to apply transactions, move the OAuth2Member save logic to "memberService"
			memberService.saveOAuth2Member(oAuth2Attributes);
			principalDetails = memberService.getUserDetailsByEmail(email);
		}
		
		principalDetails.setAttributes(attributes);
		principalDetails.setNameAttributeKey(attributeKeyName);
		
		return principalDetails;
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
