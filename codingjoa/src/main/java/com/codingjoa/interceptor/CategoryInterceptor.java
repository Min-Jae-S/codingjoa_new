package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
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
		
		if (handler == null) {
			log.info("\t > handler is null");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Class<?> beanType = handlerMethod.getBeanType();
			log.info("\t > handler = {}", beanType.getSimpleName());
			
//			if (beanType.isAnnotationPresent(Controller.class)) {
//				List<Category> parentCategoryList = categoryService.findParentCategoryList();
//				request.setAttribute("parentCategoryList", parentCategoryList);
//			}
		} else {
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("## postHandle");

		if (modelAndView != null) {
			List<Category> parentCategoryList = categoryService.findParentCategoryList();
			ModelMap model = modelAndView.getModelMap();
			model.addAttribute("parentCategoryList", parentCategoryList);
			modelAndView.addObject("REDIRECT_URL_PREFIX", REDIRECT_URL_PREFIX);

			log.info("\t > view name = {}", modelAndView.getViewName());
			model.forEach((key, value) -> 
				log.info("\t > model attribute = {} / {}", key, value.getClass().getSimpleName()));
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
	    	return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
