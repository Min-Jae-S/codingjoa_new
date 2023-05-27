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
		log.info("-------- CategoryInterceptor --------");
		log.info("## preHandle");
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		
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
//			ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
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
			String viewName = modelAndView.getViewName(); 
			log.info("\t > viewName = {}", viewName);
			
			if (viewName == null) {
				log.info("\t > viewName is null ==> not add 'top menu'");
				return;
			}

			if (viewName.startsWith(FORWARD_URL_PREFIX)) {
				log.info("\t > viewName starts with '{}' ==> not add 'top menu'", FORWARD_URL_PREFIX);
				return;
			}
			
			if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
				log.info("\t > viewName starts with '{}' ==> not add 'top menu'", REDIRECT_URL_PREFIX);
				return;
			}
			
			String[] beanNames = webApplicationContext.getBeanNamesForType(MappingJackson2JsonView.class);
			for (String beanName : beanNames) {
				log.info("\t > registerd MappingJackson2JsonView beanName = {}", beanName);
				if (viewName.equals(beanName)) {
					log.info("\t > viewName equals '{}' ==> not add 'top menu'", beanName);
					return;	
				}
			}
			
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
