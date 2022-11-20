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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@ComponentScan("com.codingjoa.security.service")
@EnableWebSecurity//(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	AuthenticationSuccessHandler loginSuccessHandler;
	
	@Autowired
	AuthenticationFailureHandler loginFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CharacterEncodingFilter encodingFilter() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		return encodingFilter;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/*	FilterChain
		 * 
		 *	Browser HTTP Request
		 *		--> SecurityContextPersistenceFilter 
		 * 		--> HeaderWriterFilter
		 * 		--> CsrfFilter
		 * 		--> LogoutFilter
		 * 		--> UsernamePasswordAuthenticationFilter
		 * 		--> ...
		 */
		
		http
			.csrf().disable()
			.addFilterBefore(encodingFilter(), CsrfFilter.class)
			.authorizeRequests()
				//.filterSecurityInterceptorOncePerRequest(false)
				// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
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
				.accessDeniedPage("/accessDenied");
	}
	
}