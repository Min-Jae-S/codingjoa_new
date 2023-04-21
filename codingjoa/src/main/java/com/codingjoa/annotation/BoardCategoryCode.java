package com.codingjoa.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.codingjoa.validator.BoardCategoryCodeValidator;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@Constraint(validatedBy = BoardCategoryCodeValidator.class)
public @interface BoardCategoryCode {
	
	public abstract String message() default "{javax.validation.constraints.BoardCategoryCode.message}";
    public abstract Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
    
}
