package com.richzjc.dcompiler;

import com.google.auto.service.AutoService;
import com.richzjc.dcompiler.util.Const;
import com.richzjc.dcompiler.util.EmptyUtils;
import com.richzjc.downloadannotation.NetChange;
import com.richzjc.downloadannotation.PauseAll;
import com.richzjc.downloadannotation.PauseAndStart;
import com.richzjc.downloadannotation.PauseStartEmpty;
import com.richzjc.downloadannotation.SizeChange;
import com.richzjc.downloadannotation.StartAll;
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
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DownloadProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private String packageName;
    private String className;

    private Map<TypeElement, List<ExecutableElement>> sizeMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> progressMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> requestDataMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> pauseStartMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> pauseStartEmptyMethods = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> netChangeMethods = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        parsePackage(processingEnv);

    }

    private void parsePackage(ProcessingEnvironment processingEnv) {
        packageName = processingEnv.getOptions().get("moduleName");
        messager.printMessage(Diagnostic.Kind.NOTE, "name =  " + packageName);
        if (!EmptyUtils.isEmpty(packageName)) {
                className = "RIndex";
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(SizeChange.class.getName());
        set.add(PauseAll.class.getName());
        set.add(StartAll.class.getName());
        set.add(PauseAndStart.class.getName());
        set.add(PauseStartEmpty.class.getName());
        set.add(NetChange.class.getName());
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
                Set<? extends Element> pauseAndStart = roundEnv.getElementsAnnotatedWith(PauseAndStart.class);
                Set<? extends Element> pauseStartEmpty = roundEnv.getElementsAnnotatedWith(PauseStartEmpty.class);
                Set<? extends Element> netChanges = roundEnv.getElementsAnnotatedWith(NetChange.class);

                messager.printMessage(Diagnostic.Kind.NOTE, "size =  " + sizeChanges.size());

                //TODO 下面的每一个方法需要对里面的参数进行检测, 貌似有漏洞去获取方法的时候 而且要把父类的方法也要取出来 保存下来
                parseSizeChange(sizeChanges);
                parseProgressChange(progressChanges);
                parseRequestDatas(requestDatas);
                parsePauseAndStart(pauseAndStart);
                parsePauseStartEmpty(pauseStartEmpty);
                parseNetChange(netChanges);
                generateFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    private void parseNetChange(Set<? extends Element> requestDatas) {
        if (requestDatas != null && !requestDatas.isEmpty()) {
            for (Element element : requestDatas) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!netChangeMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    netChangeMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = netChangeMethods.get(typeElement);
                methods.add((ExecutableElement) element);
            }
        }
    }

    private void parsePauseStartEmpty(Set<? extends Element> requestDatas) {
        if (requestDatas != null && !requestDatas.isEmpty()) {
            for (Element element : requestDatas) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!pauseStartEmptyMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    pauseStartEmptyMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = pauseStartEmptyMethods.get(typeElement);
                methods.add((ExecutableElement) element);
            }
        }
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

    private void parsePauseAndStart(Set<? extends Element> requestDatas) {
        if (requestDatas != null && !requestDatas.isEmpty()) {
            for (Element element : requestDatas) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (!pauseStartMethods.containsKey(typeElement)) {
                    List<ExecutableElement> method = new ArrayList<>();
                    pauseStartMethods.put(typeElement, method);
                }
                List<ExecutableElement> methods = pauseStartMethods.get(typeElement);
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
        builder.addStatement("$T<$T> pauseStartList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> pauseStartEmptyList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));
        builder.addStatement("$T<$T> netChangeList", ClassName.get(List.class), ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)));

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : sizeMethods.entrySet()) {
            List sizeMethods = entry.getValue();
            List progressMethod = progressMethods.get(entry.getKey());
            List reqeustMethod = requestDataMethods.get(entry.getKey());
            List pauseStartMethod = pauseStartMethods.get(entry.getKey());
            List pauseStartEmptyMethod = pauseStartEmptyMethods.get(entry.getKey());
            List netChangeMethod = netChangeMethods.get(entry.getKey());

            progressMethods.remove(entry.getKey());
            requestDataMethods.remove(entry.getKey());
            pauseStartMethods.remove(entry.getKey());
            pauseStartEmptyMethods.remove(entry.getKey());
            netChangeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : progressMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = entry.getValue();
            List reqeustMethod = requestDataMethods.get(entry.getKey());
            List pauseStartMethod = pauseStartMethods.get(entry.getKey());
            List pauseStartEmptyMethod = pauseStartEmptyMethods.get(entry.getKey());
            List netChangeMethod = netChangeMethods.get(entry.getKey());
            progressMethods.remove(entry.getKey());
            requestDataMethods.remove(entry.getKey());
            pauseStartMethods.remove(entry.getKey());
            pauseStartEmptyMethods.remove(entry.getKey());
            netChangeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }


        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : requestDataMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = entry.getValue();
            List pauseStartMethod = pauseStartMethods.get(entry.getKey());
            List pauseStartEmptyMethod = pauseStartEmptyMethods.get(entry.getKey());
            List netChangeMethod = netChangeMethods.get(entry.getKey());
            pauseStartMethods.remove(entry.getKey());
            pauseStartEmptyMethods.remove(entry.getKey());
            netChangeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : pauseStartMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = null;
            List pauseStartMethod = entry.getValue();
            List pauseStartEmptyMethod = pauseStartEmptyMethods.get(entry.getKey());
            List netChangeMethod = netChangeMethods.get(entry.getKey());
            pauseStartEmptyMethods.remove(entry.getKey());
            netChangeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : pauseStartEmptyMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = null;
            List pauseStartMethod = null;
            List pauseStartEmptyMethod = entry.getValue();
            List netChangeMethod = netChangeMethods.get(entry.getKey());
            netChangeMethods.remove(entry.getKey());
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }

        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : netChangeMethods.entrySet()) {
            List sizeMethods = null;
            List progressMethod = null;
            List reqeustMethod = null;
            List pauseStartMethod = null;
            List pauseStartEmptyMethod = null;
            List netChangeMethod = entry.getValue();
            addToBuilder(sizeMethods, progressMethod, reqeustMethod, pauseStartMethod, pauseStartEmptyMethod, netChangeMethod, builder, entry.getKey());
        }
    }

    private void addToBuilder(List<ExecutableElement> sizeMethods,
                              List<ExecutableElement> progressMethod,
                              List<ExecutableElement> reqeustMethod,
                              List<ExecutableElement> pauseStartMethod,
                              List<ExecutableElement> pauseStartEmptyMethod,
                              List<ExecutableElement> netChangeMethod,
                              CodeBlock.Builder builder, TypeElement key) {
        builder.addStatement("sizeList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("progressList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("requestList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("pauseStartList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("pauseStartEmptyList = new $T()", ClassName.get(ArrayList.class));
        builder.addStatement("netChangeList = new $T()", ClassName.get(ArrayList.class));

        if (sizeMethods != null) {
            for (ExecutableElement element : sizeMethods) {
                builder.addStatement("sizeList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (progressMethod != null) {
            for (ExecutableElement element : progressMethod) {
                builder.addStatement("progressList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (reqeustMethod != null) {
            for (ExecutableElement element : reqeustMethod) {
                builder.addStatement("requestList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (pauseStartMethod != null) {
            for (ExecutableElement element : pauseStartMethod) {
                builder.addStatement("pauseStartList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }

        if (pauseStartEmptyMethod != null) {
            for (ExecutableElement element : pauseStartEmptyMethod) {
                builder.addStatement("pauseStartEmptyList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }


        if (netChangeMethod != null) {
            for (ExecutableElement element : netChangeMethod) {
                builder.addStatement("netChangeList.add(new $T($S, null))", ClassName.get(elementUtils.getTypeElement(Const.SUBSCRIBE_METHOD_PATH)), element.getSimpleName().toString());
            }
        }


        builder.addStatement("SUBSCRIBER_INDEX.put($T.class, new $T(sizeList, progressList, requestList, pauseStartList, pauseStartEmptyList, netChangeList))", ClassName.get(key), ClassName.get(elementUtils.getTypeElement(Const.SIMPLE_SUBSCRIBE_INFO)));

    }
}
