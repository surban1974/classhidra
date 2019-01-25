package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TemporaryLinked {
	String value() default "";	
	boolean unlinkAndSetNull() default true;
}
