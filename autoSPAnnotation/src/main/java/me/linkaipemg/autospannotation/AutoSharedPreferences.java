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

}
