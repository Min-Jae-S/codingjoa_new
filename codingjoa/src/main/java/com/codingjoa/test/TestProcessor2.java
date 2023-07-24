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
import com.codingjoa.annotation.CheckMessageResolved;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SupportedAnnotationTypes({
	"com.codingjoa.annotation.CodeCallRequired",
    "com.codingjoa.annotation.CheckMessageResolved"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TestProcessor2 extends AbstractProcessor {
	
	private final String PACKAGE_NAME = "com.codingjoa.test";
	private final String CLASS_NAME = "TestResponseBuilder";
	private boolean isCodeMethodCalled;
    private boolean isMessageResolved;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		log.info("## {} : init", this.getClass().getSimpleName());
		super.init(processingEnv);
		isCodeMethodCalled = false;
		isMessageResolved = false;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		log.info("## {} : process", this.getClass().getSimpleName());
		
		// @CodeCallRequired
		for (Element element : roundEnv.getElementsAnnotatedWith(CodeCallRequired.class)) {
            if (element.getKind() == ElementKind.METHOD && element instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) element;
                Element enclosingClassElement = methodElement.getEnclosingElement();
                if (enclosingClassElement instanceof TypeElement && isTestResponseBuilder((TypeElement) enclosingClassElement)) {
                	if (!isCodeMethodCalled) {
                		processingEnv.getMessager().printMessage(
                        		Diagnostic.Kind.ERROR, "## 제약조건에 위배된 호출 : code메서드 호출이 선행되어야 합니다.", 
                        		methodElement);
                	}
                }
            }
        }

		// @CheckMessageResolved
		for (Element element : roundEnv.getElementsAnnotatedWith(CheckMessageResolved.class)) {
            if (element.getKind() == ElementKind.METHOD && element instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) element;
                Element enclosingClassElement = methodElement.getEnclosingElement();
                if (enclosingClassElement instanceof TypeElement && isTestResponseBuilder((TypeElement) enclosingClassElement)) {
                    if (isMessageResolved) {
                        processingEnv.getMessager().printMessage(
                        		Diagnostic.Kind.ERROR, "## 제약조건에 위배된 호출 : code에 의해 message가 이미 등록되었습니다.", 
                        		methodElement);
                    }
                }
            }
        }
		
		return false;
	}
	
	private boolean isTestResponseBuilder(TypeElement typeElement) {
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        log.info("\t > packageName = {}", packageName);
        log.info("\t > className = {}", className);
        
        return PACKAGE_NAME.equals(packageName) && CLASS_NAME.equals(className);
    }

}
