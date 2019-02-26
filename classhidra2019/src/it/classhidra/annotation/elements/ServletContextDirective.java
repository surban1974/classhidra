package it.classhidra.annotation.elements;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ServletContextDirective {
	int loadOnStartup() default -1;
	Entity entity() default @Entity;
}
