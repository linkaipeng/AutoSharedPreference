package me.linkaipemg.autospannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linkaipeng on 2019/4/4.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoGenerateField {

    String filedName() default "";
    String defaultStringValue() default "";
    int defaultIntValue() default 0;
    long defaultLongValue() default 0l;
    float defaultFloatValue() default 0f;
    boolean defaultBooleanValue() default false;
    CommitType commitType() default CommitType.COMMIT;

    enum CommitType {
        COMMIT, APPLY
    }
}
