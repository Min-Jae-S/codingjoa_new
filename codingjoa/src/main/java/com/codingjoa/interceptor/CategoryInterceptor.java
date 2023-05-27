package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {
	
	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private WebApplicationContext webApplicationContext;
	private CategoryService categoryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		
		if (handler == null) {
			log.info("\t > handler is null");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("\t > handler = {} [ {} ]", 
					handlerMethod.getClass().getSimpleName(), handlerMethod.getBeanType().getSimpleName());
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
		} else {
//			ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## {} : postHandle", this.getClass().getSimpleName());
		
		if (modelAndView == null) return;

		// Return the view name to be resolved by the DispatcherServlet via a ViewResolver, 
		// or null if we are using a View object.
		String viewName = modelAndView.getViewName(); 
		log.info("\t > viewName = {}", viewName);
		
		if (viewName == null) return;
		
		if (viewName.startsWith(FORWARD_URL_PREFIX)) return;	
		
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) 	return;
		
		String[] beanNames = webApplicationContext.getBeanNamesForType(MappingJackson2JsonView.class);
		for (String beanName : beanNames) {
			if (viewName.equals(beanName)) return;
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
