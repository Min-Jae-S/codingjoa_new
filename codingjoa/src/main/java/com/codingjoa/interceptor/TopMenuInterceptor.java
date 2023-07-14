package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 	인터셉터는 서블릿이 제공하는 기능이 아니기 때문에 DispatcherType을 쓸 수 없다.
 * 	excludePathPatterns 를 이용하여 경로를 제거한다.
 */

@Slf4j
@AllArgsConstructor
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private ApplicationContext applicationContext;
	private CategoryService categoryService;
	
	/*
	 * 매핑된 핸들러가 존재하지 않거나 매핑 정보를 찾을 수 없는 경우 preHandle 메서드는 호출되지 않는다. 
	 * 따라서 preHandle 메서드 내에서 handler 매개변수는 항상 null이 아닌 유효한 핸들러를 가리키게 된다.
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
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
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## {} : postHandle", this.getClass().getSimpleName());
		
		if (modelAndView == null) {
			log.info("\t > modelAndView is null");
			return;
		}
		
		// Return the view name to be resolved by the DispatcherServlet via a ViewResolver, 
		// or null if we are using a View object.
		String viewName = modelAndView.getViewName(); 
		log.info("\t > viewName = {}", viewName + ".jsp");
		
		if (viewName == null) {
			log.info("\t > viewName is null");
			return;
		}
		
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			log.info("\t > viewName starts with '{}'", FORWARD_URL_PREFIX);
			return;	
		}
		
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) 	{
			log.info("\t > viewName starts with '{}'", REDIRECT_URL_PREFIX);
			return;
		}
		
		String[] beanNames = applicationContext.getBeanNamesForType(MappingJackson2JsonView.class);
		for (String beanName : beanNames) {
			if (viewName.equals(beanName)) {
				log.info("\t > viewName equals MappingJackson2JsonView's beanName({})", beanName);
				return;
			}
		}
		
		List<Category> parentCategoryList = categoryService.findParentCategoryList();
		modelAndView.addObject("parentCategoryList", parentCategoryList);
		log.info("\t > add top menu as model");
	}
	
	@SuppressWarnings("unused")
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = 
				new StringBuilder(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
