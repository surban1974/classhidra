package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMapping {
	String error() default "";
	String auth_error() default "";
	String session_error() default "";
	Stream[] streams() default {};
	Bean[] beans() default {};
	Redirect[] redirects() default {};
	Action[] actions() default {};
	Transformation[] transformations() default {};
	

}
