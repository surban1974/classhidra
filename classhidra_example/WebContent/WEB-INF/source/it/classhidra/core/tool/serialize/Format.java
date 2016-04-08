package it.classhidra.core.tool.serialize;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Format {
	String format() default "";
	String language() default "";
	String country() default "";
	String regex() default "";
	boolean ascii() default false;
	String characterset() default "";

}
