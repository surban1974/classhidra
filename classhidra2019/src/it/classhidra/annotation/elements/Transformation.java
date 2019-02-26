package it.classhidra.annotation.elements;

import it.classhidra.core.controller.info_transformation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Transformation {
	Entity entity() default @Entity;
	String name() default "";
	String type() default "";
	String path() default "";
	String event() default info_transformation.CONST_EVENT_BEFORE;
	String inputformat() default "string";
}
