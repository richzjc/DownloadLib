package com.richzjc.dcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.dcompiler.util.Const;
import com.richzjc.dcompiler.util.EmptyUtils;
import com.richzjc.downloadannotation.PauseAll;
import com.richzjc.downloadannotation.StartAll;
import com.richzjc.downloadannotation.SizeChange;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
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
        messager.printMessage(Diagnostic.Kind.NOTE, "name =  " + name);
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
        set.add(PauseAll.class.getName());
        set.add(StartAll.class.getName());
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
                Set<? extends Element> progressChanges = roundEnv.getElementsAnnotatedWith(PauseAll.class);
                Set<? extends Element> requestDatas = roundEnv.getElementsAnnotatedWith(StartAll.class);

                messager.printMessage(Diagnostic.Kind.NOTE, "size =  " + sizeChanges.size());

                //TODO 下面的每一个方法需要对里面的参数进行检测, 貌似有漏洞去获取方法的时候 而且要把父类的方法也要取出来 保存下来
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

        TypeName typeName = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));


        MethodSpec methodSpec = MethodSpec.methodBuilder("getSubscriberInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(elementUtils.getTypeElement(Const.OVERRIDE_ANNOTATION)))
                .returns(typeName)
                .addStatement("return SUBSCRIBER_INDEX")
                .build();


        FieldSpec fieldSpec = FieldSpec.builder(typeName, "SUBSCRIBER_INDEX", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .build();

        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("SUBSCRIBER_INDEX = new $T<$T, $T>()", ClassName.get(HashMap.class), ClassName.get(Class.class), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));

        addStatementToBuilder(builder);

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_INFO_INDEX)))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addField(fieldSpec)
                .addStaticBlock(builder.build())
                .build();


        JavaFile.builder(packageName, typeSpec)
                .build()
                .writeTo(filer);
    }

    private void addStatementToBuilder(CodeBlock.Builder builder) {
        builder.addStatement("$T<$T> sizeList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> progressList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> requestList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : sizeMethods.entrySet()) {
            List sizeMethods = entry.getValue();
            List progressMethod = progressMethods.get(entry.getKey());
            List reqeustMethod = requestDataMethods.get(entry.getKey());
            progressMethods.remove(entry.getKey());
            requestDataMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : progressMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = entry.getValue();
            List reqeustMethod = requestDataMethods.get(entry.getKey());
            requestDataMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }


        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : requestDataMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = entry.getValue();
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, builder, entry.getKey());
        }
    }

    private void addToBuilder(List<ExecutableElement> sizeMethods, List<ExecutableElement> progressMethod, List<ExecutableElement> reqeustMethod, CodeBlock.Builder builder, TypeElement key) {
        builder.addStatement("sizeList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("progressList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("requestList = new $T()", ClassName.get(ArrayList.class));

        if (sizeMethods != null) {
            for (ExecutableElement element : sizeMethods) {
                builder.addStatement("sizeList.add(new $T($S, $T.class))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(Integer.class));
            }
        }

        if (progressMethod != null) {
            for (ExecutableElement element : progressMethod) {
                builder.addStatement("progressList.add(new $T($S, $T.class))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(Integer.class));
            }
        }

        if (reqeustMethod != null) {
            for (ExecutableElement element : reqeustMethod) {
                builder.addStatement("requestList.add(new $T($S, $T.class))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString(), ClassName.get(Integer.class));
            }
        }

        builder.addStatement("SUBSCRIBER_INDEX.put($T.class, new $T(sizeList, progressList, requestList))", ClassName.get(key), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));

    }
}
