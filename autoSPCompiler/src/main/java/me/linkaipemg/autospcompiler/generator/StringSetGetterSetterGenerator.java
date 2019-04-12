package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class StringSetGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(ParameterizedTypeName.get(Set.class, String.class));
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        return CodeBlock.of("return mSharedPreferences.getStringSet($S, new $T())", name, HashSet.class);
    }

    @Override
    protected String getPutType() {
        return "StringSet";
    }
}
