package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class IntGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(TypeName.INT);
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        int defaultValue = field.defaultIntValue();
        return CodeBlock.of("return mSharedPreferences.getInt($S, $L)", name, defaultValue);
    }

    @Override
    protected CodeBlock generateSetterCodeBlock(String name) {
        return CodeBlock.of("mSharedPreferences.edit().putInt($S, value).commit()", name);
    }
}
