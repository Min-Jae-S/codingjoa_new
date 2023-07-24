package com.codingjoa.test;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("com.codingjoa.test.SampleAnno")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class SampleProcessor extends AbstractProcessor {
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "## ERORR !!!");
		return true;
	}
}
