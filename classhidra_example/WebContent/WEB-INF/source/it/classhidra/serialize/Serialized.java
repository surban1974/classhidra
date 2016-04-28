package it.classhidra.serialize;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Serialized {
	Format output() default @Format;
	Format input() default @Format;
	boolean children() default false;
	int depth() default 0;
}
