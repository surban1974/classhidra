package it.classhidra.annotation.elements;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NavigatedDirective {
	String memoryContent() default "";
	Entity entity() default @Entity;
}
