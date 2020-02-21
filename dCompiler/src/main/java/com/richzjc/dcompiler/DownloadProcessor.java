package com.richzjc.dcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.dcompiler.util.EmptyUtils;
import com.richzjc.downloadannotation.ProgressChange;
import com.richzjc.downloadannotation.RequestDataSucc;
import com.richzjc.downloadannotation.SizeChange;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DownloadProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Types typeUtils;
    private String packageName;
    private String className;

    private Map<TypeElement, List<Method>> sizeMethods = new HashMap<>();
    private Map<TypeElement, List<Method>> progressMethods = new HashMap<>();
    private Map<TypeElement, List<Method>> requestDataMethods = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
        parsePackage(processingEnv);

    }

    private void parsePackage(ProcessingEnvironment processingEnv) {
        String name = processingEnv.getOptions().get("eventBusIndex");
        messager.printMessage(Diagnostic.Kind.NOTE, "在gradle里面传的值是： " + name);
        if (!EmptyUtils.isEmpty(name)) {
            int index = name.lastIndexOf(".");
            if (index > 0) {
                packageName = name.substring(0, index);
                className = name.substring(index + 1);
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
        if (!EmptyUtils.isEmpty(annotations)) {
            try {
                Set<? extends Element> sizeChanges = roundEnv.getElementsAnnotatedWith(SizeChange.class);
                Set<? extends Element> progressChanges = roundEnv.getElementsAnnotatedWith(ProgressChange.class);
                Set<? extends Element> requestDatas = roundEnv.getElementsAnnotatedWith(RequestDataSucc.class);

                parseSizeChange(sizeChanges);
                parseProgressChange(progressChanges);
                parseRequestDatas(requestDatas);
                generateFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    private void generateFile() {

    }

    private void parseRequestDatas(Set<? extends Element> requestDatas) {


    }

    private void parseProgressChange(Set<? extends Element> progressChanges) {


    }

    private void parseSizeChange(Set<? extends Element> sizeChanges) {
        if (sizeChanges != null && !sizeChanges.isEmpty()) {
            for (Element element : sizeChanges) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!sizeMethods.containsKey(typeElement)) {
                    List<Method> method = new ArrayList<>();
                    sizeMethods.put(typeElement, method);
                }
                List<Method> methods = sizeMethods.get(typeElement);
                Method
            }
        }
    }
}
