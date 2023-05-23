package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {
	
	private static final String FORWARD_URL_PREFIX = "forward:";
	private static final String REDIRECT_URL_PREFIX = "redirect:";
	private static final String JSON_VIEW = "jsonView";
	private CategoryService categoryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("-------- CategoryInterceptor --------");
		log.info("## preHandle");
		log.info("\t > URI = {}", getFullURI(request));
		
		if (handler == null) {
			log.info("\t > handler is null");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("\t > handler is not null");
			log.info("\t > handler = {} [ {} ]", 
					handlerMethod.getClass().getSimpleName(), handlerMethod.getBeanType().getSimpleName());
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
		} else {
			//ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
			log.info("\t > handler is not null");
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## postHandle");

		if (modelAndView != null) {
			log.info("\t > modelAndView is not null");
			
			// Return the view name to be resolved by the DispatcherServletvia a ViewResolver, 
			// or null if we are using a View object.
			String view = modelAndView.getViewName(); 
			log.info("\t > view = {}", view);
			
			if (view == null) return;
			
			if (view.startsWith(FORWARD_URL_PREFIX)) return;
			
			if (view.startsWith(REDIRECT_URL_PREFIX)) return;
			
			if (view.equals(JSON_VIEW)) return;			
			
			List<Category> parentCategoryList = categoryService.findParentCategoryList();
			modelAndView.addObject("parentCategoryList", parentCategoryList);
			log.info("\t > add 'top menu' as model attribute");
		} else {
			log.info("\t > modelAndView is null");
		}
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
