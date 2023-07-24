package com.codingjoa.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS) // RetentionPolicy.SOURCE
@Target(ElementType.METHOD)
public @interface SampleAnno {

}
