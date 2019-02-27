package it.classhidra.core.tool.jaas_authentication;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_auth_manager;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.handler.PassiveCallbackHandler;
import it.classhidra.core.tool.jaas_authentication.principal.bsCredential;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_format;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class auth_manager implements i_auth_manager {

	private static final long serialVersionUID = -3596589226289167079L;
	public boolean login_JAAS(auth_init auth,String password, HttpServletRequest request){
	    try {
		    String user = auth.get_user();
		    String pass = password;

	    
		    PassiveCallbackHandler cbh = new PassiveCallbackHandler(user, pass);

		    LoginContext lc = null;
		    try{
		    	lc = new LoginContext(auth.getJaas_policyname(), cbh);
		    }catch(Exception e){		    	
		    	File jaas_config = new File(util_classes.getPathPackageConfig() + auth.getJaas_defaultfilename());
				if(jaas_config.exists())
					System.setProperty(auth.getJaas_systemname(),jaas_config.getAbsolutePath() );
				else{
					
					jaas_config = new File(bsController.getAppInit().get_path_config() + auth.getJaas_defaultfilename());
					if(jaas_config.exists()){
						System.setProperty(auth.getJaas_systemname(),jaas_config.getAbsolutePath());
					}else{
						jaas_config = new File(auth.getJaas_defaultfilename());
						if(jaas_config.exists())
							System.setProperty(auth.getJaas_systemname(),jaas_config.getAbsolutePath());
					}
				}

//		    	System.setProperty(auth.getJaas_systemname(), (bsController.getAppInit().get_path_config() + auth.getJaas_defaultfilename()).replace('\\', '/'));
		    	lc = new LoginContext(auth.getJaas_policyname(), cbh);
		    }
		    lc.login();

		    	
	        final Iterator<Principal> it = lc.getSubject().getPrincipals().iterator();
	        while (it.hasNext()){ 
	        	bsController.writeLog("Authenticated: " + it.next().toString(), iStub.log_INFO);
	        }    

	        final Iterator<Properties> it1 = lc.getSubject().getPublicCredentials(Properties.class).iterator();
	        bsCredential credentials = (bsCredential)it1.next();

	        auth.set_ruolo( credentials.getProperty("_ruolo").replace('^', ';'));
	        auth.set_language(credentials.getProperty("_language"));
	        auth.set_matricola(credentials.getProperty("_matricola"));
	        auth.set_target(credentials.getProperty("_target").replace(';','^'));
	        
	        auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_ISTITUTION, credentials.getProperty("_target").replace(';','^'));
		
//		    lc.logout();

	    } catch (Exception e) {
	    	bsController.writeLog("JAAS Exception "+ e, iStub.log_FATAL);
	    	message mess = new message();
	    		mess.setTYPE("E");
	    		mess.setDESC_MESS("JAAS Exception "+ e);
//	    	new bsControllerMessageException(mess,request,iStub.log_ERROR);
	    	auth.set_logged(false);	
	    	return false;
	    	
	    }	
	    auth.set_logged(true);
		return true;	
		
	}
	

	public boolean readTicker(auth_init auth, HttpServletRequest request){
		try{
			String naCookie = null;
			Cookie[] arCooki = request.getCookies();
			Cookie elCooki = null;
			if(arCooki == null) {
				arCooki= new Cookie[0];
			}	
			for(int i=0; i<arCooki.length; i++){
				elCooki = arCooki[i];
				naCookie = elCooki.getName();
				naCookie.trim();
				if(naCookie.indexOf(bsController.CONST_AUTH_TICKER) != -1){
					auth.set_ticker(elCooki.getValue().trim());
				}
			}
			if(!auth.get_ticker().equals("")){
//				_ticker = codifica(-1,_ticker);
//				_ticker = new String(new sun.misc.BASE64Decoder().decodeBuffer(_ticker));
				auth.set_ticker(confrontWithAll(auth.get_ticker()));
				StringTokenizer st = new StringTokenizer(auth.get_ticker(),"_");
				String local_m = st.nextToken();
				String local_r = st.nextToken();
				String local_h = st.nextToken();				
				if(local_h.equals(util_format.dataToString(new java.util.Date(),"yyyyMMddhh"))){
					auth.set_matricola(local_m);
					auth.set_ruolo(local_r+"");
					info_user _user = (info_user)((load_users)bsController.getUser_config()).get_matriculation().get(auth.get_matricola());
					if(_user!=null){
						auth.set_user(_user.getName());
						auth.set_ruoli(_user.getGroup());
						auth.set_target(_user.getTarget());
					}
					
				}
				if(!auth.get_matricola().equals("")) return true;
			}
		}catch(Exception ex){	
			new bsControllerException(ex,iStub.log_ERROR);
		}
		return false;
	}

	public boolean saveTicker(auth_init auth,HttpServletResponse response){
		try{

			String value = auth.get_matricola()+"_"+auth.get_ruolo()+"_"+util_format.dataToString(new java.util.Date(),"yyyyMMddhh");
			auth.set_ticker(bsController.encrypt(value));
			Cookie elCooki = new Cookie(bsController.CONST_AUTH_TICKER,auth.get_ticker());
			elCooki.setMaxAge(60*60);
			response.addCookie(elCooki);
		}catch(Exception ex){	
			new bsControllerException(ex,iStub.log_ERROR);
		}
		return false;
	}

	public boolean saveFromForm(auth_init auth, i_bean bean, HttpServletRequest request, HttpServletResponse response){
		info_user _user = ((load_users)bsController.getUser_config()).get_user(auth.get_user(),bean.get("password_old"));
		if(_user==null){
			new bsControllerMessageException("error_2",request,null,iStub.log_INFO);
			return false;
		}
		
		_user.setName((String)bean.get("name"));
		_user.setPassword((String)bean.get("password"));		
		_user.setMatriculation((String)bean.get("matriculation"));
		_user.setLanguage((String)bean.get("language"));
		_user.setGroup(((String)bean.get("group")).replace(';','^'));
		_user.setTarget(((String)bean.get("target")).replace(';','^'));
		
		try{
			_user.setPassword(bsController.encrypt(_user.getPassword()));
		}catch(Exception e){
		}
		String newfilecontent = bsController.getUser_config().toString();
		newfilecontent+="";
		
		File usersfile = new File(bsController.getAppInit().get_path_config()+bsConstants.CONST_XML_USERS);
		File copyfile = new File(bsController.getAppInit().get_path_config()+bsConstants.CONST_XML_USERS+"."+util_format.dataToString(new java.util.Date(),"yyyyMMddhhmmss")+"."+_user.getMatriculation());
		boolean exit = false;
		if(usersfile.exists() && copyFile(usersfile,copyfile)){
			exit = writeToFile(newfilecontent,usersfile);
		}
		if(exit){
			
			((load_users)bsController.getUser_config()).get_users().put(bsController.getIdApp()+"/"+_user.getName().toUpperCase()+"."+_user.getPassword(),_user.clone(info_user.class));
			try{
				((load_users)bsController.getUser_config()).get_users().remove(bsController.getIdApp()+"/"+_user.getName().toUpperCase()+"."+bsController.encrypt((String)bean.get("password_old")));
			}catch(Exception ex){
			}
			((load_users)bsController.getUser_config()).get_matriculation().put(_user.getMatriculation(),_user);

			reInitAuth(auth,_user);
			auth.saveTicker(response);
			new bsControllerMessageException("message_2",request,null,iStub.log_INFO);
			return true;
		}		
		return false;
	}

	public void reInitAuth(auth_init auth, info_user user){
		
		if(user==null){
			auth.reimposta();
			return; 
		}
		auth.set_user(user.getName());
		auth.set_ruolo(user.getGroup().replace(';','^'));
		auth.set_language(user.getLanguage());
		auth.set_matricola(user.getMatriculation());
		auth.set_target(user.getTarget().replace(';','^'));
		
		
	}


	
	private String confrontWithAll(String value){
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
 			
		String res = ((load_users)bsController.getUser_config()).checkTickerWithDate(value);
		return res;
	}

	private boolean copyFile(File src, File dst){
		try{	
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dst);
		    
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}catch(Exception e){
			new bsControllerMessageException(e);
			return false;
		}
		return true;
	}
	private boolean writeToFile(String content, File dst){
		try{
			FileOutputStream fileOutputStream = new FileOutputStream(dst);
			fileOutputStream.write(content.getBytes());
			fileOutputStream.close();
		}catch(Exception e){	
			new bsControllerMessageException(e);
			return false;		
		}
		return true;
	}

}
