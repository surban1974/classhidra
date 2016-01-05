package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface Access {
	AccessRelation[] allowed() default {};
	AccessRelation[] forbidden() default {};
}
