package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface ActionCall {
	Entity entity() default @Entity;
	String owner() default "";
	String name();
	String path() default "";
	String method() default "";
	String navigated() default "true";
	Redirect Redirect() default @Redirect;
	Expose Expose() default @Expose;
}
