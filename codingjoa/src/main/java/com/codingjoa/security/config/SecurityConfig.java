package com.codingjoa.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.codingjoa.security.filter.JwtVerificationFilter;
import com.codingjoa.security.filter.RestAuthenticationFilter;
import com.codingjoa.security.service.JwtProvider;

@Configuration
@EnableWebSecurity
@ComponentScan("com.codingjoa.security.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider restAuthenticationProvider;
	
	@Autowired
	private AuthenticationSuccessHandler restAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler restAuthenticationFailureHandler;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
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
	 * 		CharacterEncodingFilter*
	 * 		LogoutFilter
	 * 		RestAuthenticationFilter*
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
				.antMatchers("/board/write", "/board/writeProc").authenticated()
				.antMatchers("/board/modify", "/board/modifyProc", "/board/deleteProc").authenticated()
				// the order of the rules matters and the more specific rules should go first --> "/api/boards/**", "/api/comments/**"
				.antMatchers("/api/boards/**/comments").permitAll()
				.antMatchers(HttpMethod.GET, "/api/boards/**/likes").permitAll()
				.antMatchers("/api/boards/**").authenticated()
				.antMatchers(HttpMethod.GET, "/api/comments/**/likes").permitAll()
				.antMatchers("/api/comments/**").authenticated() 
				//.mvcMatchers("/api/comments/**").authenticated()
				.antMatchers("/api/board/image", "/api/member/image").authenticated()
				.antMatchers("/api/member/images", "/api/member/images/**").authenticated()
				.antMatchers("/api/member/details").authenticated()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.anyRequest().permitAll()
			.and()
			.formLogin().disable()
			.addFilterBefore(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(jwtVerificationFilter(), RestAuthenticationFilter.class)
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
		auth.authenticationProvider(restAuthenticationProvider);
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
	public RestAuthenticationFilter restAuthenticationFilter() throws Exception {
		RestAuthenticationFilter filter = new RestAuthenticationFilter();
		// Error creating bean with name 'restAuthenticationFilter' defined in com.codingjoa.security.config.SecurityConfig: 
		// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
		filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
		return filter;
	}
	
	@Bean
	public JwtVerificationFilter jwtVerificationFilter() throws Exception {
		return new JwtVerificationFilter(jwtProvider);
	}

}