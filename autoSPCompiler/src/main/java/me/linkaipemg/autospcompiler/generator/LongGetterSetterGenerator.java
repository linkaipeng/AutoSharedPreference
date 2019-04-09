package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class LongGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(TypeName.LONG);
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        long defaultValue = field.defaultLongValue();
        return CodeBlock.of("return mSharedPreferences.getLong($S, $L)", name, defaultValue);
    }

    @Override
    protected CodeBlock generateSetterCodeBlock(String name) {
        return CodeBlock.of("mSharedPreferences.edit().putLong($S, value).commit()", name);
    }
}
