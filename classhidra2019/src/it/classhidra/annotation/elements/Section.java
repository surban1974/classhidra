package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Section {
	Entity entity() default @Entity;
	String name();	
	boolean allowed() default true;
}
