package me.linkaipemg.autospcompiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import me.linkaipemg.autospannotation.AutoGenerateField;
import me.linkaipemg.autospannotation.AutoSharedPreferences;
import me.linkaipemg.autospcompiler.generator.BooleanGetterSetterGenerator;
import me.linkaipemg.autospcompiler.generator.FloatGetterSetterGenerator;
import me.linkaipemg.autospcompiler.generator.IntGetterSetterGenerator;
import me.linkaipemg.autospcompiler.generator.LongGetterSetterGenerator;
import me.linkaipemg.autospcompiler.generator.StringGetterSetterGenerator;
import me.linkaipemg.autospcompiler.generator.StringSetGetterSetterGenerator;
import me.linkaipemg.autospcompiler.utils.TextUtils;

/**
 * Created by linkaipeng on 2019/4/4.
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"me.linkaipemg.autospannotation.AutoSharedPreferences", "me.linkaipemg.autospannotation.AutoGenerateField"})
public class AutoSharedPreferenceProcessor extends AbstractProcessor {

    private static final String PACKAGE_NAME = "me.linkaipeng.autosp";

    private Messager mMessager;
    private Elements mElements;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mElements = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        generateConfigClass();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(AutoSharedPreferences.class)) {
            AutoSharedPreferences sharedPreferencesAnnotation = element.getAnnotation(AutoSharedPreferences.class);

            String targetClassName = element.getSimpleName().toString();

            String spName = targetClassName + "SP";
            if (!TextUtils.isEmpty(sharedPreferencesAnnotation.name())) {
                spName = sharedPreferencesAnnotation.name();
            }
            TypeName currentClassTypeName = ClassName.get(PACKAGE_NAME, spName);

            FieldSpec instanceField = FieldSpec.builder(currentClassTypeName, "sInstance", Modifier.PRIVATE, Modifier.STATIC)
                    .build();

            TypeName sharedPreferencesTypeName = ClassName.get("android.content", "SharedPreferences");
            FieldSpec sharedPreferencesField = FieldSpec.builder(sharedPreferencesTypeName, "mSharedPreferences")
                    .addModifiers(Modifier.PRIVATE)
                    .build();


            // 私有构造函数
            MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addStatement("mSharedPreferences = AutoSharedPreferenceConfig.getInstance().getContext().getSharedPreferences($S, $L)", spName, sharedPreferencesAnnotation.mode())
                    .build();

            MethodSpec instanceMethod = MethodSpec.methodBuilder("getInstance")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(currentClassTypeName)
                    .addStatement("if (null == sInstance) {\n" +
                                    "sInstance = new $T();\n" +
                                    "}\n" +
                                    "return sInstance",
                            currentClassTypeName)
                    .build();

            MethodSpec getSharedPreferencesMethod = MethodSpec.methodBuilder("getSharedPreferences")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return mSharedPreferences")
                    .returns(sharedPreferencesTypeName)
                    .build();

            MethodSpec containsMethod = MethodSpec.methodBuilder("contains")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return mSharedPreferences.contains(key)")
                    .addParameter(TypeName.get(String.class), "key")
                    .returns(TypeName.BOOLEAN)
                    .build();

            TypeSpec infoClazz = TypeSpec.classBuilder(spName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(instanceField)
                    .addField(sharedPreferencesField)
                    .addMethod(constructorMethod)
                    .addMethod(instanceMethod)
                    .addMethod(getSharedPreferencesMethod)
                    .addMethod(containsMethod)
                    .addMethods(generateGetterSetter(roundEnvironment))
                    .addMethod(generateClear())
                    .build();

            JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, infoClazz).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    private List<MethodSpec> generateGetterSetter(RoundEnvironment roundEnvironment) {
        List<MethodSpec> methodSpecs = new ArrayList();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(AutoGenerateField.class)) {
            // getter setter
            methodSpecs.addAll(new StringGetterSetterGenerator().generateMethodSpec(element));
            methodSpecs.addAll(new IntGetterSetterGenerator().generateMethodSpec(element));
            methodSpecs.addAll(new LongGetterSetterGenerator().generateMethodSpec(element));
            methodSpecs.addAll(new FloatGetterSetterGenerator().generateMethodSpec(element));
            methodSpecs.addAll(new BooleanGetterSetterGenerator().generateMethodSpec(element));
            methodSpecs.addAll(new StringSetGetterSetterGenerator().generateMethodSpec(element));
        }
        return methodSpecs;
    }

    private MethodSpec generateClear() {
        // clear
        return MethodSpec.methodBuilder("clear")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("mSharedPreferences.edit().clear().commit()")
                .build();
    }


    private void generateConfigClass() {
        String className = "AutoSharedPreferenceConfig";
        TypeName classTypeName = ClassName.get(PACKAGE_NAME, className);

        TypeName applicationType = ClassName.get("android.app", "Application");
        FieldSpec applicationContextField = FieldSpec.builder(applicationType, "mContext")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec instanceField = FieldSpec.builder(classTypeName, "sInstance")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();


        MethodSpec instanceMethod = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(classTypeName)
                .addStatement("if (null == sInstance) {\n" +
                                "sInstance = new $T();\n" +
                                "}\n" +
                                "return sInstance",
                        classTypeName)
                .build();

        MethodSpec applicationContextMethod = MethodSpec.methodBuilder("getContext")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return mContext")
                .returns(applicationType)
                .build();

        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(applicationType, "context")
                .addStatement("mContext = context")
                .build();

        TypeSpec configClass = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addField(instanceField)
                .addField(applicationContextField)
                .addMethod(constructorMethod)
                .addMethod(applicationContextMethod)
                .addMethod(instanceMethod)
                .addMethod(initMethod)
                .build();

        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, configClass)
                .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
