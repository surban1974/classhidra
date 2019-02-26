package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface ActionService {
	Entity entity() default @Entity;
	Redirect Redirect() default @Redirect;
	Expose Expose() default @Expose;
	Async Async() default @Async;
}
