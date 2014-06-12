package it.classhidra.annotation.elements;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
	Entity entity() default @Entity;
	String type() default "";
	String name() default "";
	String model() default "";
	String listener() default "";
}
