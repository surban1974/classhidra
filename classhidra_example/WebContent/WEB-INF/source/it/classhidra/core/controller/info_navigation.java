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


public class info_navigation extends elementBase implements i_elementBase{
	private static final long serialVersionUID = 1L;
	private String id;	
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

	public void decodeMessage(HttpServletRequest request){
		if(this.iRedirect!=null && (desc_second==null || desc_second.equals("")))
			desc_second = (bsController.writeLabel(request,this.getIRedirect().getMess_id(),this.getIRedirect().getDescr(),null));
		
		if(this.child!=null) child.decodeMessage(request);
	}
	
	public void init(info_action iAction, info_redirect iRedirect, info_service iService, i_bean content) throws bsControllerException{
		if(iAction==null) throw new bsControllerException("ERROR load iAction",iStub.log_ERROR);
		this.iAction = iAction;
		this.iRedirect = iRedirect;
		this.id = iAction.getPath().trim();
		this._content = content;
		this.iService = iService;
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
			child = iNavigation.getChild();
			if(!(_content!=null && iNavigation.get_content()==null))
				_content = iNavigation.get_content();
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

}
