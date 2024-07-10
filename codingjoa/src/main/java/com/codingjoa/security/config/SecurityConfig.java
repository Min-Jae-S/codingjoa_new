package com.codingjoa.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codingjoa.security.filter.AjaxAuthenticationFilter;
import com.codingjoa.security.filter.JwtFilter;
import com.codingjoa.security.service.AccessDeniedHandlerImpl;
import com.codingjoa.security.service.AjaxAuthenticationFailureHandler;
import com.codingjoa.security.service.AjaxAuthenticationProvider;
import com.codingjoa.security.service.AjaxAuthenticationSuccessHandler;
import com.codingjoa.security.service.AuthenticationEntryPointImpl;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.security.service.LogoutSuccessHandlerImpl;

@Configuration
@EnableWebSecurity
@ComponentScan("com.codingjoa.security.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AjaxAuthenticationProvider ajaxAuthenticationProvider;
	
	@Autowired
	private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
	
	@Autowired
	private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

	@Autowired
	private AccessDeniedHandlerImpl accessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPointImpl authenticationEntryPoint;
	
	@Autowired
	private LogoutSuccessHandlerImpl logoutSuccessHandler;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	/*	
	 * 	FilterChain
	 * 	https://gngsn.tistory.com/160
	 * 
	 *	Browser HTTP Request --> Security filter chain: [
	 *		WebAsyncManagerIntegrationFilter
	 * 		SecurityContextPersistenceFilter
	 * 		HeaderWriterFilter
	 * 		LogoutFilter
	 * 		AjaxAuthenticationFilter*
	 * 		JwtFilter*
	 * 		UsernamePasswordAuthenticationFilter
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
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
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
				
				/*
				 * @RequestMapping("/api")
				 * LikesRestController {
				 * 		@PostMapping("/boards/{boardIdx}/likes") 			toggleBoardLikes	-->	authenticated
				 * 		@GetMapping("/boards/{boardIdx}/likes") 			getBoardLikesCnt	--> permitAll
				 * 		@PostMapping("/comments/{commentIdx}/likes")		toggleCommentLikes	--> authenticated
				 * 		@GetMapping("/comments/{commentIdx}/likes")			getCommentLikesCnt	--> permitAll
				 * }
				 * 
				 * @RequestMapping("/api")
				 * CommentRestController {
				 * 		@GetMapping("/boards/{commentBoardIdx}/comments")					getCommentList		--> permitAll
				 * 		@GetMapping(value = { "/comments/", "/comments/{commentIdx}" })		getModifyComment 	--> authenticated
				 * 		@PostMapping("/comments")											writeComment		--> authenticated		
				 * 		@PatchMapping(value = { "/comments/", "/comments/{commentIdx}" })	modifyComment		--> authenticated
				 * 		@DeleteMapping(value = { "/comments/", "/comments/{commentIdx}" })	deleteComment		--> authenticated
				 * }
				 * 
				 * @RequestMapping("/api")
				 * ImageRestController {
				 * 		@PostMapping("/board/image")														uploadBoardImage		--> authenticated
				 * 		@GetMapping(value = { "/board/images/", "/board/images/{boardImageName:.+}"})		getBoardImageResource	--> permitAll
				 * 		@PostMapping("/member/image")														uploadMemberImage		--> authenticated
				 * 		@GetMapping(value = { "/member/images/", "/member/images/{memberImageName:.+}"})	getMemberImageResource	--> authenticated
				 * }
				 * 
				 */
				
				.antMatchers(HttpMethod.POST, "/api/boards/*/likes", "/api/comments/*/likes").authenticated()
				.antMatchers("/api/comments", "/comments/", "/api/comments/*").authenticated()
				.antMatchers(HttpMethod.POST, "/api/board/image", "/api/member/image").authenticated()
				.antMatchers("/api/member/images", "/api/member/images/*").authenticated()
				.antMatchers("/api/member/details").authenticated()
				.antMatchers("/test/jwt/test7", "/test/jwt/test8").authenticated()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.anyRequest().permitAll()
			.and()
			.formLogin().disable()
			.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(jwtFilter(), AjaxAuthenticationFilter.class)
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
		auth.authenticationProvider(ajaxAuthenticationProvider);
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
	public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
		AjaxAuthenticationFilter filter = new AjaxAuthenticationFilter();
		// Error creating bean with name 'ajaxAuthenticationFilter' defined in com.codingjoa.security.config.SecurityConfig: 
		// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
		filter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
		return filter;
	}
	
	@Bean
	public JwtFilter jwtFilter() throws Exception {
		JwtFilter filter = new JwtFilter(jwtProvider);
		filter.addIncludeMatchers("/member/account/**");
		filter.addIncludeMatchers("/board/write", "/board/writeProc", "/board/modify", "/board/modifyProc", "/board/deleteProc");
		filter.addIncludeMatchers(HttpMethod.POST, "/api/boards/*/likes", "/api/comments/*/likes");
		filter.addIncludeMatchers("/api/comments", "/comments/", "/api/comments/*");
		filter.addIncludeMatchers(HttpMethod.POST, "/api/board/image", "/api/member/image");
		filter.addIncludeMatchers("/api/member/images", "/api/member/images/*");
		filter.addIncludeMatchers("/api/member/details");
		filter.addIncludeMatchers("/test/jwt/test7", "/test/jwst/test8");
		return filter;
	}
	
}