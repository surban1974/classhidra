package it.classhidra.annotation.elements;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Stream {
	Entity entity() default @Entity;
	String name() default "";
	Apply_to_action[] applied() default {};
}
