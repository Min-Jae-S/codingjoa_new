package com.codingjoa.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error")
@Controller
public class ErrorController {

	@RequestMapping("/errorPage")
	public String errorPage() {
		return "error/error-page";
	}
}
