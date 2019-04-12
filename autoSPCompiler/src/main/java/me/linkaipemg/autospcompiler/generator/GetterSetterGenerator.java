package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import me.linkaipemg.autospannotation.AutoGenerateField;

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
        String name = element.getSimpleName().toString();

        try {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        } catch (Exception e) {
        }

        MethodSpec.Builder getterBuilder = getBuilder("get", name).returns(typeName);
        MethodSpec.Builder setterBuilder = getBuilder("set", name)
                .returns(TypeName.VOID)
                .addParameter(typeName, "value");
        AutoGenerateField field = element.getAnnotation(AutoGenerateField.class);

        getterSetterList.add(getterBuilder.addStatement(generateGetterReturnCodeBlock(field, name)).build());
        getterSetterList.add(setterBuilder
                .addStatement("mSharedPreferences.edit().put$L($S, value)$L", getPutType(), name, commitType(field))
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
