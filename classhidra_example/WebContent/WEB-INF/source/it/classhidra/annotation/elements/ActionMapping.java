package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMapping {
	String error() default "";
	String auth_error() default "";
	String session_error() default "";
	String listener_actions() default "";
	String listener_beans() default "";
	String listener_streams() default "";
	String memoryInContainer_streams() default "false";
	String provider() default "";
	String instance_navigated() default "";
	String instance_local_container() default "";
	String instance_scheduler_container() default "";
	String instance_onlysession() default "";
	String instance_servletcontext() default "";
	Stream[] streams() default {};
	Bean[] beans() default {};
	Redirect[] redirects() default {};
	Action[] actions() default {};
	Transformation[] transformations() default {};
	

}
