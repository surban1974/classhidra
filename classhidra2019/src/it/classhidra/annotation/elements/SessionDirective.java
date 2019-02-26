package it.classhidra.annotation.elements;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SessionDirective {
	Entity entity() default @Entity;
}
