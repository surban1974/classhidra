package it.classhidra.serialize;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Format {
	String name() default "";
	String format() default "";
	String language() default "";
	String country() default "";
	String timeZoneShift() default "";
	String regex() default "";
	boolean ascii() default false;
	String characterset() default "";
	String replaceOnBlank() default "";
	String replaceOnErrorFormat() default "";
	boolean xml_cdata() default false;
	boolean xml_escape10() default false;
	boolean xml_escape11() default false;
}
