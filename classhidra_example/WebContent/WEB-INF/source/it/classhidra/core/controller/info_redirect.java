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

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.v2.Util_sort;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class info_redirect extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -1L;
	private String path;
	private String auth_id;
	private String error;
	private HashMap<String,info_section> _sections;
	private HashMap<String,info_transformation> _transformationoutput;
	private String descr;
	private String mess_id;
	private String united_id;
	private String img;
	private String navigated;
	private String contentType;
	private String contentName;
	private String contentEncoding;
	private String flushBuffer;
	private String transformationName;
	private String avoidPermissionCheck;

	private Vector<info_section> v_info_sections;
	private Vector<info_transformation> v_info_transformationoutput;
	private boolean external=false;




	public info_redirect(){
		super();
		reimposta();
	}

	public void init(info_redirect iRedirect) throws bsControllerException{
		if(iRedirect==null) return;
		if((this.auth_id==null || this.auth_id.equals("")) && (iRedirect.getAuth_id()!=null && !iRedirect.getAuth_id().equals("")))
			this.auth_id = iRedirect.getAuth_id();
		if((this.error==null || this.error.equals("")) && (iRedirect.getError()!=null && !iRedirect.getError().equals("")))
			this.error = iRedirect.getError();
		if((this.descr==null || this.descr.equals("")) && (iRedirect.getDescr()!=null && !iRedirect.getDescr().equals("")))
			this.descr = iRedirect.getDescr();
		if((this.contentType==null || this.contentType.equals("")) && (iRedirect.getContentType()!=null && !iRedirect.getContentType().equals("")))
			this.contentType = iRedirect.getContentType();
		if((this.contentEncoding==null || this.contentEncoding.equals("")) && (iRedirect.getContentEncoding()!=null && !iRedirect.getContentEncoding().equals("")))
			this.contentEncoding = iRedirect.getContentEncoding();
		if((this.contentName==null || this.contentName.equals("")) && (iRedirect.getContentName()!=null && !iRedirect.getContentName().equals("")))
			this.contentName = iRedirect.getContentName();
		if((this.flushBuffer==null || this.flushBuffer.equals("")) && (iRedirect.getFlushBuffer()!=null && !iRedirect.getFlushBuffer().equals("")))
			this.flushBuffer = iRedirect.getFlushBuffer();
		
		if((this.transformationName==null || this.transformationName.equals("")) && (iRedirect.getTransformationName()!=null && !iRedirect.getTransformationName().equals("")))
			this.transformationName = iRedirect.getTransformationName();
		
		if((this.avoidPermissionCheck==null || this.avoidPermissionCheck.equals("")) && (iRedirect.getAvoidPermissionCheck()!=null && !iRedirect.getAvoidPermissionCheck().equals("")))
			this.avoidPermissionCheck = iRedirect.getAvoidPermissionCheck();
		
		if((this.mess_id==null || this.mess_id.equals("")) && (iRedirect.getMess_id()!=null && !iRedirect.getMess_id().equals("")))
			this.mess_id = iRedirect.getMess_id();
		if((this.united_id==null || this.united_id.equals("")) && (iRedirect.getUnited_id()!=null && !iRedirect.getUnited_id().equals("")))
			this.united_id = iRedirect.getUnited_id();
		if((this.img==null || this.img.equals("")) && (iRedirect.getImg()!=null && !iRedirect.getImg().equals("")))
			this.img = iRedirect.getImg();


		if(iRedirect.get_sections()!=null){
			if(this._sections==null) this._sections = iRedirect.get_sections();
			else{
				Object[] keys = iRedirect.get_sections().keySet().toArray();
				for (int i = 0; i < keys.length; i++){
					String key = (String)keys[i];
					if(_sections.get(key)==null) _sections.put(key,iRedirect.get_sections().get(key));
				}
			}
		}
	}

	public void init(Node node) throws bsControllerException{
		if(node==null) return;
		try{
			NamedNodeMap nnm = node.getAttributes();
			if (nnm!=null){
				for (int i=0;i<node.getAttributes().getLength();i++){
					String paramName = node.getAttributes().item(i).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					String sNodeValue = node_nnm.getNodeValue();
					if(sNodeValue!=null){
						sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}

				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
		NodeList nodeList = node.getChildNodes();
		int order_s=0;
		int order_t=0;
		for(int i=0;i<nodeList.getLength();i++){
			if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
				if(nodeList.item(i).getNodeName().toLowerCase().equals("section")){
					info_section iSection = new info_section();
					iSection.init(nodeList.item(i));
					order_s++;
					iSection.setOrder(Integer.valueOf(order_s).toString());
					if(iSection!=null) _sections.put(iSection.getName(),iSection);
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("transformationoutput")){
					info_transformation iTransformationoutput = new info_transformation();
					iTransformationoutput.init(nodeList.item(i));
					order_t++;
					iTransformationoutput.setOrder(Integer.valueOf(order_t).toString());
					if(iTransformationoutput!=null) _transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
				}

			}
		}
		v_info_transformationoutput.addAll(new Vector<info_transformation>(_transformationoutput.values()));
//		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
		v_info_transformationoutput = Util_sort.sort(v_info_transformationoutput,"int_order");

		v_info_sections.addAll(new Vector<info_section>(_sections.values()));
//		v_info_sections = new util_sort().sort(v_info_sections,"int_order");
		v_info_sections = Util_sort.sort(v_info_sections,"int_order");

	}

	public void reimposta(){
		prefix="";
		path="";
		auth_id="";
		error="";
		descr="";
		contentType="";
		contentEncoding="";
		transformationName="";
		contentName="";
		mess_id="";
		united_id="";
		img="";
		navigated="true";
		avoidPermissionCheck="false";
		_sections=new HashMap<String,info_section>();
		_transformationoutput=new HashMap<String,info_transformation>();
		v_info_transformationoutput=new Vector<info_transformation>();
		v_info_sections=new Vector<info_section>();
	}


	public i_transformation transformationFactory(String transformationName){
		return load_actions.transformationFactory(transformationName,_transformationoutput, null);
	}
	public i_transformation transformationFactory(String transformationName, ServletContext servletContext){
		return load_actions.transformationFactory(transformationName,_transformationoutput, servletContext);
	}

	public void setAllowedSection(boolean value){
		if(_sections==null) return;
		Vector<String> keys_sections= new Vector<String>(_sections.keySet());
		for(int i=0;i<keys_sections.size();i++){
			info_section sec = (info_section)_sections.get(keys_sections.get(i));
			if(sec!=null) sec.setAllowed(value);
		}
	}
	
	public HashMap<String,info_section> get_sections() {
		return _sections;
	}
	public String getPath() {
		return path;
	}
	public info_redirect setPath(String string) {
		path = string;
		return this;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public info_redirect setAuth_id(String string) {
		auth_id = string;
		return this;
	}
	public String getError() {
		return error;
	}
	public info_redirect setError(String string) {
		error = string;
		return this;
	}
	public String getDescr() {
		return descr;
	}
	public String getMess_id() {
		return mess_id;
	}
	public info_redirect setDescr(String string) {
		descr = string;
		return this;
	}
	public info_redirect setMess_id(String string) {
		mess_id = string;
		return this;
	}
	public String getUnited_id() {
		return united_id;
	}
	public info_redirect setUnited_id(String string) {
		united_id = string;
		return this;
	}
	public String getImg() {
		return img;
	}

	public info_redirect setImg(String img) {
		this.img = img;
		return this;
	}

	public String getNavigated() {
		return navigated;
	}

	public info_redirect setNavigated(String navigated) {
		this.navigated = navigated;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public info_redirect setContentType(String contenttype) {
		this.contentType = contenttype;
		return this;
	}
	
	public String getContentEncoding() {
		return contentEncoding;
	}

	public info_redirect setContentEncoding(String contentencoding) {
		this.contentEncoding = contentencoding;
		return this;
	}
	
	public String getContentName() {
		return contentName;
	}

	public info_redirect setContentName(String contentName) {
		this.contentName = contentName;
		return this;
	}	
	
	public String getFlushBuffer() {
		return flushBuffer;
	}

	public info_redirect setFlushBuffer(String flushBuffer) {
		this.flushBuffer = flushBuffer;
		return this;
	}
	
	public String toString(){
		return toXml();
	}
	
	public String getTransformationName() {
		return transformationName;
	}

	public info_redirect setTransformationName(String transformationName) {
		this.transformationName = transformationName;
		return this;
	}
	
	public String toXml(){
		return toXml("");
	}
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <"+prefix+"redirect";
		if(auth_id!=null && !auth_id.trim().equals("")) result+=" auth_id=\""+util_format.normaliseXMLText(auth_id)+"\"";
		if(path!=null && !path.trim().equals("")) result+=" path=\""+util_format.normaliseXMLText(path)+"\"";
		if(contentType!=null && !contentType.trim().equals("")) result+=" contentType=\""+util_format.normaliseXMLText(contentType)+"\"";
		if(contentEncoding!=null && !contentEncoding.trim().equals("")) result+=" contentEncoding=\""+util_format.normaliseXMLText(contentEncoding)+"\"";
		if(contentName!=null && !contentName.trim().equals("")) result+=" contentName=\""+util_format.normaliseXMLText(contentName)+"\"";
		if(flushBuffer!=null && !flushBuffer.trim().equals("")) result+=" flushBuffer=\""+util_format.normaliseXMLText(flushBuffer)+"\"";
		if(transformationName!=null && !transformationName.trim().equals("")) result+=" transformationName=\""+util_format.normaliseXMLText(transformationName)+"\"";
		if(navigated!=null && !navigated.trim().equals("") && !navigated.trim().equals("true")) result+=" navigated=\""+util_format.normaliseXMLText(navigated)+"\"";
		if(descr!=null && !descr.trim().equals("")) result+=" descr=\""+util_format.normaliseXMLText(descr)+"\"";
		if(mess_id!=null && !mess_id.trim().equals("")) result+=" mess_id=\""+util_format.normaliseXMLText(mess_id)+"\"";
		if(avoidPermissionCheck!=null && !avoidPermissionCheck.trim().equals("") && !avoidPermissionCheck.trim().equalsIgnoreCase("false")) result+=" avoidPermissionCheck=\""+util_format.normaliseXMLText(avoidPermissionCheck)+"\"";
		
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			isEntity=true;
			for(int i=0;i<v_info_transformationoutput.size();i++){
				info_transformation iTransformation = (info_transformation)v_info_transformationoutput.get(i);
				if(iTransformation!=null) result+=iTransformation.toXml();
			}
		}
		if(v_info_sections!=null && v_info_sections.size()>0){
			isEntity=true;
			for(int i=0;i<v_info_sections.size();i++){
				info_section iSection = (info_section)v_info_sections.get(i);
				if(iSection!=null) result+=iSection.toXml();
			}
		}

		if(isEntity)
			result+=System.getProperty("line.separator")+space+"      </"+prefix+"redirect>";
		else result+="</"+prefix+"redirect>";


		return result;
	}

	public void syncroWithRelations(HashMap<String,HashMap<String,String>> _elements){
		enabled = CONST_ENABLED;
		if(_elements==null) return;
		if(_elements.get("*")!=null){
			setEnableDisable(CONST_DISABLED);
			return;
		}
		HashMap<String,String> _sections = _elements.get(auth_id);
		if(_sections==null) return;
		if(_sections.get("*")!=null){
			setEnableDisable(CONST_DISABLED);
			return;
		}
		int dis=0;
		if(_sections.size()>0) enabled = CONST_DISABLED_CHILD;
		for(int i=0;i<v_info_sections.size();i++){
			info_section current = (info_section)v_info_sections.get(i);
			current.syncroWithRelations(_sections);
			if(current.getEnabled()==CONST_DISABLED) dis++;
		}
		if(dis==0) enabled = CONST_ENABLED;
		if(dis==v_info_sections.size())  enabled = CONST_DISABLED;
	}

	public info_redirect setEnableDisable(int value){
		enabled=value;
		for(int i=0;i<v_info_sections.size();i++){
			info_section current = (info_section)v_info_sections.get(i);
			current.setEnableDisable(value);
		}
		return this;
	}

	public void refreshEnableDisableLevel(){
		int count_ENABLED=0;
		int count_DISABLED=0;
//		int count_DISABLED_CHILD=0;
		for(int i=0;i<v_info_sections.size();i++){
			info_section current = (info_section)v_info_sections.get(i);
			if(current.getEnabled()==CONST_ENABLED) count_ENABLED++;
			if(current.getEnabled()==CONST_DISABLED) count_DISABLED++;
//			if(current.getEnabled()==CONST_DISABLED_CHILD) count_DISABLED_CHILD++;
		}
		if(v_info_sections.size()==count_ENABLED){
			enabled=CONST_ENABLED;
			return;
		}
		if(v_info_sections.size()==count_DISABLED){
			enabled=CONST_DISABLED;
			return;
		}
		enabled=CONST_DISABLED_CHILD;
	}

	public Vector<info_section> getV_info_sections() {
		return v_info_sections;
	}

	public info_redirect setV_info_sections(Vector<info_section> vInfoSections) {
		v_info_sections = vInfoSections;
		return this;
	}

	public Vector<info_transformation> getV_info_transformationoutput() {
		return v_info_transformationoutput;
	}

	public info_redirect setV_info_transformationoutput(Vector<info_transformation> vInfoTransformationoutput) {
		v_info_transformationoutput = vInfoTransformationoutput;
		return this;
	}

	public HashMap<String,info_transformation> get_transformationoutput() {
		return _transformationoutput;
	}
	
	public boolean isEmpty(){
		if(auth_id!=null && !auth_id.trim().equals("")) return false;
		if(path!=null && !path.trim().equals("")) return false;
		if(contentType!=null && !contentType.trim().equals("")) return false;
		if(contentEncoding!=null && !contentEncoding.trim().equals("")) return false;
		if(contentName!=null && !contentName.trim().equals("")) return false;
		if(flushBuffer!=null && !flushBuffer.trim().equals("")) return false;
		if(transformationName!=null && !transformationName.trim().equals("")) return false;
		if(navigated!=null && !navigated.trim().equals("") && !navigated.trim().equals("true")) return false;
		if(descr!=null && !descr.trim().equals("")) return false;
		if(mess_id!=null && !mess_id.trim().equals("")) return false;

		return true;
	}

	public String getAvoidPermissionCheck() {
		return avoidPermissionCheck;
	}

	public info_redirect setAvoidPermissionCheck(String avoidPermissionCheck) {
		this.avoidPermissionCheck = avoidPermissionCheck;
		return this;
	}

	public boolean isExternal() {
		return external;
	}

	public info_redirect setExternal(boolean external) {
		this.external = external;
		return this;
	}


}
