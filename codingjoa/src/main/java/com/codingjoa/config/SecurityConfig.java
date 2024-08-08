package com.codingjoa.config;

import org.springframework.context.annotation.Bean;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codingjoa.security.filter.JwtFilter;
import com.codingjoa.security.filter.LoginFilter;
import com.codingjoa.security.filter.OAuth2LoginFilter;
import com.codingjoa.security.oauth2.service.OAuth2CustomAuthorizationRequestResolver;
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
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserServce;
	
	/*	
	 *	Browser HTTP Request --> Security filter chain: [
	 *		WebAsyncManagerIntegrationFilter
	 * 		SecurityContextPersistenceFilter
	 * 		HeaderWriterFilter
	 * 		LogoutFilter
	 * 		LoginFilter*
	 * 		OAuth2AuthorizationRequestRedirectFilter
	 * 		OAuthLoginFilter*
	 * 		OAuth2LoginAuthenticationFilter
	 * 		JwtFilter*
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
			// SecurityContextPersistenceFilter, SessionManagementFilter, HttpSessionSecurityContextRepository
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
				.redirectionEndpoint(config -> 
					config.baseUri("/login/*/callback")
				)
				.authorizationEndpoint(config  -> {
					config.authorizationRequestResolver(authorizationRequestResolver());
					//config.authorizationRequestRepository(null);
				})
				.tokenEndpoint(config -> 
					config.accessTokenResponseClient(accessTokenResponseClient)
				)
				.userInfoEndpoint(config -> 
					config.userService(oAuth2UserServce)
				)
				//.successHandler(oAuth2LoginSuccessHandler)
				//.failureHandler(oAuth2LoginFailureHandler)
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
			.addFilterBefore(loginFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
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
		//filter.setAuthenticationManager(this.authenticationManagerBean());
		filter.setAuthenticationManager(this.authenticationManager());
		filter.setAuthenticationSuccessHandler(loginSuccessHandler);
		filter.setAuthenticationFailureHandler(loginFailureHandler);
		return filter;
	}

	private OAuth2LoginFilter oAuth2LoginFilter() throws Exception {
		OAuth2LoginFilter filter = new OAuth2LoginFilter(clientRegistrationRepository, oAuth2AuthorizedClientRepository);
		//filter.setAuthenticationManager(this.authenticationManagerBean()); 	// by AuthenticationManagerDelegator
		filter.setAuthenticationManager(this.authenticationManager());			// by ProviderManager
		filter.setAuthenticationSuccessHandler(oAuth2LoginSuccessHandler);
		filter.setAuthenticationFailureHandler(oAuth2LoginFailureHandler);
		return filter;
	}
	
	private OAuth2AuthorizationRequestResolver authorizationRequestResolver() {
		return new OAuth2CustomAuthorizationRequestResolver(clientRegistrationRepository, "/login");
	}
	
}