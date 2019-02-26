package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TemporaryLinked {
	String value() default "";	
	Class<?> reference() default void.class;	
	boolean unlinkAndSetNull() default true;
}
