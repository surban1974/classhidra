package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	Entity entity() default @Entity;
	String path() default "";
	String type() default "";	
	String name() default "";
	String method() default "";
	String redirect() default "";
	String error() default "";
	String memoryInSession() default "";
	String memoryInServletContext() default "";
	String memoryInServletContextLoadOnStartup() default "-1";
	String memoryAsLastInstance() default "";
	String reloadAfterAction() default "";
	String reloadAfterNextNavigated() default "";	
	String navigated() default "";
	String navigatedMemoryContent() default "";
	String syncro() default "false";
	String statistic() default "true";
	String help() default "";
	String listener() default "";
	Bean Bean() default @Bean;
	Redirect Redirect() default @Redirect;
	Redirect[] redirects() default {};
	Transformation[] transformations() default {};
	ActionCall[] calls() default {};
	Bean[] beans() default {};
	Expose Expose() default @Expose;
	Async Async() default @Async;
}
