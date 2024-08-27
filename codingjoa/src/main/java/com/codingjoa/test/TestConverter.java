package com.codingjoa.test;

import java.util.Set;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import com.codingjoa.annotation.AnnoTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConverter implements ConditionalGenericConverter {

	private final int DEFAULT_VALUE = 100;
	
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) { 
		// the source object to convert, which must be an instance of S (never null)
		log.info("## {}#convert ", this.getClass().getSimpleName());
		log.info("\t > source = {}", source);

		try {
			return Integer.parseInt((String) source);
		} catch (NumberFormatException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
		
		return DEFAULT_VALUE;
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		log.info("## {}#matches ", this.getClass().getSimpleName());
		
		Object source = targetType.getSource();
		log.info("\t > {}", source.getClass().getName());
		
		if (source instanceof MethodParameter) {
			MethodParameter methodParameter = (MethodParameter) source;
			log.info("\t > methodParameter = {}", methodParameter);
		}
		
		log.info("\t > sourceType.hasAnnotation(AnnoTest.class) = {}", targetType.hasAnnotation(AnnoTest.class));
		if (targetType.hasAnnotation(AnnoTest.class)) {
			return true;
		}
		
		return true;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() { // String --> int 
		log.info("## {}#getConvertibleTypes ", this.getClass().getSimpleName());
		ConvertiblePair convertiblePair = new ConvertiblePair(String.class, int.class);
		log.info("\t > {}", convertiblePair);
		
		//return Collections.singleton(convertiblePair);
		return null;
	}
}
