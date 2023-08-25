package starter.components.streams; 


import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import application.web.streams.app_control_permission;
import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.init.jwt_init;
//import it.classhidra.core.controller.streams.def_control_permission;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;

@Stream(	
		name="def_control_permission_jwt",	
		applied={
			@Apply_to_action(action="*")
		},						
		entity=@Entity(
			order = "2"
		)
)

public class CheckPermissionJWT extends app_control_permission implements i_stream{

	private static final long serialVersionUID = 1L;
	
	private jwt_init jwt = new jwt_init();
	private Algorithm algorithm = null; 
	private JWTVerifier verifier = null;


	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
		
		String id_action = (String)request.getAttribute(bsController.CONST_ID);
		info_action i_a = (info_action)bsController.getAction_config().get_actions().get(id_action);
		if(i_a!=null && i_a.getProperty("jwt").equals("check")){
			if(algorithm==null)
				algorithm = Algorithm.HMAC256(jwt.get_jwt_secret());
			if(verifier==null)
				verifier = JWT.require(algorithm)
				  .withIssuer(jwt.get_jwt_issuer())
				  .build();
			if(verifier!=null) {
				boolean doError = true;
				try {
					String jwtToken = null;
					if(jwt.get_jwt_cookie_name()!=null && !jwt.get_jwt_cookie_name().isEmpty()) {
						if(request.getCookies()!=null) {
							for(Cookie cookie : request.getCookies()) {
								if(cookie.getName().equals(jwt.get_jwt_cookie_name()))
									jwtToken = cookie.getValue();
							}
						}	
					}else 
						jwtToken = extractHeaderToken(request);
					
					if(jwtToken!=null) {
						DecodedJWT decodedJWT = verifier.verify(jwtToken);						
						
						
						auth_init auth = bsController.checkAuth_init(request);
						if(auth!=null) {
							Claim claim = decodedJWT.getClaim("userId");
							if(claim!=null && claim.asString().equals(auth.get_matricola()))
								doError = false;
							if(!doError && jwt.get_jwt_reloadIfExpireIn()!=null && !jwt.get_jwt_reloadIfExpireIn().isEmpty()) {
								Date expired = decodedJWT.getExpiresAt();
								if(System.currentTimeMillis() + Long.valueOf(jwt.get_jwt_reloadIfExpireIn()) > expired.getTime())
									recreateJwtToken(auth, response);
							}
						}
						
					}
					
					
						

				    
				} catch (Exception e) {
				    e.toString();
				}	
				if(doError) {
					String redirectURI=null;
					redirectURI = service_ErrorRedirect(id_action,request.getSession().getServletContext(),request, response);
					 new bsControllerMessageException(
								"error_9",
								request,
								null,
								iStub.log_ERROR,
								new Object[]{});
					 return new redirects(redirectURI);
				}
				
			}
			
			
		}
		
		
		return super.streamservice_enter(request, response);
//		return null;
	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {
		
		String id_action = (String)request.getAttribute(bsController.CONST_ID);
		info_action i_a = (info_action)bsController.getAction_config().get_actions().get(id_action);
		if(i_a!=null && i_a.getProperty("jwt").equals("provider")){
			action instance = (action)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTION);
			if(instance!=null) {
				if(algorithm==null)
					algorithm = Algorithm.HMAC256(jwt.get_jwt_secret());
				auth_init auth = bsController.checkAuth_init(request);
				if(auth!=null && auth.is_logged()) {
					recreateJwtToken(auth, response);
				}
				
			}
							
		}
		
		return super.streamservice_enter(request, response);
//		return null;
	}
	
	private void recreateJwtToken(auth_init auth, HttpServletResponse response) {
		Builder jwtTokenBuilder = JWT.create()
				  .withIssuer(jwt.get_jwt_issuer())
				  .withSubject(jwt.get_jwt_subject())
				  .withClaim("userId", auth.get_matricola())
				  .withClaim("userName", auth.get_user())
				  .withClaim("userRole", auth.get_ruolo())
				  .withClaim("userTarget", auth.get_target())
				  .withIssuedAt(new Date())
				  .withJWTId(UUID.randomUUID().toString());
		
			try {
				jwtTokenBuilder.withExpiresAt(new Date(System.currentTimeMillis() + Long.valueOf(jwt.get_jwt_expiresAt())));
			}catch (Exception e) {
			}
			
			try {
				jwtTokenBuilder.withNotBefore(new Date(System.currentTimeMillis() + Long.valueOf(jwt.get_jwt_notBefore())));
			}catch (Exception e) {
			}	
			
			String jwtToken = jwtTokenBuilder.sign(algorithm);
			
			if(jwt.get_jwt_cookie_name()!=null && !jwt.get_jwt_cookie_name().isEmpty()) {
				Cookie elCooki = new Cookie(jwt.get_jwt_cookie_name(),jwtToken);
				elCooki.setMaxAge(60*60);
				if(jwt.get_jwt_cookie_path()!=null && !jwt.get_jwt_cookie_path().isEmpty()) 
					elCooki.setPath(jwt.get_jwt_cookie_path());
				if(jwt.get_jwt_cookie_domain()!=null && !jwt.get_jwt_cookie_domain().isEmpty()) 
					elCooki.setDomain(jwt.get_jwt_cookie_domain());
				response.addCookie(elCooki);
			}else { 
				response.addHeader(jwt_init.REQUEST_HEADER_AUTHORIZATION, jwt_init.REQUEST_HEADER_AUTHORIZATION_VALUE_BEARER_TYPE+" "+jwtToken);
			}

	}

	private String extractHeaderToken(HttpServletRequest request){
		Enumeration<String> headers = request.getHeaders(jwt_init.REQUEST_HEADER_AUTHORIZATION);
		while (headers.hasMoreElements()) { 
			// typically there is only one (most servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(jwt_init.REQUEST_HEADER_AUTHORIZATION_VALUE_BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(jwt_init.REQUEST_HEADER_AUTHORIZATION_VALUE_BEARER_TYPE.length()).trim();
				return authHeaderValue;
			}
		}
		return null;
	}	
	
	private String service_ErrorRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(bsController.getAction_config()==null ||
				bsController.getAction_config().getAuth_error()==null ||
				bsController.getAction_config().getAuth_error().equals("")){
				redirectURI="";
			}else redirectURI = bsController.getAction_config().getError();

		}catch(Exception ex){
			bsController.writeLog(request, "Controller authentication error. Action: ["+id_action+"] URI: ["+redirectURI+"]");
		}
		return redirectURI;
	}	
	
}
