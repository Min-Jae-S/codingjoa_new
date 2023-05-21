package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
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
	
	private static final String REDIRECT_URL_PREFIX = "redirect:";
    private static final String FORWARD_URL_PREFIX = "forward:";
	private CategoryService categoryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("-------- CategoryInterceptor --------");
		log.info("## preHandle");
		log.info("\t > URI = {}", getFullURI(request));
		
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Class<?> beanType = handlerMethod.getBeanType();
			log.info("\t > handler = {}", beanType);
			
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
		} else {
			Optional<Object> optionalObj = Optional.ofNullable(handler);
			log.info("\t > handler = {}", optionalObj.get().getClass().getSimpleName());
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## postHandle");

		String viewName = null;
		if (modelAndView != null) {
			viewName = modelAndView.getViewName();
		} 
		
		log.info("\t > view name = {}", viewName);
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = new StringBuilder(request.getRequestURI().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
