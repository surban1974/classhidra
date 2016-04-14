package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {
	final String GET = "GET"; 
	final String POST = "POST";
	final String PUT = "PUT";
	final String PATCH = "PATCH";
	final String DELETE = "DELETE";

	String method() default ""; 
	String[] methods() default {};
	Rest[] restmapping() default {};
}
