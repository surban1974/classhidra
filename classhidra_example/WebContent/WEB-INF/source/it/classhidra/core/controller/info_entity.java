/**
* Creation date: (10/09/2010)
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

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.util.util_format;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public abstract class info_entity extends elementBase implements i_elementBase{


	private static final long serialVersionUID = -1;

	public final static int CONST_ENABLED=1;
	public final static int CONST_DISABLED=-1;
	public final static int CONST_DISABLED_CHILD=0;

	protected String property;
	protected String provider;
	protected String order;
	protected String comment;
	protected int int_order=-1;
	protected String system;
	protected String prefix;
	protected String annotated;
	protected String lookup;
	protected int enabled=1;

	protected HashMap properties;
	protected HashMap access_allowed;
	protected HashMap access_forbidden;
	
	protected boolean annotationLoaded=false;
	protected info_entity parent;
	
	private Class[] mappedMethodParameterTypes;



	public info_entity(){
		super();
		provider="";
		properties=new HashMap();
		access_allowed=new HashMap();
		access_forbidden=new HashMap();
		property="";
		order="";
		comment="";
		int_order=-1;
		system="false";
		annotated="false";
		prefix="";
		lookup="";
		enabled=1;
	}


	public void reInit(info_entity entity){
		setProperty(entity.getProperty());
		provider = entity.getProvider();
		order = entity.getOrder();
		comment = entity.getComment();
		lookup = entity.getLookup();
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		if(property==null) return;
		this.property = property;
		properties=new HashMap();
		StringTokenizer st = new StringTokenizer(property,";");
		while(st.hasMoreTokens()){
			String currToken = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(currToken,":");
			String h_key = null;
			String h_value = null;
			if(st2.hasMoreTokens()) h_key=st2.nextToken();
			if(st2.hasMoreTokens()) h_value=st2.nextToken();
			if(h_key!=null && h_value!=null) properties.put(h_key.trim(),h_value);
		}
	}
	public String getProperty(String key) {
		return (properties.get(key)==null)?"":properties.get(key).toString();
	}
	public void addProperty(String key, Object value) {
		properties.put(key,value);
	}

	public void addRelation(info_relation iRelation){
		if(iRelation!=null && iRelation.getTargets()!=null && !iRelation.getTargets().equals("") && iRelation.getGroups()!=null && !iRelation.getGroups().equals("")){
			String key = iRelation.getTargets()+"|"+iRelation.getGroups();
			
			HashMap access = null;
			if(iRelation.getType().equalsIgnoreCase(info_relation.TYPE_ALLOWED))
				access = access_allowed;
			if(iRelation.getType().equalsIgnoreCase(info_relation.TYPE_FORBIDDEN))
				access = access_forbidden;	
			
			if(access!=null){
				info_relation iRelationExist = (info_relation)access.get(key);
				if(iRelationExist!=null){
					if(iRelation.get_elements()!=null){
						if(iRelationExist.get_elements()==null)
							iRelationExist.set_elements(new HashMap());
						iRelationExist.get_elements().putAll(iRelation.get_elements());
						iRelationExist.refreshV_elements();
					}
					if(iRelation.get_middleactions()!=null){
						if(iRelationExist.get_middleactions()==null)
							iRelationExist.set_middleactions(new HashMap());
						iRelationExist.get_middleactions().putAll(iRelation.get_middleactions());
						iRelationExist.refreshV_middleactions();
					}
				}else
					access.put(key, iRelation);
			}
		}
	}
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
		try{
			int_order = Integer.valueOf(order).intValue();
		}catch(Exception e){
		}
	}

	public int getInt_order() {
		return int_order;
	}

	public void setInt_order(int intOrder) {
		int_order = intOrder;
	}

	public String toString(){
		return toXml();
	}


	public String toXml(){
		String result="";
		if(provider!=null && !provider.trim().equals("")) result+=" provider=\""+util_format.normaliseXMLText(provider)+"\"";
		if(lookup!=null && !lookup.trim().equals("")) result+=" lookup=\""+util_format.normaliseXMLText(lookup)+"\"";
		if(this instanceof info_stream){
			if(order!=null && !order.trim().equals("")) result+=" order=\""+util_format.normaliseXMLText(order)+"\"";
		}
		String prop="";
		if(properties==null){
			prop=property;
		}else{
			Vector tmp = new Vector(properties.keySet());
			for(int i=0;i<tmp.size();i++){
				String key=(String)tmp.get(i);
				String value="";
				if(properties.get(key)==null) value="null";
				else value=properties.get(key).toString();
				prop+=key+":"+value+";";
			}
		}
		if(prop!=null && !prop.trim().equals("")) result+=" property=\""+util_format.normaliseXMLText(prop)+"\"";
		if(comment!=null && !comment.trim().equals("")) result+=" comment=\""+util_format.normaliseXMLText(comment)+"\"";

		return result;
	}


	public boolean convert2xml(){
		return false;
	}

	public boolean convert2json(){
		return false;
	}
	


	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}



	public String getSystem() {
		return system;
	}



	public void setSystem(String system) {
		this.system = system;
	}



	public String getPrefix() {
		return prefix;
	}



	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public int getEnabled() {
		return enabled;
	}


	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}


	public HashMap getProperties() {
		return properties;
	}


	public void setProperties(HashMap properties) {
		this.properties = properties;
	}


	public String getAnnotated() {
		return annotated;
	}


	public void setAnnotated(String annotated) {
		this.annotated = annotated;
	}


	public boolean isAnnotationLoaded() {
		return annotationLoaded;
	}


	public void setAnnotationLoaded(boolean annotationLoaded) {
		this.annotationLoaded = annotationLoaded;
	}


	public String getLookup() {
		return lookup;
	}


	public void setLookup(String lookup) {
		this.lookup = lookup;
	}


	public HashMap getAccess_allowed() {
		return access_allowed;
	}


	public void setAccess_allowed(HashMap access_allowed) {
		this.access_allowed = access_allowed;
	}


	public HashMap getAccess_forbidden() {
		return access_forbidden;
	}


	public void setAccess_forbidden(HashMap access_forbidden) {
		this.access_forbidden = access_forbidden;
	}


	public info_entity getParent() {
		return parent;
	}


	public void setParent(info_entity parent) {
		this.parent = parent;
	}


	public Class[] getMappedMethodParameterTypes() {
		return mappedMethodParameterTypes;
	}


	public void setMappedMethodParameterTypes(Class[] mappedMethodParameterTypes) {
		this.mappedMethodParameterTypes = mappedMethodParameterTypes;
	}





}
