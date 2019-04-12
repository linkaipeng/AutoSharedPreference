package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import me.linkaipemg.autospannotation.AutoGenerateField;
import me.linkaipemg.autospcompiler.utils.TextUtils;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public abstract class GetterSetterGenerator {

    public List<MethodSpec> generateMethodSpec(Element element) {
        List<MethodSpec> getterSetterList = new ArrayList();
        TypeName typeName = TypeName.get(element.asType());
        if (!correctType(typeName)) {
            return getterSetterList;
        }
        String methodName = element.getSimpleName().toString();

        try {
            methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        } catch (Exception e) {
        }

        MethodSpec.Builder getterBuilder = getBuilder("get", methodName).returns(typeName);
        MethodSpec.Builder setterBuilder = getBuilder("set", methodName)
                .returns(TypeName.VOID)
                .addParameter(typeName, "value");
        AutoGenerateField fieldAnnotation = element.getAnnotation(AutoGenerateField.class);

        String fieldName = methodName;
        if (!TextUtils.isEmpty(fieldAnnotation.filedName())) {
            fieldName = fieldAnnotation.filedName();
        }

        getterSetterList.add(getterBuilder.addStatement(generateGetterReturnCodeBlock(fieldAnnotation, fieldName)).build());
        getterSetterList.add(setterBuilder
                .addStatement("mSharedPreferences.edit().put$L($S, value)$L", getPutType(), fieldName, commitType(fieldAnnotation))
                .build());
        return getterSetterList;
    }

    private MethodSpec.Builder getBuilder(String action, String name) {
        return MethodSpec.methodBuilder(action + name)
                .addModifiers(Modifier.PUBLIC);
    }

    protected String commitType(AutoGenerateField field) {
        if (field.commitType() == AutoGenerateField.CommitType.APPLY) {
            return ".apply()";
        } else {
            return ".commit()";
        }
    }

    protected abstract boolean correctType(TypeName typeName);
    protected abstract CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name);
    protected abstract String getPutType();
}
