/**
* Creation date: (08/09/2010)
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
package it.classhidra.core.controller;



import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.jaas_authentication.info_user;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_format;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class wsController   {


	public String getId_UserSOAP(String user, String password,String isCodedInput){

		if(isCodedInput!=null && isCodedInput.toUpperCase().equals("TRUE")){
			BASE64Decoder decoder = new BASE64Decoder();
			try{
				user = new String(decoder.decodeBuffer(user));
			}catch(Exception e){
			}
			try{
				password = new String(decoder.decodeBuffer(password));
			}catch(Exception e){
			}

		}

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
		info_user _user = ((load_users)bsController.getUser_config()).get_user(user,password);
	    if(_user!=null){

	    	auth_init auth = new auth_init();

	    	auth.set_user(_user.getName());
	    	auth.set_userDesc(_user.getDescription());
	    	auth.set_ruolo(_user.getGroup());
	    	auth.set_language(_user.getLanguage());
	    	auth.set_matricola(_user.getMatriculation());
	    	auth.set_target(_user.getTarget().replace(';','^'));
	    	auth.get_target_property().put(bsConstants.CONST_AUTH_TARGET_ISTITUTION, auth.get_target());
	    	auth.set_logged(true);
	    	String redirectSSOID = auth.get_matricola()+"$$"+auth.get_ruolo()+"$$"+util_format.dataToString(new Date(), "yyyyMMddHHmm");

			try{
				redirectSSOID = bsController.encrypt(redirectSSOID.toUpperCase());
			}catch(Exception e){
			}
			return redirectSSOID;
	    }

		return "";
	}
	public String PerformActionSOAP(String id_action, String ssoid, String inputXML, String isCodedInput, String isCodedOutput) {

		HashMap wsParameters = new HashMap();

		wsParameters.put(bsController.CONST_ID, id_action);
		wsParameters.put(bsController.CONST_SSOID, ssoid);

		String outputXML="";
		Vector errors=new Vector();
		auth_init auth = null;
		if(auth==null) auth = new auth_init();

		StatisticEntity stat = null;
		try{
			stat = new StatisticEntity(
					"WS",
					auth.get_user_ip(),
					auth.get_matricola(),
					auth.get_language(),
					id_action,
					null,
					new Date(),
					null,
					null);
		}catch(Exception e){
		}


		if(isCodedInput!=null && isCodedInput.toUpperCase().equals("TRUE")){
			BASE64Decoder decoder = new BASE64Decoder();
			try{
				inputXML = new String(decoder.decodeBuffer(inputXML));
			}catch(Exception e){
			}
		}

		if(id_action!=null){


			Vector _streams = new Vector();
			Vector _streams_orig = (Vector)bsController.getAction_config().get_streams_apply_to_actions().get("*");

			if(_streams_orig!=null) _streams.addAll(_streams_orig);
			Vector _streams4action = (Vector)bsController.getAction_config().get_streams_apply_to_actions().get(id_action);
			if(_streams4action!=null) _streams.addAll(_streams4action);

			i_action action_instance = null;



			try{
						try{
							performStream_Enter(_streams, id_action,action_instance,wsParameters );
						}catch (Exception e) {
							throw e;
						}

						action_instance = bsController.getAction_config().actionFactory(id_action);

						i_bean bean_instance = bsController.getAction_config().beanFactory(action_instance.get_infoaction().getName());
						if(bean_instance!=null){
							bean_instance.reimposta();
							i_bean fromXML = null;
							try{
								fromXML = (i_bean)util_beanMessageFactory.message2bean(inputXML);
							}catch(Exception e){
							}
							if(fromXML!=null)
								bean_instance.reInit(fromXML);
						}


						action_instance.onPreSet_bean();
						action_instance.set_bean(bean_instance);
						action_instance.onPostSet_bean();

						action_instance.onPreInit(wsParameters);
						action_instance.init(wsParameters);
						action_instance.onPostInit(wsParameters);

						redirects redirect = null;
						if(action_instance.get_infoaction().getSyncro().toLowerCase().equals("true")){
							action_instance.onPreSyncroservice(wsParameters);
							redirect = action_instance.syncroservice(wsParameters);
							action_instance.onPostSyncroservice(redirect,wsParameters);
						}else{
							action_instance.onPreActionservice(wsParameters);
							redirect = action_instance.actionservice(wsParameters);
							action_instance.onPostActionservice(redirect,wsParameters);
						}

//						redirects redirect = action_instance.actionservice(wsParameters);


						if(action_instance!=null && action_instance.get_bean()!=null){
							String output4SOAP = (String)action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4SOAP);
							if(output4SOAP==null)
								outputXML = util_beanMessageFactory.bean2xml(action_instance.get_bean(),action_instance.get_bean().get_infobean().getName(),true);
							else outputXML = output4SOAP;
						}


						try{
							performStream_Exit(_streams, id_action,action_instance,wsParameters );
						}catch (Exception e) {
							throw e;
						}

						if(redirect!=null){
							String id_current = check_DO(redirect.get_uri());
							if(id_current!=null){
								outputXML = PerformActionSOAP(id_action, ssoid, outputXML, isCodedInput, isCodedOutput);
							}
						}


			}catch(bsControllerException e){
				errors.add(e.toString());
			}catch(Exception ex){
				errors.add(ex.toString());
			}catch(Throwable t){
				errors.add(t.toString());
			}

		}


		if(errors.size()>0){
			outputXML+=	util_beanMessageFactory.bean2xml(errors);
		}

		if(isCodedOutput!=null && isCodedOutput.toUpperCase().equals("TRUE")){
			BASE64Encoder encoder = new BASE64Encoder();
			try{
				outputXML = new String(encoder.encode(outputXML.getBytes()));
			}catch(Exception e){
			}
		}

		if(stat!=null){
			stat.setFt(new Date());
			bsController.putToStatisticProvider(stat);
		}

		return outputXML;
	}

	public  boolean performStream_Enter(Vector _streams, String id_action,i_action action_instance, HashMap wsParameters) throws bsControllerException, Exception, Throwable{
		for(int i=0;i<_streams.size();i++){
			info_stream iStream = (info_stream)_streams.get(i);
			i_stream currentStream = bsController.getAction_config().streamFactory(iStream.getName());
			if(currentStream!=null){
				currentStream.onPreEnter(wsParameters);
				redirects currentStreamRedirect = currentStream.streamservice_enter(wsParameters);
				currentStream.onPostEnter(currentStreamRedirect,wsParameters);
				if(currentStreamRedirect!=null){
					throw new bsControllerException("BLOCKED from ENTER stream:"+currentStream.get_infostream().getName(),iStub.log_ERROR);
				}
			}
		}
		return true;
	}

	public  boolean performStream_Exit(Vector _streams, String id_action,i_action action_instance, HashMap wsParameters) throws bsControllerException, Exception, Throwable{
		for(int i=_streams.size()-1;i>-1;i--){
			info_stream iStream = (info_stream)_streams.get(i);
			i_stream currentStream = bsController.getAction_config().streamFactory(iStream.getName());
			if(currentStream!=null){
				currentStream.onPreExit(wsParameters);
				redirects currentStreamRedirect = currentStream.streamservice_exit(wsParameters);
				currentStream.onPostExit(currentStreamRedirect,wsParameters);
				if(currentStreamRedirect!=null){
					throw new bsControllerException("BLOCKED from EXIT stream:"+currentStream.get_infostream().getName(),iStub.log_ERROR);
				}
			}
		}
		return true;
	}

	public String check_DO(String url) throws ServletException{





			String id_current = null;
			if(id_current==null){
				try{
					if(url.lastIndexOf("/actions")+8==url.length()) url+="/";
					if(url.lastIndexOf("/")>-1) id_current = url.substring(url.lastIndexOf("/")+1);
				}catch(Exception e){}
			}
			if(id_current!=null){
				if(id_current.indexOf(bsConstants.CONST_EXTENTION_DO)>-1){
					id_current = util_format.replace(id_current,bsConstants.CONST_EXTENTION_DO,"");
					return id_current;
				}
				if(	id_current.equals("") &&
					(url.lastIndexOf("/actions/")+9==url.length())
				){
					return bsController.getAppInit().get_enterpoint();
				}
			}

		return null;
	}


}