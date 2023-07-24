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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SupportedAnnotationTypes({
	"com.codingjoa.annotation.CodeCallRequired",
    "com.codingjoa.annotation.CheckMessageResolved"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TestProcessor extends AbstractProcessor {
	
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
		 TypeElement enclosingClassElement = (TypeElement) methodElement.getEnclosingElement();
		 
		 if (annotationType.getQualifiedName().toString().equals("com.codingjoa.annotation.CodeCallRequired")) {
	            if (!isCodeMethodCalled(enclosingClassElement)) {
	                processingEnv.getMessager().printMessage(
	                        javax.tools.Diagnostic.Kind.ERROR,
	                        "## 제약조건에 위배된 호출 : code 메서드 호출이 선행되어야 합니다.",
	                        methodElement
	                );
	            }
	        } else if (annotationType.getQualifiedName().toString().equals("com.codingjoa.annotation.CheckMessageByCode")) {
	            if (isMessageResolved(enclosingClassElement)) {
	                processingEnv.getMessager().printMessage(
	                        javax.tools.Diagnostic.Kind.ERROR,
	                        "## 제약조건에 위배된 호출 : code에 의해 message가 이미 등록되었습니다.",
	                        methodElement
	                );
	            }
	        }
	}
	
	private boolean isCodeMethodCalled(TypeElement enclosingClassElement) {
		// Code 메서드 호출 여부를 체크하는 로직
		// Code 메서드 호출 여부는 enclosingClassElement 내의 상태에 따라 판단
		return true;
	}

	private boolean isMessageResolved(TypeElement enclosingClassElement) {
		// messageByCode 필드의 상태를 체크하는 로직
		// messageByCode 필드의 상태는 enclosingClassElement 내의 상태에 따라 판단
		return true;
	}

}
