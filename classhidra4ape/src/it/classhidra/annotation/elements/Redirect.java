package it.classhidra.annotation.elements;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Redirect {
	Entity entity() default @Entity;
	String path() default "";
	String auth_id() default "";
	String error() default "";
	String descr() default "";
	String mess_id() default "";
	String united_id() default "";
	String img() default "";
	String navigated() default "true";
	Section[] sections() default {};
	Transformation[] transformations() default {};
}
