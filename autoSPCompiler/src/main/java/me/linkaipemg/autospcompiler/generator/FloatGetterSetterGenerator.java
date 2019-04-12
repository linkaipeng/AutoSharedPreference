package me.linkaipemg.autospcompiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import me.linkaipemg.autospannotation.AutoGenerateField;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class FloatGetterSetterGenerator extends GetterSetterGenerator {

    @Override
    protected boolean correctType(TypeName typeName) {
        return typeName.equals(TypeName.FLOAT);
    }

    @Override
    protected CodeBlock generateGetterReturnCodeBlock(AutoGenerateField field, String name) {
        float defaultValue = field.defaultFloatValue();
        return CodeBlock.of("return mSharedPreferences.getFloat($S, $Lf)", name, defaultValue);
    }

    @Override
    protected String getPutType() {
        return "Float";
    }
}
