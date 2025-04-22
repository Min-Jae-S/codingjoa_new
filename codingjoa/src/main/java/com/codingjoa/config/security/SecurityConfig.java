package com.codingjoa.config.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codingjoa.security.filter.JwtAuthenticationFilter;
import com.codingjoa.security.filter.LoginAuthenticationFilter;
import com.codingjoa.security.filter.OAuth2AppLoginAuthenticationFilter;
import com.codingjoa.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.codingjoa.security.oauth2.OAuth2CustomAuthorizationRequestResolver;
import com.codingjoa.security.oauth2.service.OAuth2LoginFailureHandler;
import com.codingjoa.security.oauth2.service.OAuth2LoginProvider;
import com.codingjoa.security.oauth2.service.OAuth2LoginSuccessHandler;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.security.service.LoginFailureHandler;
import com.codingjoa.security.service.LoginProvider;
import com.codingjoa.security.service.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.security")
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final LogoutSuccessHandler logoutSuccessHandler;
	private final JwtProvider jwtProvider;
	private final ClientRegistrationRepository clientRegistrationRepository;
	private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
	
	/*	
	 *	Browser HTTP Request --> Security filter chain: [
	 *		WebAsyncManagerIntegrationFilter
	 * 		SecurityContextPersistenceFilter
	 * 		HeaderWriterFilter
	 * 		LogoutFilter
	 * 		LoginAuthenticationFilter*
	 * 		OAuth2AuthorizationRequestRedirectFilter
	 * 		OAuth2AppLoginAuthenticationFilter*
	 * 		OAuth2LoginAuthenticationFilter
	 * 		JwtAuthenticationFilter*
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
		web.ignoring().antMatchers("/resources/**", "/user/images/**", "/board/images/**", "/favicon.ico");
		//web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**");
		//web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
		//web.debug(true);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//.httpBasic().disable()
			.formLogin().disable()
			.csrf().disable()
			// SecurityContextPersistenceFilter, SessionManagementFilter, HttpSessionSecurityContextRepository
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
				//.filterSecurityInterceptorOncePerRequest(false)
				.antMatchers("/user/account/**", "/api/users/**").authenticated()
				.antMatchers("/board/write", "/board/modify", "/board/delete").authenticated()
				// the order of the rules matters and the more specific rules should go first
				//.antMatchers("/api/comments/**/likes").permitAll()
				//.antMatchers("/api/comments/**").authenticated()
				.antMatchers(HttpMethod.POST, "/api/boards/*/likes", "/api/comments/*/likes").authenticated()
				.antMatchers("/api/comments", "/comments/", "/api/comments/*").authenticated()
				.antMatchers(HttpMethod.POST, "/api/board/image").authenticated()
				.antMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
				.antMatchers("/swagger-ui/**").hasRole("ADMIN")
				.anyRequest().permitAll()
				.and()
			.oauth2Login()
				.clientRegistrationRepository(clientRegistrationRepository)
				.authorizationEndpoint(config  -> {
					config.authorizationRequestResolver(authorizationRequestResolver());
					config.authorizationRequestRepository(authorizationRequestRepository());
				})
				.redirectionEndpoint(config -> 
					config.baseUri("/login/*/callback")
				)
				.tokenEndpoint(config -> 
					config.accessTokenResponseClient(accessTokenResponseClient)
				)
				.userInfoEndpoint(config -> 
					config.userService(oAuth2UserService)
				)
				//.successHandler(oAuth2LoginSuccessHandler)
				//.failureHandler(oAuth2LoginFailureHandler)
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST"))
				.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("ACCESS_TOKEN", "AUTHORIZATION_REQUEST")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
				.and()
			// https://velog.io/@tmdgh0221/Spring-Security-%EC%99%80-OAuth-2.0-%EC%99%80-JWT-%EC%9D%98-%EC%BD%9C%EB%9D%BC%EB%B3%B4
			// add it right after the LogoutFilter, which is the point just before the actual authentication process takes place.
			.addFilterBefore(loginAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
			.addFilterBefore(oAuth2AppLoginAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
			.addFilterAfter(new JwtAuthenticationFilter(jwtProvider), OAuth2LoginAuthenticationFilter.class);
	}
	
	@Override // register provider with AuthenticationManager
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new LoginProvider(userDetailsService, passwordEncoder));
		auth.authenticationProvider(new OAuth2LoginProvider(accessTokenResponseClient, oAuth2UserService));
	}
	
	private LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
		LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
		// Error creating bean with name 'loginFilter' defined in com.codingjoa.security.config.SecurityConfig: 
		// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified
		//filter.setAuthenticationManager(this.authenticationManagerBean());
		filter.setAuthenticationManager(this.authenticationManager());
		filter.setAuthenticationSuccessHandler(loginSuccessHandler);
		filter.setAuthenticationFailureHandler(loginFailureHandler);
		return filter;
	}

	private OAuth2AppLoginAuthenticationFilter oAuth2AppLoginAuthenticationFilter() throws Exception {
		OAuth2AppLoginAuthenticationFilter filter = new OAuth2AppLoginAuthenticationFilter(clientRegistrationRepository, oAuth2AuthorizedClientRepository);
		//filter.setAuthenticationManager(this.authenticationManagerBean()); 	// by AuthenticationManagerDelegator
		filter.setAuthenticationManager(this.authenticationManager());			// by ProviderManager
		filter.setAuthenticationSuccessHandler(oAuth2LoginSuccessHandler);
		filter.setAuthenticationFailureHandler(oAuth2LoginFailureHandler);
		return filter;
	}
	
	private OAuth2AuthorizationRequestResolver authorizationRequestResolver() {
		return new OAuth2CustomAuthorizationRequestResolver(clientRegistrationRepository, "/login");
	}
	
	// ref) HttpSessionOAuth2AuthorizationRequestRepository
	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}
	
}