package com.codingjoa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.security.filter.JwtFilter;
import com.codingjoa.security.filter.JwtMathcerFilter;
import com.codingjoa.security.filter.LoginFilter;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.security.service.LoginFailureHandler;
import com.codingjoa.security.service.LoginProvider;
import com.codingjoa.security.service.LoginSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Import(OAuth2Config.class)
@ComponentScan("com.codingjoa.security.service")
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginProvider loginProvider;
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private LoginFailureHandler loginFailureHandler;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Qualifier("mainClientRegistrationRepository")
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	
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
				.authorizationEndpoint()
					.authorizationRequestResolver(authorizationRequestResolver(this.clientRegistrationRepository))
					.and()
				.redirectionEndpoint()
					.and()
				.clientRegistrationRepository(clientRegistrationRepository)
				.and()
			// https://velog.io/@tmdgh0221/Spring-Security-%EC%99%80-OAuth-2.0-%EC%99%80-JWT-%EC%9D%98-%EC%BD%9C%EB%9D%BC%EB%B3%B4
			// add it right after the LogoutFilter, which is the point just before the actual authentication process takes place.
			.addFilterBefore(loginFilter(), OAuth2LoginAuthenticationFilter.class)
			.addFilterAfter(jwtFilter(), LogoutFilter.class)
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
				.accessDeniedHandler(accessDeniedHandler);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(loginProvider);
	}
	
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LoginFilter loginFilter() throws Exception {
		LoginFilter filter = new LoginFilter();
		// Error creating bean with name 'ajaxAuthenticationFilter' defined in com.codingjoa.security.config.SecurityConfig: 
		// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(loginSuccessHandler);
		filter.setAuthenticationFailureHandler(loginFailureHandler);
		return filter;
	}
	
	@Bean
	public JwtFilter jwtFilter() throws Exception {
		return new JwtFilter(jwtProvider);
	}
	
	@Bean
	public JwtMathcerFilter jwtMathcerFilter() throws Exception {
		JwtMathcerFilter filter = new JwtMathcerFilter(jwtProvider);
		filter.addIncludeMatchers("/member/account/**");
		filter.addIncludeMatchers("/board/write", "/board/writeProc", "/board/modify", "/board/modifyProc", "/board/deleteProc");
		filter.addIncludeMatchers(HttpMethod.POST, "/api/boards/*/likes", "/api/comments/*/likes");
		filter.addIncludeMatchers("/api/comments", "/comments/", "/api/comments/*");
		filter.addIncludeMatchers(HttpMethod.POST, "/api/board/image", "/api/member/image");
		filter.addIncludeMatchers("/api/member/images", "/api/member/images/*");
		filter.addIncludeMatchers("/api/member/details");
		filter.addIncludeMatchers("/test/jwt/test7", "/test/jwt/test8");
		return filter;
	}
	
	private OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/login");
		resolver.setAuthorizationRequestCustomizer(customizer -> {
			customizer.authorizationRequestUri(uriBuilder -> {
				UriComponents originalComponents = ((UriComponentsBuilder) uriBuilder).build();
				log.info("\t > {}", originalComponents.getQueryParams().get("redirect_uri"));
				return originalComponents.toUri();
			});
		});
		
		return resolver;
	}
	
	
}