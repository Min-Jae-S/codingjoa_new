package com.codingjoa.test;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.codingjoa.annotation.CodeCallRequired;
import com.codingjoa.annotation.CheckMessageByCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SupportedAnnotationTypes({
	"com.codingjoa.annotation.CodeCallRequired",
    "com.codingjoa.annotation.MessageAlreadySet"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TestProcessor extends AbstractProcessor {
	
	private final String PACKAGE_NAME = "com.codingjoa.test";
	private final String CLASS_NAME = "TestResponseBuilder";

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		log.info("## {} : init", this.getClass().getSimpleName());
		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		log.info("## {} : process", this.getClass().getSimpleName());
		
		for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                if (element.getKind() == ElementKind.METHOD) {
                    checkMethodAnnotation((ExecutableElement) element, annotation);
                }
            }
        }
		
		return true;
	}
	
	private void checkMethodAnnotation(ExecutableElement methodElement, TypeElement annotationType) {
		
	}
	
	private boolean isCodeCalled() {
		// Code 메서드 호출 여부를 체크하는 로직
		// Code 메서드 호출 여부는 enclosingClassElement 내의 상태에 따라 판단
		return true;
	}

	private boolean isMessageByCodeSet() {
		// messageByCode 필드의 상태를 체크하는 로직
		// messageByCode 필드의 상태는 enclosingClassElement 내의 상태에 따라 판단
		return true;
	}

}
