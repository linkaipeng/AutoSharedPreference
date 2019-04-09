package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class StringGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(TypeName.get(String.class));
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        String defaultValue = field.defaultStringValue();
        return CodeBlock.of("return mSharedPreferences.getString($S, $S)", name, defaultValue);
    }

    @Override
    protected CodeBlock generateSetterCodeBlock(String name) {
        return CodeBlock.of("mSharedPreferences.edit().putString($S, value).commit()", name);
    }
}
