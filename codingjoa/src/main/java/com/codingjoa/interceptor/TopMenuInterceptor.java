package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;
import com.codingjoa.test.Test;

import lombok.extern.slf4j.Slf4j;

/*
 * 	인터셉터는 서블릿이 제공하는 기능이 아니기 때문에 DispatcherType을 쓸 수 없다.
 * 	excludePathPatterns 를 이용하여 경로를 제거한다.
 */

@Slf4j
public class TopMenuInterceptor implements HandlerInterceptor {

	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private WebApplicationContext webApplicationContext;
	private CategoryService categoryService;
	
	public TopMenuInterceptor(WebApplicationContext webApplicationContext, CategoryService categoryService) {
		this.webApplicationContext = webApplicationContext;
		this.categoryService = categoryService;
	}

//	@Autowired
//	private ConversionService conversionService;

	/*
	 * 매핑된 핸들러가 존재하지 않거나 매핑 정보를 찾을 수 없는 경우 preHandle 메서드는 호출되지 않는다. 
	 * 따라서 preHandle 메서드 내에서 handler 매개변수는 항상 null이 아닌 유효한 핸들러를 가리키게 된다.
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		log.info("\t > dispatcherType = {}", request.getDispatcherType());

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("\t > handler = {}", handlerMethod.getBeanType().getSimpleName());
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
		} else {
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
//		log.info("######################");
//		
//		String param2 = request.getParameter("param2");
//		log.info("\t > request.getParameter(\"param2\") = {}", param2);
//		Test test = new Test();
//		
//		Integer converted_param2 = conversionService.convert(param2, Integer.class);
//		log.info("\t > converted_param2 = {}", converted_param2);
//		
//		test.setParam2(converted_param2);
//		log.info("test = {}", test);
//		
//		log.info("######################");
		
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
		log.info("\t > modelAndView is not null, viewName = {}", viewName);
		
		if (viewName == null) {
			log.info("\t > viewName is null; no top menu");
			return;
		}
		
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			log.info("\t > viewName starts with '{}'; no top menu", FORWARD_URL_PREFIX);
			return;	
		}
		
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) 	{
			log.info("\t > viewName starts with '{}'; no top menu", REDIRECT_URL_PREFIX);
			return;
		}
		
		String[] beanNames = webApplicationContext.getBeanNamesForType(MappingJackson2JsonView.class);
		for (String beanName : beanNames) {
			if (viewName.equals(beanName)) {
				log.info("\t > viewName equals MappingJackson2JsonView's beanName({}); no top menu", beanName);
				return;
			}
		}
		
		List<Category> parentCategoryList = categoryService.findParentCategoryList();
		modelAndView.addObject("parentCategoryList", parentCategoryList);
		log.info("\t > add top menu as model");
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = new StringBuilder(request.getRequestURI().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
