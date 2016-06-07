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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_cloner;


public class info_navigation extends elementBase implements i_elementBase{
	private static final long serialVersionUID = 1L;
	private String id;	
	private String class_name;
	private String desc_second;
	private info_action iAction;
	private info_redirect iRedirect;
	private info_navigation parent;
	private info_navigation child;
	private info_service iService;
	
	
	private i_bean _content;
	
	public info_navigation(){
		super();
		reimposta();
	}
	
	public info_navigation clone(){
		info_navigation result = new info_navigation();

		result.iAction = this.getIAction();
		result.iRedirect = this.getIRedirect();
		result.id = this.getId();
		if(this.get_realcontent()!=null && !this.get_realcontent().getInfo_context().isScoped())
				result._content = this.get_realcontent();
		else{
			try{
				result._content = (i_bean)util_cloner.clone(this.get_realcontent());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		result.iService = this.getIService();
		if(this.child!=null){
			result.child = this.child.clone();
			result.child.setParent(result);
		}

		
		return result;
	}

	public void decodeMessage(HttpServletRequest request){
		if(this.iRedirect!=null && (desc_second==null || desc_second.equals("")))
			desc_second = (bsController.writeLabel(request,this.getIRedirect().getMess_id(),this.getIRedirect().getDescr(),null));
		
		if(this.child!=null) child.decodeMessage(request);
	}
	
	public void reDecodeMessage(HttpServletRequest request){
		if(this.iRedirect!=null)
			desc_second = (bsController.writeLabel(request,this.getIRedirect().getMess_id(),this.getIRedirect().getDescr(),null));
		
		if(this.child!=null) child.reDecodeMessage(request);
	}
	
	
	public void init(info_action iAction, info_redirect iRedirect, info_service iService, i_bean content) throws bsControllerException{
		if(iAction==null) throw new bsControllerException("ERROR load iAction",iStub.log_ERROR);
		this.iAction = iAction;
		this.iRedirect = iRedirect;
		this.id = iAction.getPath().trim();
		if(content!=null){
//			info_context info = bsController.checkBeanContext(content.asBean());
			if(iAction!=null && iAction.getNavigatedMemoryContent()!=null && !iAction.getNavigatedMemoryContent().equals("")){
				if(iAction.getNavigatedMemoryContent().equalsIgnoreCase("true")){
					if(content.asBean().getInfo_context().isOnlyProxied()){
						if(this._content==null)
							this._content = content;
					}else
						this._content = content;
				}else if(!iAction.getNavigatedMemoryContent().equalsIgnoreCase("false")){
					if(!content.asBean().getInfo_context().isScoped()){
						if(content.asBean().getInfo_context().isOnlyProxied()){
							if(this._content==null)
								this._content = content;
						}else
							this._content = content;
					}
				}
			}
			else if(!content.getInfo_context().isScoped()){
				if(content.asBean().getInfo_context().isOnlyProxied()){
					if(this._content==null)
						this._content = content;
				}else
					this._content = content;
			}
			
//			if(content.isNavigable())
//				this._content = content;
			if(content instanceof i_bean)
				class_name = content.asBean().getClass().getName();
			else if(content instanceof i_action)
				class_name = content.asBean().getClass().getName();					
		}
			
		
		this.iService = iService;
	}
	
	public void reInit(info_navigation second) throws bsControllerException{
		if(second==null) return;
		this.iAction = second.getIAction();
		this.iRedirect = second.getIRedirect();
		this.id = second.getId();
		if(second.get_realcontent()!=null && !second.get_realcontent().getInfo_context().isScoped()){
//			info_context info = bsController.checkBeanContext(second.get_realcontent().asBean());
			if(second.get_realcontent().asBean().getInfo_context().isOnlyProxied()){
				if(this._content==null)
					this._content = second.get_realcontent();
			}else
				this._content = second.get_realcontent();
		}
		this.iService = second.getIService();
		this.parent = second.getParent();
		this.child = second.getChild();
	}	
	
	public void reInitClone(info_navigation clone) throws Exception{
		if(clone==null) return;
		info_navigation second = (info_navigation)util_cloner.clone(clone);
		this.iAction = second.getIAction();
		this.iRedirect = second.getIRedirect();
		this.id = second.getId();
		if(second.get_realcontent()!=null && !second.get_realcontent().getInfo_context().isScoped()){
//			info_context info = bsController.checkBeanContext(second.get_realcontent().asBean());
			if(second.get_realcontent().asBean().getInfo_context().isOnlyProxied()){
				if(this._content==null)
					this._content = second.get_realcontent();
			}else
				this._content = second.get_realcontent();
		}
		this.iService = second.getIService();
		this.parent = second.getParent();
		this.child = second.getChild();
	}		
	
	public info_navigation get(String _id){
		if(this.id.equals(_id)) return this;
		else{
			if(child!=null) return child.get(_id);
			return null;
		}
	}

	public info_navigation getPrevIRedirect(){
		info_navigation last = lastIRedirect();
		if(last==null || last.getParent()==null) return null;
		info_navigation current = last.getParent();
		if(current.iRedirect!=null) return current;
		while(current.getParent()!=null){
			current = current.getParent();
			if(current.iRedirect!=null) return current;
		}
		return null;
	}	
	public info_navigation getPrev(){
		return last(1);
	}	

	public info_navigation find(String _id){
		if(this.id.equals(_id)) return this;
		else{
			if(child!=null) return child.find(_id);
			return null;
		}
	}
	
	public int findLevel(String _id, int level){
		if(this.id.equals(_id)) return level;
		else{
			if(child!=null) return child.findLevel(_id,level+1);
			return -1;
		}
	}

	public info_navigation lastIRedirect(){
		if(this.child==null){
			if(this.iRedirect!=null) return this;
			info_navigation current = this;
			while(current.getParent()!=null){
				current = current.getParent();
				if(current.iRedirect!=null) return current;
			}
			return null;
		}
		else{
			return child.last();
		}
	}	
	
	public info_navigation last(){
		if(this.child==null) return this;
		else{
			return child.last();
		}
	}
	

	public info_navigation last(int prevStep){
		info_navigation lin = this.last();
		if(lin!=null){
			if(prevStep<0) prevStep=(-1)*prevStep;
			int i=0;
			while(i<prevStep){
				if(lin!=null)lin=lin.getParent();
				i++;
			}
		}
		return lin;
	}	
	
	public void add(info_navigation iNavigation){
		

			
		if(iNavigation==null) return;
		if(id.equals(iNavigation.getId())){
			this.reimposta(iNavigation);
			return;
		}
		if(child==null){
			iNavigation.setParent(this);
			child = iNavigation;
			if(	!child.getIService().isComplete() && iService.isComplete()) child.setIService(iService);
/*
 			if(	!child.getIService().isComplete() &&
			 	iService.isComplete() &&
			 	(
					child.getIService().getId_pointOfService()==null ||
					child.getIService().getId_pointOfService().equals("") ||
					child.getIService().getId_pointOfService().equals(iService.getId_pointOfService())
				)	
				) child.setIService(iService);
 
 */				
		}else child.add(iNavigation);	
	}
	
	public void reimposta(info_navigation iNavigation){
		if(iNavigation==null) return;
/*		
		if(	this.iRedirect==null &&
			iNavigation!=null &&
			iNavigation.getIRedirect()!=null &&
			!iNavigation.getIRedirect().getNavigated().toLowerCase().equals("true")
		){
			if(this.getParent()!=null){
				this.getParent().child = null;
			}
		}else{
*/			
			id=iNavigation.getId();
			desc_second=iNavigation.getDesc_second();
			iAction = iNavigation.getIAction();
			if(	iNavigation.getIRedirect()!=null &&
				!iNavigation.getIRedirect().getMess_id().equals("") &&
				!iNavigation.getIRedirect().getDescr().equals("") &&
				iNavigation.getIRedirect().getNavigated().toLowerCase().equals("true"))
				iRedirect = iNavigation.getIRedirect();
			else if(iNavigation.getIRedirect()!=null && this.iRedirect==null)
				iRedirect = iNavigation.getIRedirect();
			child = iNavigation.getChild();
//			if(!(_content!=null && iNavigation.get_content()==null))
//				_content = iNavigation.get_content();
			if(!(_content!=null && iNavigation.get_realcontent()==null)){
				if(iNavigation.get_realcontent()!=null){
//					info_context info = bsController.checkBeanContext(iNavigation.get_realcontent().asBean());
					if(iAction!=null && iAction.getNavigatedMemoryContent()!=null && !iAction.getNavigatedMemoryContent().equals("")){
						if(iAction.getNavigatedMemoryContent().equalsIgnoreCase("true")){
							if(iNavigation.get_realcontent().asBean().getInfo_context().isOnlyProxied()){
								if(_content==null)
									_content = iNavigation.get_realcontent();
							}else
								_content = iNavigation.get_realcontent();
						}else if(!iAction.getNavigatedMemoryContent().equalsIgnoreCase("false")){
							if(!iNavigation.get_realcontent().getInfo_context().isScoped()){
								if(iNavigation.get_realcontent().asBean().getInfo_context().isOnlyProxied()){
									if(_content==null)
										_content = iNavigation.get_realcontent();
								}else
									_content = iNavigation.get_realcontent();
							}
						}
					}
					else if(!iNavigation.get_realcontent().getInfo_context().isScoped()){
						if(iNavigation.get_realcontent().asBean().getInfo_context().isOnlyProxied()){
							if(_content==null)
								_content = iNavigation.get_realcontent();
						}else
							_content = iNavigation.get_realcontent();
					}
					
					if(iNavigation.get_realcontent() instanceof i_bean)
						class_name = iNavigation.get_realcontent().asBean().getClass().getName();
					else if(iNavigation.get_realcontent() instanceof i_action)
						class_name = iNavigation.get_realcontent().asBean().getClass().getName();					
				}
				
			}
			if(iNavigation.getClass_name()!=null) class_name = iNavigation.getClass_name();
			if(!iService.isComplete()){
				if(!iService.isComplete() && iNavigation.getIService().isComplete()) iService = iNavigation.getIService();
				else{
					if(parent!=null) iService = parent.getIService(); 
				}
			}	
//		}
	}

	public void reimposta(){
		id="";
		desc_second="";
		iAction = new info_action();
		iRedirect = new info_redirect();
		iService = new info_service();
		child = null;

	}

	public Vector getAllChildReversIRedirect(){
		Vector result = new Vector();
		info_navigation parent = this;
		while(parent!=null){
			if(parent.iRedirect!=null){				
				result.add(parent);
			}
			parent = parent.getParent();
		}
		return result;
	}	
	
	public Vector getAllChildRevers(){
		Vector result = new Vector();
		info_navigation parent = this;
		while(parent!=null){
			result.add(parent);
			parent = parent.getParent();
		}
		return result;
	}

	public Vector getAllChildIRedirect(){
		Vector result = getAllChildReversIRedirect();
		Vector rev = new Vector(result.size());
		for(int i=result.size()-1;i>=0;i--) rev.add(result.get(i));
		return rev;
	}	
	
	public Vector getAllChild(){
		Vector result = getAllChildRevers();
		Vector rev = new Vector(result.size());
		for(int i=result.size()-1;i>=0;i--) rev.add(result.get(i));
		return rev;
	}
	
	public info_redirect getLastChildRedirect(){
		info_redirect res = new info_redirect();
		if(getChild()!=null) res = getChild().getIRedirect();
		else{
			if(this.iRedirect==null) res = new info_redirect();
			else res = iRedirect;
		}
		return res;
	}
	
	
	public info_navigation getChild() {
		return child;
	}
	public info_action getIAction() {
		return iAction;
	}
	public String getId() {
		return id;
	}
	public info_navigation getParent() {
		return parent;
	}
	public void setParent(info_navigation navigation) {
		parent = navigation;
	}
	public String getDesc_second() {
		return desc_second;
	}
	public void setDesc_second(String string) {
		desc_second = string;
	}
	public i_bean get_content() {
/*
		if(_content == null && class_name!=null && !class_name.equals("")){
			Object instance = util_provider.getBeanFromObjectFactory(new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider()}, id, class_name, null);
			if(instance!=null){
				if(instance instanceof i_bean)
					return ((i_bean)instance).asBean();
				else if(instance instanceof i_action)
					return ((i_action)instance).asBean();
				return null;
			}
		}
*/	
/*		
		if(_content == null && class_name!=null && !class_name.equals("")){
			Object instance = util_provider.getBeanFromObjectFactory(new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider()}, id, class_name, null);
			if(instance!=null){
				if(instance instanceof i_bean)
					return ((i_bean)instance);

				return null;
			}
		}
*/		
		return _content;
	}
	public i_bean get_realcontent() {
		return _content;
	}	
	public String get_content_xml() {
		if(_content!=null){
			try{
				return util_beanMessageFactory.bean2message(_content);
			}catch(Exception e){
				return e.toString();
			}
		}
		return "null";
	}
	
	public info_redirect getIRedirect() {
		return iRedirect;
	}
	public void setIRedirect(info_redirect info_redirect) {
		iRedirect = info_redirect;
	}

	public info_service getIService() {
		return iService;
	}

	public void setIService(info_service info_service) {
		iService = info_service;
	}

	public void setChild(info_navigation child) {
		this.child = child;
	}

	public void set_content(i_bean content) {
		_content = content;
	}

	public String getClass_name() {
		return class_name;
	}

}
