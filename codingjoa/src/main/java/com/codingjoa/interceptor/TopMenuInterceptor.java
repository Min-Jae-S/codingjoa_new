package com.codingjoa.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private static final String JSON_VIEW = "jsonView";

	//private final List<String> excludePatterns = Arrays.asList("/error/**", "/login");
	private final AntPathRequestMatcher loginMatcher = new AntPathRequestMatcher("/login");
	private final AntPathRequestMatcher errorMatcher = new AntPathRequestMatcher("/error");
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
		
//		boolean matchesExcludePattern = excludePatterns.stream()
//				.anyMatch(pattern -> new AntPathRequestMatcher(pattern).matches(request));
//		
//		if (!matchesExcludePattern) {
//			log.info("\t > no matches excludePatterns, setting continueUrl as model attribute");
//			
//			String continueUrl = UriUtils.buildContinueUrl(request);
//			log.info("\t > continueUrl = {}", continueUrl);
//			
//			modelAndView.addObject("continueUrl", continueUrl);
//		} else {
//			log.info("\t > matches excludePatterns, no continueUrl set");
//		}
		
		String continueUrl = "";
		if (loginMatcher.matches(request)) {
			log.info("\t > matching loginPattern");
		} else if (errorMatcher.matches(request)) {
			log.info("\t > matching errorPattern");
		} else {
			log.info("\t > not matching loginPattern or errorPattern");
		}
		
		modelAndView.addObject("continueUrl", continueUrl);
		
		log.info("\t > added model attrs = {}", modelAndView.getModel().keySet());
	}
	
}
