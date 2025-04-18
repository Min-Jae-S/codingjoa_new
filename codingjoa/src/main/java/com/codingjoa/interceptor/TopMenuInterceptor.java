package com.codingjoa.interceptor;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;
import com.codingjoa.util.RequestUtils;
import com.codingjoa.util.UriUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_PREFIX = "forward:";
	private static final String REDIRECT_PREFIX = "redirect:";
	private static final String JSON_VIEW = "jsonView";
	private final List<RequestMatcher> disallowedMatchers = List.of(new AntPathRequestMatcher("/login"), new AntPathRequestMatcher("/error"));
	private final List<Category> parentCategories;
	
	public TopMenuInterceptor(CategoryService categoryService) {
		log.info("## {}", this.getClass().getSimpleName());
		this.parentCategories = categoryService.getParentCategories();
		log.info("\t > loaded parentCategories = {}", parentCategories);
	}
	
	/*
	 * If there is no mapped handler or if the mapping information cannot be found, the preHandle method is not called 
	 * Therefore, in the preHandle method, the handler parameter always points to a valid, non-null handler.
	 */
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## {}.postHandle", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", RequestUtils.getRequestLine(request));
		
		// @RestController or @ResponseBody annotation is present, the ModelAndView object will be null.
		if (modelAndView == null) {
			log.info("\t > not find top menu - no modelAndView");
			return;
		}
		
		// Return the view name to be resolved by the DispatcherServlet via a ViewResolver, or null if using a view object.
		String viewName = modelAndView.getViewName(); 
		if (viewName == null) {
			log.info("\t > not find top menu - no viewName");
			return;
		}
		
		if (viewName.startsWith(FORWARD_PREFIX) || viewName.startsWith(REDIRECT_PREFIX) || viewName.equals(JSON_VIEW)) {
			log.info("\t > not find top menu - {}", viewName);
			return;
		}
		
		modelAndView.addObject("parentCategories", parentCategories);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			modelAndView.addObject("logoutPath", buildPath(request, "/logout"));
		} else { // authentication == null || authentication instanceof AnonymousAuthenticationToken
			modelAndView.addObject("loginPath", buildPath(request, "/login"));
		}
		
		log.info("\t > added model attrs = {}", modelAndView.getModel().keySet());
	}
	
	private boolean isDisallowedPath(HttpServletRequest request) {
        return disallowedMatchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
	
	private String buildPath(HttpServletRequest request, String baseUrl) {
		String currentUrl = UriUtils.buildFullCurrentUrl(request);
		return UriComponentsBuilder
			.fromPath(request.getContextPath() + baseUrl)
			.queryParamIfPresent("continue", Optional.of(currentUrl).filter(url -> !isDisallowedPath(request)))
			.build()
			.toUriString();
	}

}
