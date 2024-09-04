package com.codingjoa.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;
import com.codingjoa.util.FormatUtils;
import com.codingjoa.util.UriUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private static final String JSON_VIEW = "jsonView";
	private final AntPathRequestMatcher loginMatcher = new AntPathRequestMatcher("/login", "GET");
	private final AntPathRequestMatcher errorMatcher = new AntPathRequestMatcher("/error", "GET");
	private final CategoryService categoryService;
	
	/*
	 * If there is no mapped handler or if the mapping information cannot be found, the preHandle method is not called 
	 * Therefore, in the preHandle method, the handler parameter always points to a valid, non-null handler.
	 */
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## {}.postHandle", this.getClass().getSimpleName());
		
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
		
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			log.info("\t > not find top menu - FORWARD_URL_PREFIX");
			return;	
		}
		
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) 	{
			log.info("\t > not find top menu - REDIRECT_URL_PREFIX");
			return;
		}
		
		if (viewName.equals(JSON_VIEW)) {
			log.info("\t > not find top menu - JSON_VIEW");
			return;
		}
		
		List<Category> parentCategoryList = categoryService.getParentCategoryList();
		modelAndView.addObject("parentCategoryList", parentCategoryList);
		
		String currentUrl = UriUtils.buildCurrentUrl(request);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			modelAndView.addObject("logoutCurrentUrl", currentUrl);
			log.info("\t > set logoutCurrentUrl to the current request URL: {}", FormatUtils.formatString(currentUrl));
			log.info("\t > added model attrs = {}", modelAndView.getModel().keySet());
			return;
		}
		
		String loginCurrentUrl = "";
		if (!loginMatcher.matches(request) && !errorMatcher.matches(request)) {
			loginCurrentUrl = currentUrl;
			log.info("\t > not matching loginPattern or errorPattern");
			log.info("\t > set loginCurrentUrl to the current request URL: {}", FormatUtils.formatString(currentUrl));
 		} else {
 			log.info("\t > matching loginPattern or errorPattern, set loginCurrentUrl to an empty string");
		}
		
		modelAndView.addObject("loginCurrentUrl", loginCurrentUrl);
		log.info("\t > added model attrs = {}", modelAndView.getModel().keySet());
	}
	
}
