package it.classhidra.serialize;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Serialized {
	boolean value() default true;
	Format output() default @Format;
	Format input() default @Format;
	boolean children() default false;
	int order() default -1;
	int depth() default 0;
}
