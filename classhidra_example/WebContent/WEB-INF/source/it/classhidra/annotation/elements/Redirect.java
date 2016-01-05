package it.classhidra.annotation.elements;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Redirect {
	Entity entity() default @Entity;
	String path() default "";
	String contentType() default "";
	String contentEncoding() default "";
	String contentName() default "";
	String transformationName() default "";
	String auth_id() default "";
	String error() default "";
	String descr() default "";
	String mess_id() default "";
	String united_id() default "";
	String img() default "";
	String navigated() default "true";
	String avoidPermissionCheck() default "false";
	Section[] sections() default {};
	Transformation[] transformations() default {};
}
