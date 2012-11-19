package it.classhidra.annotation.elements;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Apply_to_action {
	Entity entity() default @Entity;
	String action() default "";
	String excluded() default "false";
}
