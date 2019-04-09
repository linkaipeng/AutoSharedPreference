package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class BooleanGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(TypeName.BOOLEAN);
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        boolean defaultValue = field.defaultBooleanValue();
        return CodeBlock.of("return mSharedPreferences.getBoolean($S, $L)", name, defaultValue);
    }

    @Override
    protected CodeBlock generateSetterCodeBlock(String name) {
        return CodeBlock.of("mSharedPreferences.edit().putBoolean($S, value).commit()", name);
    }
}
