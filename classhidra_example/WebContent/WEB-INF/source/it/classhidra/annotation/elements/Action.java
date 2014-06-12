package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	Entity entity() default @Entity;
	String path() default "";
	String type() default "";
	String name() default "";
	String redirect() default "";
	String error() default "";
	String memoryInSession() default "";
	String reloadAfterAction() default "";
	String reloadAfterNextNavigated() default "";	
	String navigated() default "";
	String syncro() default "false";
	String statistic() default "true";
	String help() default "";
	String listener() default "";
	Redirect[] redirects() default {};
	Transformation[] transformations() default {};
	ActionCall[] calls() default {};
}
