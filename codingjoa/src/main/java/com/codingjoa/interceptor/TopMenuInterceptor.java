package com.codingjoa.interceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 	인터셉터는 서블릿이 제공하는 기능이 아니기 때문에 DispatcherType을 쓸 수 없다. 
 *  excludePathPatterns 를 이용하여 경로를 제거한다.
 */

@Slf4j
@RequiredArgsConstructor
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private final ApplicationContext applicationContext;
	private final CategoryService categoryService;
	private final List<String> excludedPatterns = Arrays.asList("/error/**", "/login");
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
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
		
		// Return the view name to be resolved by the DispatcherServlet via a ViewResolver, or null if we are using a View object.
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
		
		String[] beanNames = applicationContext.getBeanNamesForType(MappingJackson2JsonView.class);
		for (String beanName : beanNames) {
			if (viewName.equals(beanName)) {
				log.info("\t > viewName equals MappingJackson2JsonView's beanName({})", beanName);
				return;
			}
		}
		
		List<Category> parentCategoryList = categoryService.getParentCategoryList();
		modelAndView.addObject("parentCategoryList", parentCategoryList);
		log.info("\t > add parentCategoryList attr");
		
		boolean matchesExcludedPattern = excludedPatterns.stream()
				.anyMatch(pattern -> antPathMatcher.match(request.getContextPath() + pattern, request.getRequestURI()));
		
		if (!matchesExcludedPattern) {
			String loginRedirect = getFullURL(request);
			loginRedirect = URLEncoder.encode(loginRedirect, StandardCharsets.UTF_8);
			modelAndView.addObject("loginRedirect", loginRedirect);
			log.info("\t > add loginRedirect attr");
		}
		
		log.info("\t > added model attrs = {}", modelAndView.getModel().keySet());
	}
	
	private String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		
		if (queryString == null) {
			return requestURL.toString();
		} else {
			//return requestURL.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
			return requestURL.append('?').append(queryString).toString();
		}
	}
	
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		log.info("## {}.postHandle", this.getClass().getSimpleName());
//		
//		if (handler instanceof HandlerMethod) {
//			HandlerMethod handlerMethod = (HandlerMethod) handler;
//			Class<?> controllerClass = handlerMethod.getBeanType();
//			if (controllerClass.isAnnotationPresent(RestController.class)) {
//				// log.info("\t > not find top menu - @RestController");
//				return;
//			}
//
//			MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
//			for (MethodParameter methodParameter : methodParameters) {
//				if (methodParameter.hasMethodAnnotation(ResponseBody.class)) {
//					// log.info("\t > not find top menu - @ResponseBody");
//					return;
//				}
//			}
//		}
//		
//		...
//	
//	}
	
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		log.info("## {}.preHandle", this.getClass().getSimpleName());
//		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
//		log.info("\t > dispatcherType = {}", request.getDispatcherType());
//		if (handler instanceof HandlerMethod) {
//			HandlerMethod handlerMethod = (HandlerMethod) handler;
//			int index = handlerMethod.toString().lastIndexOf(".");
//			log.info("\t > handler = {}", handlerMethod.toString().substring(index + 1));
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
//		} else {
//			log.info("\t > handler = {}", handler.getClass().getSimpleName());
//		}
	
//		return true;
//	}
}
