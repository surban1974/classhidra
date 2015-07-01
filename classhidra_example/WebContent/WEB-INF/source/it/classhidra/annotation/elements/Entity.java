package it.classhidra.annotation.elements;import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	String property() default "";
	String provider() default "";
	String order() default "";
	String comment() default "";
	String system() default "false";
	String annotated() default "true";
	String lookup() default "";
}
