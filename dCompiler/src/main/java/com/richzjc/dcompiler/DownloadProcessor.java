package com.richzjc.dcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.dcompiler.util.EmptyUtils;
import com.richzjc.downloadannotation.ProgressChange;
import com.richzjc.downloadannotation.RequestDataSucc;
import com.richzjc.downloadannotation.SizeChange;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class DownloadProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Types typeUtils;
    private String packageName;
    private String className;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
        parsePackage(processingEnv);
    }

    private void parsePackage(ProcessingEnvironment processingEnv){
        String name = processingEnv.getOptions().get("eventBusIndex");
        if(!EmptyUtils.isEmpty(name)){
            int index = name.lastIndexOf(".");
            if(index > 0){
                packageName = name.substring(0, index);
                className = name.substring(index + 1, name.length());
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(SizeChange.class.getName());
        set.add(ProgressChange.class.getName());
        set.add(RequestDataSucc.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!EmptyUtils.isEmpty(annotations)){
            return true;
        }
        return false;
    }
}
