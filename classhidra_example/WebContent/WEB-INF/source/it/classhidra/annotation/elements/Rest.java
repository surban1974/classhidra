package it.classhidra.annotation.elements;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Rest {
	
	/*	
	200: OK, The requested object exist.
	201: Created, the object has been created.
	400: Bad Request, required parameters are missing.
	401: Unauthorized, Your App ID, Access Token and/or User ID are missing or invalid.
	404: Not Found, the requested object doesn’t exist.
	405: Method Not Allowed, the called method doesn’t exist.
	500, 503: Server Errors, something went wrong on our side.	
*/		
	final int EXIST_200 				= 200;
	final int CREATED_201				= 201;
	final int MISSING_PARAMETERS_400 	= 400;
	final int UNAUTHORIZED_401 			= 401;
	final int NOTFOUND_404 				= 404;
	final int NOTALLOWED_405 			= 405;
	final int SERVER_ERROR_500 			= 500;
	final int SERVER_ERROR_503 			= 503;
	

	String path() default ""; 
	String parametermapping() default "";
}
