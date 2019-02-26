package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AccessRelation {
	String targets() default "";
	String rules() default "";
	String elements() default "";
	String middleactions() default "";
}
