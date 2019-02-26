package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
	boolean value() default false;	
	long timeout() default 0;
	boolean flushBuffer() default false;
	long loopEvery() default 0;
	boolean reInitBeenEveryLoop() default false;
	ResponseHeader[] headers() default {};
}
