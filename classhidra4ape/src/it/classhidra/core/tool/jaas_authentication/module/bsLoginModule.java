package it.classhidra.core.tool.jaas_authentication.module;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.jaas_authentication.handler.PassiveCallbackHandler;
import it.classhidra.core.tool.jaas_authentication.principal.bsCredential;
import it.classhidra.core.tool.jaas_authentication.principal.bsPrincipal;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.*;

/* Security & JAAS imports */

import javax.security.auth.spi.LoginModule;
import javax.security.auth.login.LoginException;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;



public class bsLoginModule implements LoginModule {

    // initial state
    CallbackHandler callbackHandler;
    Subject  subject;
    Map      sharedState;
    Map      options;

    // temporary state
    Vector   tempCredentials;
    Vector   tempPrincipals;

    // the authentication status
    boolean  success;

    // configurable options
    boolean  debug;
 
    public bsLoginModule() {
        tempCredentials = new Vector();
        tempPrincipals  = new Vector();
        success = false;
        debug   = false;
    }

 
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map sharedState, Map options) {

        // save the initial state
        this.callbackHandler = callbackHandler;
        this.subject     = subject;
        this.sharedState = sharedState;
        this.options     = options;

        // initialize any configured options
        if (options.containsKey("debug"))
            debug = "true".equalsIgnoreCase((String)options.get("debug"));


        if (debug)	bsController.writeLog("[bsLoginModule] initialize", iStub.log_INFO); 
        
    }


    public boolean login() throws LoginException {

        if (debug) bsController.writeLog("[bsLoginModule] login", iStub.log_INFO);

        if (callbackHandler == null)
            throw new LoginException("Error: no CallbackHandler available " +
                    "to garner authentication information from the user");

        try {
            // Setup default callback handlers.
            Callback[] callbacks = new Callback[] {
                new NameCallback("Username: "),
                new PasswordCallback("Password: ", false)
            };

            callbackHandler.handle(callbacks);

            String username = ((NameCallback)callbacks[0]).getName();
            String password = new String(((PasswordCallback)callbacks[1]).getPassword());

            ((PasswordCallback)callbacks[1]).clearPassword();

            success = bsValidate(username, password);

            callbacks[0] = null;
            callbacks[1] = null;

            if (!success)
                throw new LoginException("Authentication failed: Password does not match");

            return(true);
        } catch (LoginException ex) {
            throw ex;
        } catch (Exception ex) {
            success = false;
            throw new LoginException(ex.getMessage());
        }
    }

     public boolean commit() throws LoginException {

        if (debug)
        	bsController.writeLog("[bsLoginModule] commit", iStub.log_INFO);           

        if (success) {

            if (subject.isReadOnly()) {
                throw new LoginException ("Subject is Readonly");
            }

            try {
                Iterator it = tempPrincipals.iterator();
                
                if (debug) {
                    while (it.hasNext())
                    	bsController.writeLog("[bsLoginModule] Principal: " + it.next().toString(), iStub.log_INFO); 
                }

                subject.getPrincipals().addAll(tempPrincipals);
                subject.getPublicCredentials().addAll(tempCredentials);

                tempPrincipals.clear();
                tempCredentials.clear();

                if(callbackHandler instanceof PassiveCallbackHandler)
                    ((PassiveCallbackHandler)callbackHandler).clearPassword();

                return(true);
            } catch (Exception ex) {
                throw new LoginException(ex.getMessage());
            }
        } else {
            tempPrincipals.clear();
            tempCredentials.clear();
            return(true);
        }
    }

 
    public boolean abort() throws javax.security.auth.login.LoginException {

        if (debug)
        	bsController.writeLog("[bsLoginModule] abort", iStub.log_INFO);  

        // Clean out state
        success = false;

        tempPrincipals.clear();
        tempCredentials.clear();

        if (callbackHandler instanceof PassiveCallbackHandler)
            ((PassiveCallbackHandler)callbackHandler).clearPassword();

        logout();

        return(true);
    }

    public boolean logout() throws javax.security.auth.login.LoginException {

        if (debug) bsController.writeLog("[bsLoginModule] logout", iStub.log_INFO); 

        tempPrincipals.clear();
        tempCredentials.clear();

        if (callbackHandler instanceof PassiveCallbackHandler)
            ((PassiveCallbackHandler)callbackHandler).clearPassword();

        // remove the principals the login module added
        Iterator it = subject.getPrincipals(bsPrincipal.class).iterator();
        while (it.hasNext()) {
            bsPrincipal p = (bsPrincipal)it.next();
            if(debug)
            	bsController.writeLog("[bsLoginModule] removing principal "+p.toString(), iStub.log_INFO); 
            subject.getPrincipals().remove(p);
        }

        // remove the credentials the login module added
        it = subject.getPublicCredentials(bsCredential.class).iterator();
        while (it.hasNext()) {
            bsCredential c = (bsCredential)it.next();
            if(debug) bsController.writeLog("[bsLoginModule] removing credential "+c.toString(), iStub.log_INFO);
            subject.getPrincipals().remove(c);
        }

        return(true);
    }

     private boolean bsValidate(String user, String pass) throws Exception {
        loadUser_config();
        boolean passwordMatch = false;
        info_user _user = (info_user)((load_users)bsController.getUser_config()).get_user(user,pass);
        if(_user!=null){
        	passwordMatch = true;
        	this.tempPrincipals.add(new bsPrincipal(_user.getMatriculation()));
        	bsCredential bsc = new bsCredential();
 	        	bsc.setProperty("_ruolo",_user.getGroup().replace(';','^'));
	        	bsc.setProperty("_language",_user.getLanguage());
	        	bsc.setProperty("_matricola",_user.getMatriculation());
	    		bsc.setProperty("_target",_user.getTarget().replace(';','^'));
    		this.tempCredentials.add(bsc);
        }
 
        return (passwordMatch);
    }
     
 	public void  loadUser_config() {
		if(bsController.getUser_config()==null){
			bsController.setUser_config(new load_users());
 			try{
 				((load_users)bsController.getUser_config()).init();
 				if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).load_from_resources();
 				if(((load_users)bsController.getUser_config()).isReadError()) ((load_users)bsController.getUser_config()).init(bsController.getAppInit().get_path_config()+bsController.CONST_XML_USERS);
 				if(((load_users)bsController.getUser_config()).isReadError()) bsController.setUser_config(null);
 			}catch(bsControllerException je){
 				bsController.setUser_config(null);
 			}
 		}
 	}
}

