package com.richzjc.dcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.dcompiler.util.Const;
import com.richzjc.dcompiler.util.EmptyUtils;
import com.richzjc.downloadannotation.ProgressChange;
import com.richzjc.downloadannotation.RequestDataSucc;
import com.richzjc.downloadannotation.SizeChange;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DownloadProcessor extends AbstractProcessor {

//    class MyEventIndex{
//        private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX;
//        static{
//              su = new HashMap(Class)
//     putIndex()
//        }
//
//    putIndex
    // getsubscribeInfo
//    }


    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Types typeUtils;
    private String packageName;
    private String className;

    private Map<TypeElement, List<ExecutableElement>> sizeMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> progressMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> requestDataMethods = new HashMap<>();

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

    private void parseRequestDatas(Set<? extends Element> requestDatas) {
        if (requestDatas != null && !requestDatas.isEmpty()) {
            for (Element element : requestDatas) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!requestDataMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    requestDataMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = requestDataMethods.get(typeElement);
                methods.add((ExecutableElement) element);
            }
        }
    }

    private void parseProgressChange(Set<? extends Element> progressChanges) {
        if (progressChanges != null && !progressChanges.isEmpty()) {
            for (Element element : progressChanges) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!progressMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    progressMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = progressMethods.get(typeElement);
                methods.add((ExecutableElement) element);
            }
        }
    }

    private void parseSizeChange(Set<? extends Element> sizeChanges) {
        if (sizeChanges != null && !sizeChanges.isEmpty()) {
            for (Element element : sizeChanges) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!sizeMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    sizeMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = sizeMethods.get(typeElement);
                methods.add((ExecutableElement) element);
            }
        }
    }

    private void generateFile() throws IOException {
        if (EmptyUtils.isEmpty(packageName) || EmptyUtils.isEmpty(className)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "在gradle里面传的值是不对的，导致包名和类名没有获取得到");
            return;
        }

        ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(elementUtils.getTypeElement(Const.CLASS_PATH)), "cls")
                .build();

        MethodSpec methodSpec = MethodSpec.methodBuilder("getSubscriberInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(elementUtils.getTypeElement(Const.OVERRIDE_ANNOTATION)))
                .addParameter(parameterSpec)
                .returns(ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)))
                .addStatement("return SUBSCRIBER_INDEX.get(cls)")
                .build();

        TypeName typeName = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));

        FieldSpec fieldSpec = FieldSpec.builder(typeName, "SUBSCRIBER_INDEX", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .build();

        CodeBlock codeBlock = CodeBlock.builder()
                .addStatement("SUBSCRIBER_INDEX = new $T<$T, $T>()", ClassName.get(HashMap.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)))
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_INFO_INDEX)))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addField(fieldSpec)
                .addStaticBlock(codeBlock)
                .build();


        JavaFile.builder(packageName, typeSpec)
                .build()
                .writeTo(filer);
    }
}
