/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/

package it.classhidra.core.tool.exception;


import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class bsControllerMessageException extends bsControllerException {
	private static final long serialVersionUID = -3214500155618658056L;
	private message mess;
	private Exception native_Exception;
	private Throwable native_Throwable;

/*
public bsControllerMessageException() {
	super();
}
*/
public bsControllerMessageException(Exception exc) {
	super(exc.toString(),iStub.log_ERROR);
	native_Exception = exc;
	if(exc instanceof bsControllerMessageException){
		mess = ((bsControllerMessageException)exc).getMess();
	}
}

public bsControllerMessageException(message mess, HttpServletRequest request) {
	
	super(
			(mess!=null) ? mess.getDESC_MESS() : "",
			request,
			(mess!=null) 
				? 
				(mess.getTYPE().equals("E")) ? iStub.log_ERROR : (mess.getTYPE().equals("W")) ? iStub.log_WARN : ""
				:
				""	
	);
	

	if(request!=null && mess!=null){
		try{
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			Vector<message> list = null;
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,iStub.log_ERROR);
		}
	}
}

public bsControllerMessageException(message mess, HttpServletRequest request,String level) {
	super((mess!=null)?mess.getDESC_MESS(null):"",request,level);
	if(request!=null && mess!=null){
		try{
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			Vector<message> list = null;
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}

public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Exception exc, String level) {
	super(decode(cd_mess,request,exc,null).get_log_mess(),request,level);
	native_Exception = exc;
	mess = decode(cd_mess,request,exc,null);
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}
public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Exception exc, String level, HashMap<String,String> parameters) {
	super(decode(cd_mess,request,exc,parameters).get_log_mess(),request,level);
	native_Exception = exc;
	mess = decode(cd_mess,request,exc,parameters);
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}
public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Exception exc, String level, Object[] o_parameters) {
	super(decode(cd_mess,request,exc,prepare_hm_parameters(o_parameters)).get_log_mess(),request,level);
	native_Exception = exc;
	mess = decode(cd_mess,request,exc,prepare_hm_parameters(o_parameters));
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}


public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Throwable exc,String level) {
	super(decode(cd_mess,request,exc,null).get_log_mess(),request,level);
	native_Throwable = exc;
	mess = decode(cd_mess,request,exc,null);
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));	
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}
public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Throwable exc,String level,HashMap<String,String> parameters) {
	super(decode(cd_mess,request,exc,parameters).get_log_mess(),request,level);
	native_Throwable = exc;
	mess = decode(cd_mess,request,exc,parameters);
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));	
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}
public bsControllerMessageException(String cd_mess, HttpServletRequest request,  Throwable exc,String level,Object[] o_parameters) {
	super(decode(cd_mess,request,exc,prepare_hm_parameters(o_parameters)).get_log_mess(),request,level);
	native_Throwable = exc;
	mess = decode(cd_mess,request,exc,prepare_hm_parameters(o_parameters));
	if(mess!=null && mess.getTYPE()!=null && mess.getTYPE().equals("?") && level!=null && level.length()>0)
		mess.setTYPE(String.valueOf(level.charAt(0)));	
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list!=null) list.add(mess);
		}catch(Exception e){
			new bsControllerException(e.toString(), request,level);
		}
	}
}



public static message decode (String cd_mess,HttpServletRequest request, Exception exp,HashMap<String,String> parameters){
	message mess_in = null;
	String lang="IT";
	try{
		auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
		lang = aInit.get_language();
	}catch(Exception e){
	}
	if(cd_mess==null || cd_mess.equals("?"))	cd_mess = get_fromLib(exp);
	if(	bsController.getMess_config()!=null && bsController.getMess_config().get_messages()!=null){
		if(bsController.getMess_config().get_messages().get(lang+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang+"."+cd_mess)).clone();
		if(mess_in==null && bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)).clone();
		if(mess_in==null && bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)).clone();
	}
	if(mess_in==null){
		mess_in = new message();
		mess_in.setTYPE("?");
		mess_in.setDESC_MESS(cd_mess);
		mess_in.setCD_LANG("?");
	}
	String _log = "";
	if(mess_in.getTYPE().equals("E")) _log+=" [mes. user error] ";
	if(mess_in.getTYPE().equals("I")) _log+=" [mes. user info] ";
	if(mess_in.getTYPE().equals("W")) _log+=" [mes. user warning] ";
	if(mess_in.getTYPE().equals("?")) _log+=" [mes. user generic] ";

	mess_in.setParameters(parameters);
	String mess_in_desc = mess_in.getDESC_MESS();

	if(request==null) _log=_log + mess_in_desc + ((exp!=null)?" -> "+exp.toString():"");
	else _log=_log + mess_in_desc+ ((exp!=null)?" -> "+exp.toString():"");
	
	boolean isDebug=false;
	try{
		isDebug = System.getProperty("debug").equals("true");
	}catch(Exception e){	
		try{
			isDebug = bsController.getAppInit().get_debug().equals("true");
		}catch(Exception ex){					
		}
	}

	
	if(isDebug && request!=null){
		@SuppressWarnings("unchecked")
		Vector<String> s_log = (Vector<String>)request.getSession().getAttribute(bsController.CONST_SESSION_LOG);
		if(s_log==null) s_log = new Vector<String>();
		if(exp!=null) s_log.add(exp.toString());
		if(mess_in_desc!=null && !mess_in_desc.equals(""))  s_log.add(mess_in_desc);
		request.getSession().setAttribute(bsController.CONST_SESSION_LOG, s_log);
	}
	mess_in.set_log_mess(_log);
	return mess_in;
}
public static message decode (String cd_mess,HttpServletRequest request, Throwable exp, HashMap<String,String> parameters){
	message mess_in = null;
	String lang="IT";
	try{
		auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
		lang = aInit.get_language();
	}catch(Exception e){
	}
	if(cd_mess==null || cd_mess.equals("?"))	cd_mess = get_fromLib(exp);
	if(	bsController.getMess_config()!=null && bsController.getMess_config().get_messages()!=null){
		if(bsController.getMess_config().get_messages().get(lang+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang+"."+cd_mess)).clone();
		if(mess_in==null && bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)).clone();
		if(mess_in==null && bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)!=null)
			mess_in = (message)((message)bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)).clone();
	}
	if(mess_in==null){
		mess_in = new message();
		mess_in.setTYPE("?");
		mess_in.setDESC_MESS(cd_mess);
		mess_in.setCD_LANG("?");
	}
	String _log = "";
	if(mess_in.getTYPE().equals("E")) _log+=" [mes. user error] ";
	if(mess_in.getTYPE().equals("I")) _log+=" [mes. user info] ";
	if(mess_in.getTYPE().equals("W")) _log+=" [mes. user warning] ";
	if(mess_in.getTYPE().equals("?")) _log+=" [mes. user generic] ";

	mess_in.setParameters(parameters);
	String mess_in_desc = mess_in.getDESC_MESS();


	if(request==null) _log=_log + mess_in_desc + ((exp!=null)?" -> "+exp.toString():"");
	else _log=_log + mess_in_desc+ ((exp!=null)?" -> "+exp.toString():"");
	
	boolean isDebug=false;
	try{
		isDebug = System.getProperty("debug").equals("true");
	}catch(Exception e){	
		try{
			isDebug = bsController.getAppInit().get_debug().equals("true");
		}catch(Exception ex){					
		}
	}
	
	if(isDebug && request!=null){
		@SuppressWarnings("unchecked")
		Vector<String> s_log = (Vector<String>)request.getSession().getAttribute(bsController.CONST_SESSION_LOG);
		if(s_log==null) s_log = new Vector<String>();
		if(exp!=null) s_log.add(exp.toString());
		if(mess_in_desc!=null && !mess_in_desc.equals(""))  s_log.add(mess_in_desc);

		request.getSession().setAttribute(bsController.CONST_SESSION_LOG, s_log);
	}
	mess_in.set_log_mess(_log);
	return mess_in;
}

public static HashMap<String,String> prepare_hm_parameters(Object[] o_parameters){
	HashMap<String,String> result = null;
	if(o_parameters==null || o_parameters.length==0) return result;
	result=new HashMap<String, String>();
	for(int i=0;i<o_parameters.length;i++)
		result.put(Integer.valueOf(i).toString(), (o_parameters[i]==null)?"":o_parameters[i].toString());
	return result;
}

public static boolean clearAllMessages(HttpServletRequest request){
	boolean isNotEmpty = false;
	if(request!=null){
		try{
			Vector<message> list = null;
			if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)==null) request.getSession().setAttribute(bsController.CONST_BEAN_$LISTMESSAGE, new Vector<message>());
			@SuppressWarnings("unchecked")
			Vector<message> attribute = (Vector<message>)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
			list = attribute;
			if(list.size()>0) isNotEmpty = true;
			bsController.writeLog("*** LISTMESSAGE" + list.toString(),iStub.log_INFO);
			list.clear();
		}catch(Exception e){
			new bsControllerException(e.toString(), request,iStub.log_ERROR);
		}
	}
	return isNotEmpty;
}

private static String get_fromLib(Throwable exp){
	return exp.toString();
}
public message getMess() {
	return mess;
}
public String toString(){
	if(mess!=null){
		return mess.getDESC_MESS(null);
	}
	return "";
}

	public Exception getNative_Exception() {
		return native_Exception;
	}
	public Throwable getNative_Throwable() {
		return native_Throwable;
	}
}