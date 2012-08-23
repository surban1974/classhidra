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
package it.classhidra.core.controller;
import java.io.Serializable;

import it.classhidra.core.tool.util.util_format;


import javax.servlet.*;


public class redirects implements Serializable{ 

	private static final long serialVersionUID = 1326242236772367795L;
	private info_redirect _inforedirect;
	private String _uri;	
	private String _transformationName;
	private String _uriError;
	
	public redirects(String uri){		
		super();
		_uri = uri;
	}
	
	public redirects(String uri, String transformationName){		
		super();
		_uri = uri;
		_transformationName=transformationName;
	}	
	
	public String getRedirectUri(info_action _infoaction) {

		if(_infoaction!=null){
		}
		
	
			if(	_uri.indexOf("/Controller?$")==0 ||
				_uri.indexOf(bsController.getAppInit().get_extention_do()+"?")>0 ||
				_uri.lastIndexOf(bsController.getAppInit().get_extention_do()) == _uri.length()-bsController.getAppInit().get_extention_do().length()) return transformURI(_uri);
			if(_uri==null || _uri.equals("") || _infoaction==null) return null;
			if(	bodyURI(_uri).equals(bodyURI(_infoaction.getRedirect())) ||
				_infoaction.get_redirects().get(bodyURI(_uri))!=null ||
				_infoaction.get_redirects().get("*")!=null){
				return transformURI(_uri);
			}	
		
		return null;
	}
	
	public RequestDispatcher redirect(ServletContext scontext, info_action _infoaction) throws ServletException, UnavailableException{
		RequestDispatcher rd=null;
		if(_infoaction!=null){
		}
		
		if(	_uri.indexOf("/Controller?$")==0 ||
			_uri.indexOf(bsController.getAppInit().get_extention_do()+"?")>0 ||
			_uri.lastIndexOf(bsController.getAppInit().get_extention_do()) == _uri.length()-bsController.getAppInit().get_extention_do().length()) return scontext.getRequestDispatcher(transformURI(_uri));
		if(_uri==null || _uri.equals("") || _infoaction==null) return rd;
		if(	bodyURI(_uri).equals(bodyURI(_infoaction.getRedirect())) ||
			_infoaction.get_redirects().get(bodyURI(_uri))!=null ||
			_infoaction.get_redirects().get("*")!=null){
			rd = scontext.getRequestDispatcher(transformURI(_uri));
		}	
		return rd;
	}
	public RequestDispatcher redirectError(ServletContext scontext, info_action _infoaction) throws ServletException, UnavailableException{
		RequestDispatcher rd=null;
		if(_uriError==null || _uriError.equals("")){
			if(_infoaction.get_redirects().get(_uri)==null) return rd;
			info_redirect _inforedirect = (info_redirect)_infoaction.get_redirects().get(_uri); 
			_uriError = _inforedirect.getError();
		}
		if(_uriError==null) return rd;
		rd = scontext.getRequestDispatcher(transformURI(_uriError));
		return rd;
	}
	
	public String bodyURI(String uri){
		if(uri==null || uri.indexOf("?")==-1) return uri;
		return uri.substring(0,uri.indexOf("?"));
	}
	
	public String transformURI(String uri){
		String first="";
		String second="";
		if(bsController.getAppInit().get_extention_do().equals("")){
			first = uri;
			if(first.lastIndexOf("?")>-1){
				second=first.substring(first.lastIndexOf("?")+1,first.length());
				first=first.substring(0,first.lastIndexOf("?"));				
			}
			if(first.indexOf(".")>-1) return uri;
			String id_action="";
			
			if(first.lastIndexOf("/")>-1){
				id_action=first.substring(first.lastIndexOf("/")+1,first.length());
				first=first.substring(0,first.lastIndexOf("/")+1);
			}else{
				id_action=first;
				first="";
			}

			if(load_actions.get_actions().get(id_action)!=null){
				if(first.indexOf("/actions/")==0) first=util_format.replace(first,"/actions/","/");
				if(first.indexOf("actions/")==0) first=util_format.replace(first,"actions/","/");
				if(first.indexOf("/")==0) first=first.substring(1,first.length());
				uri=first+"/Controller?"+bsController.CONST_ID_$ACTION+"="+id_action+"&"+second;	
			}
			return uri;
		}else{
			if(uri.indexOf(bsController.getAppInit().get_extention_do()+"?")>0 || uri.lastIndexOf(bsController.getAppInit().get_extention_do()) == uri.length()-bsController.getAppInit().get_extention_do().length()){
				int pos = uri.indexOf(bsController.getAppInit().get_extention_do()+"?");
				if(pos!=-1){
					first = uri.substring(0,pos+bsController.getAppInit().get_extention_do().length());
					second = uri.substring(pos+bsController.getAppInit().get_extention_do().length()+1);
				}else{
					first = uri;
					second = "";
				}
				if(first.indexOf("/actions/")==0) first=util_format.replace(first,"/actions/","/");
				if(first.indexOf("actions/")==0) first=util_format.replace(first,"actions/","/");
				if(first.indexOf("/")==0) first=first.substring(1,first.length());
				String id = first;
				id = util_format.replace(id,bsController.getAppInit().get_extention_do(),"");
				first=util_format.replace(first,id+bsController.getAppInit().get_extention_do(),"")+"/Controller";
				uri=first+"?"+bsController.CONST_ID_$ACTION+"="+id+"&"+second;	
			}
		}
		return uri;
	}
	
	public void set_uriError(String string) {
		_uriError = string;
	}
	public String get_uri() {
		return _uri;
	}
	public String get_uriError() {
		return _uriError;
	}
	public info_redirect get_inforedirect() {
		return _inforedirect;
	}
	public void set_inforedirect(info_redirect redirect) {
		_inforedirect = redirect;
	}
	public void set_uri(String string) {
		_uri = string;
	}
	
	public String toString(){
		return _uri;
	}

	public String get_transformationName() {
		return _transformationName;
	}

	public void set_transformationName(String transformationName) {
		_transformationName = transformationName;
	}

}
