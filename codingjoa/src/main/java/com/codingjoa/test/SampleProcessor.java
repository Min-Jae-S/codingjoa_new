package com.codingjoa.test;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class SampleProcessor extends AbstractProcessor {
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		// Set of full qullified annotation type names
		return Set.of(SampleAnno.class.getName());
	}
	
	@Override
    public SourceVersion getSupportedSourceVersion(){
         return SourceVersion.latestSupported();
    }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "## ERORR");
		return true;
	}
}
