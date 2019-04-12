package me.linkaipemg.autospannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linkaipeng on 2019/4/4.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AutoSharedPreferences {

    /**
     * Use android.content.Context MODE_XXX
     * @return
     */
    int mode() default 0x0000;

    /**
     * The file name configuration. Returns ClassName+"SP" if set empty or null.
     * @return
     */
    String name() default "";
}
