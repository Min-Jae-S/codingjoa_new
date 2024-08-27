package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestExController {
	
	/*
	 * @@ Throwable
	 * 	> Error (OutOfMemoryError, StackOverflowError)
	 * 		- An error occurs when there is an abnormal situation in the system, such as OutofMemoryError or StackOverflowError, 
	 * 		  indicating situations that are unrecoverable. 
	 * 		  These errors are often challenging for developers to predict and offer no viable means of handling. 
	 * 	> Exception
	 * 		- Checked Exception(IOException, SQLException)
	 * 		- Unchecked Exception(RuntimeException; NullPointerException, IndexOutOfBoundException, IllegalArgumentException)
	 * 		- An exception occurs when an unforeseen situation arises during the execution of a program due to a developer's mistake.
	 * 		  It can be seen at a glance that these are serious errors in the program, 
	 * 		  indicating a level of severity that is not entirely unrecoverable.
	 * 	> Checked Exception vs Unchecked Exception
	 * 		- Unchecked exceptions inherit from RuntimeException, while checked exceptions do not inherit from RuntimeException. 
	 * 		  This is an important point for distinguishing between the two types of exceptions.
	 * 		- Unchecked exceptions are so named because they do not enforce explicit exception handling. 
	 * 		  This means that you are not compelled to catch them using a catch block 
	 * 		  or declare them using the throws clause in the method signature.
	 * 		- They are termed Checked Exceptions because handling them is mandatory. 
	 * 		  You must either catch them using a try-catch block or propagate them to the calling method using the throws clause.
	 * 
	 */
	
	@GetMapping("/ex/checked")
	public ResponseEntity<Object> checkedEx() { 
		log.info("## checkedEx");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/ex/unchecked")
	public ResponseEntity<Object> uncheckedEx() { 
		log.info("## uncheckedEx");
		return ResponseEntity.ok("success");
	}
	
}
