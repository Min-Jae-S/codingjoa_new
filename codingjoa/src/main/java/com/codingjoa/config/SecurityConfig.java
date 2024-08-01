package com.codingjoa.config;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.codingjoa.security.filter.JwtFilter;
import com.codingjoa.security.filter.LoginFilter;
import com.codingjoa.security.filter.OAuth2LoginFilter;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.security.service.LoginFailureHandler;
import com.codingjoa.security.service.LoginProvider;
import com.codingjoa.security.service.LoginSuccessHandler;
import com.codingjoa.security.service.OAuth2LoginFailureHandler;
import com.codingjoa.security.service.OAuth2LoginProvider;
import com.codingjoa.security.service.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Import(OAuth2Config.class)
@ComponentScan("com.codingjoa.security.service")
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final LogoutSuccessHandler logoutSuccessHandler;
	private final JwtProvider jwtProvider;
	private final ClientRegistrationRepository clientRegistrationRepository;
	private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserServce;
	
	/*	
	 *	Browser HTTP Request --> Security filter chain: [
	 *		WebAsyncManagerIntegrationFilter
	 * 		SecurityContextPersistenceFilter
	 * 		HeaderWriterFilter
	 * 		LogoutFilter
	 * 		OAuth2AuthorizationRequestRedirectFilter
	 * 		LoginFilter*
	 * 		JwtFilter*
	 * 		OAuth2LoginAuthenticationFilter
	 * 		RequestCacheAwareFilter
	 * 		SecurityContextHolderAwareRequestFilter
	 * 		AnonymousAuthenticationFilter		
	 * 		SessionManagementFilter
	 * 		ExceptionTranslationFilter - AuthenticationEntryPoint, AccessDeniedHandler
	 * 		FilterSecurityInterceptor - AuthenticationException, AccessDeniedException 
	 * 	]
	 */
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
		web.ignoring().antMatchers("/resources/**");
		//web.debug(true);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
				//.filterSecurityInterceptorOncePerRequest(false)
				.antMatchers("/member/account/**").authenticated()
				.antMatchers("/board/write", "/board/writeProc", "/board/modify", "/board/modifyProc", "/board/deleteProc").authenticated()
				// the order of the rules matters and the more specific rules should go first
				//.antMatchers("/api/comments/**/likes").permitAll()
				//.antMatchers("/api/comments/**").authenticated()
				.antMatchers(HttpMethod.POST, "/api/boards/*/likes", "/api/comments/*/likes").authenticated()
				.antMatchers("/api/comments", "/comments/", "/api/comments/*").authenticated()
				.antMatchers(HttpMethod.POST, "/api/board/image", "/api/member/image").authenticated()
				.antMatchers("/api/member/images", "/api/member/images/*").authenticated()
				.antMatchers("/api/member/details").authenticated()
				.antMatchers("/test/jwt/test7", "/test/jwt/test8").authenticated()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.anyRequest().permitAll()
				.and()
			.oauth2Login()
				.clientRegistrationRepository(clientRegistrationRepository)
				.redirectionEndpoint(config -> config.baseUri("/login/*/callback"))
				.authorizationEndpoint(config  -> {
					config.baseUri("/login/*");
					config.authorizationRequestResolver(authorizationRequestResolver());
				})
				//.tokenEndpoint(config -> config.accessTokenResponseClient(accessTokenResponseClient))
				//.userInfoEndpoint(config -> config.userService(null))
				.successHandler(oAuth2LoginSuccessHandler)
				.failureHandler(oAuth2LoginFailureHandler)
				.and()
			.logout()
				//.logoutUrl("/api/logout")
				//.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST"))
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.logoutSuccessHandler(logoutSuccessHandler)
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
				.and()
			// https://velog.io/@tmdgh0221/Spring-Security-%EC%99%80-OAuth-2.0-%EC%99%80-JWT-%EC%9D%98-%EC%BD%9C%EB%9D%BC%EB%B3%B4
			// add it right after the LogoutFilter, which is the point just before the actual authentication process takes place.
			.addFilterBefore(loginFilter(), OAuth2LoginAuthenticationFilter.class)
			.addFilterBefore(oAuth2LoginFilter(), OAuth2LoginAuthenticationFilter.class)
			.addFilterAfter(new JwtFilter(jwtProvider), OAuth2LoginAuthenticationFilter.class);
	}
	
	@Override // register provider with AuthenticationManager
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new LoginProvider(userDetailsService, passwordEncoder()));
		auth.authenticationProvider(new OAuth2LoginProvider(accessTokenResponseClient, oAuth2UserServce));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private LoginFilter loginFilter() throws Exception {
		LoginFilter filter = new LoginFilter();
		// Error creating bean with name 'loginFilter' defined in com.codingjoa.security.config.SecurityConfig: 
		// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified
		filter.setAuthenticationManager(authenticationManager());
		//filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(loginSuccessHandler);
		filter.setAuthenticationFailureHandler(loginFailureHandler);
		return filter;
	}

	private OAuth2LoginFilter oAuth2LoginFilter() throws Exception {
		OAuth2LoginFilter filter = new OAuth2LoginFilter(clientRegistrationRepository, oAuth2AuthorizedClientService);
		filter.setAuthenticationManager(authenticationManager());			// ProviderManager
		//filter.setAuthenticationManager(authenticationManagerBean()); 	// AuthenticationManagerDelegator
		return filter;
	}
	
	private OAuth2AuthorizationRequestResolver authorizationRequestResolver() {
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/login");
		resolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());
		return resolver;
	}
	
	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> {
			log.info("## AuthorizationRequestCustomizer");
			log.info("\t > customize authorizationRequestUri (fully encoded redirect_uri, adding parameter)");

			OAuth2AuthorizationRequest authorizationRequest = customizer.build();
			String customizedAuthorizationRequestUri = customizeAuthorizationRequestUri(authorizationRequest);
			customizer.authorizationRequestUri(customizedAuthorizationRequestUri);
		};
	}
	
	private String customizeAuthorizationRequestUri(OAuth2AuthorizationRequest authorizationRequest) {
		String authorizationRequestUri = authorizationRequest.getAuthorizationRequestUri();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authorizationRequestUri);
		
		String authorizationResponseUri = builder.build().getQueryParams().getFirst("redirect_uri");
		String decodedAuthorizationResponseUri = UriUtils.decode(authorizationResponseUri, StandardCharsets.UTF_8);
		String encodedAuthorizationResponseUri = UriUtils.encode(decodedAuthorizationResponseUri, StandardCharsets.UTF_8);
		builder.replaceQueryParam("redirect_uri", encodedAuthorizationResponseUri);
		
//		String registrationId = (String) authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
//		if (registrationId.equals("kakao")) {
//			builder.queryParam("prompt", "login");
//		}
		
		return builder.build().toUriString();
	}
	
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception { 
//		return super.authenticationManagerBean();
//	}
	
//	@Override
//	protected AuthenticationManager authenticationManager() throws Exception { // ProviderManager
//		log.info("## authenticationManager");
//		AuthenticationManager authenticationManager = super.authenticationManager();
//		log.info("\t > authenticationManager = {}", authenticationManager.getClass().getSimpleName());
//		
//		if (authenticationManager instanceof ProviderManager) {
//			ProviderManager providerManager = (ProviderManager) authenticationManager;
//			providerManager.getProviders().forEach(provider -> {
//				log.info("\t > registerd provider = {}", provider);
//			});
//		}
//		return authenticationManager;
//	}
	
}