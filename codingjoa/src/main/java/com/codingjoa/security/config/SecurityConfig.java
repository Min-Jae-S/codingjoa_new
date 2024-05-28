package com.codingjoa.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codingjoa.security.filter.AjaxAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
@ComponentScan("com.codingjoa.security.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private AuthenticationSuccessHandler ajaxLoginSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler ajaxLoginFailureHandler;
	
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*	
	 * 	FilterChain
	 * 	https://gngsn.tistory.com/160
	 * 
	 *	Browser HTTP Request --> Security filter chain: [
	 *		WebAsyncManagerIntegrationFilter
	 * 		SecurityContextPersistenceFilter
	 * 		HeaderWriterFilter
	 * 		(CharacterEncodingFilter)
	 * 		LogoutFilter
	 * 		(CustomAuthenticationFilter)
	 * 		UsernamePasswordAuthenticationFilter
	 * 		RequestCacheAwareFilter
	 * 		SecurityContextHolderAwareRequestFilter
	 * 		AnonymousAuthenticationFilter		
	 * 		SessionManagementFilter
	 * 		ExceptionTranslationFilter(AuthenticationEntryPoint, AccessDeniedHandler)
	 * 		FilterSecurityInterceptor 
	 * 	]
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				//.filterSecurityInterceptorOncePerRequest(false)
				// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
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
			.formLogin()
				.loginPage("/login")
				.usernameParameter("memberId")
				.passwordParameter("memberPassword")
				.loginProcessingUrl("/api/login")
				.successHandler(ajaxLoginSuccessHandler)
				.failureHandler(ajaxLoginFailureHandler)
				.permitAll()
				.and()
			.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			//.addFilterBefore(logFilter(), WebAsyncManagerIntegrationFilter.class)
			//.addFilterBefore(encodingFilter(), CsrfFilter.class)
			.logout()
				.logoutUrl("/logout")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);		 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
	
	@Bean
	public AjaxAuthenticationFilter ajaxAuthenticationFilter() {
		return new AjaxAuthenticationFilter();
	}
	
}