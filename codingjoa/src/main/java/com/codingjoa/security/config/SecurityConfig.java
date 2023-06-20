package com.codingjoa.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity//(debug = true)
@ComponentScan("com.codingjoa.security.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private AuthenticationSuccessHandler loginSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler loginFailureHandler;
	
	@Autowired
	private AccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("## configure(AuthenticationManagerBuilder)");
		auth.authenticationProvider(customAuthenticationProvider);
	}
	
//	@Override
// 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//  	auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
//	}
	
//	@Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//  	auth.userDetailsService(loginIdPwValidator);
//  }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("## configure(HttpSecurity)");
		
		/*	
		 * 	FilterChain
		 * 	https://gngsn.tistory.com/160
		 * 
		 *	Browser HTTP Request --> Security filter chain: [
		 *		WebAsyncManagerIntegrationFilter
		 * 		SecurityContextPersistenceFilter
		 * 		HeaderWriterFilter
		 * 		CharacterEncodingFilter
		 * 		LogoutFilter
		 * 		UsernamePasswordAuthenticationFilter
		 * 		RequestCacheAwareFilter
		 * 		SecurityContextHolderAwareRequestFilter
		 * 		AnonymousAuthenticationFilter		
		 * 		SessionManagementFilter
		 * 		ExceptionTranslationFilter(AuthenticationEntryPoint, AccessDeniedHandler)
		 * 		FilterSecurityInterceptor
		 */
		
		http
			.csrf().disable()
			//.addFilterBefore(logFilter(), WebAsyncManagerIntegrationFilter.class)
			//.addFilterBefore(encodingFilter(), CsrfFilter.class)
			.authorizeRequests()
				//.filterSecurityInterceptorOncePerRequest(false)
				// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
				.antMatchers("/board/write", "/board/writeProc").authenticated()
				.antMatchers("/board/modify", "/board/modifyProc").authenticated()
				.antMatchers("/board/deleteProc").authenticated()
				.antMatchers("/api/comments/**").authenticated()
				//.mvcMatchers("/api/comments/**").authenticated()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.loginPage("/member/login")
				.usernameParameter("memberId")
				.passwordParameter("memberPassword")
				.loginProcessingUrl("/member/loginProc")
				.successHandler(loginSuccessHandler)
				.failureHandler(loginFailureHandler)
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/member/logout")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(customAuthenticationEntryPoint)	// XMLHttpRequest가 아닌 다른 것으로 ajax 판단하기
				.accessDeniedHandler(customAccessDeniedHandler); 			// ajax check 추가하기 			 
	}

}