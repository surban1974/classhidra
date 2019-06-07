package it.classhidra.qreports;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_reflect;


public class languagetranslationtable extends elementBase{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String section;

	private String description_view;


	public languagetranslationtable(){
		super();
		init();
	}
	public void init(){
		id="";
		section="";
		description_view = "";
	}
	
	public void load4view(Statement dbm, HashMap<String, parameter> h_parameters){
		load4view(dbm, h_parameters, null);
	}

	public void load4view(Statement dbm, HashMap<String, parameter> h_parameters, String lang){
		if(id.equals("") || section.equals("")) return;
		if(id!=null) {
			if(section!=null) {
				try {
					section = sqlTransformer.getTransformed(section, null, h_parameters, lang);
				}catch(Exception e) {	
					new bsException(e, iStub.log_ERROR);
				}
			}
			try {
				final Object loader = Class.forName(id).newInstance();
				description_view = (String)util_reflect.getValue(loader,"translate",new Object[] {section,dbm,h_parameters,lang});			
			}catch(Exception e) {
				new bsException(e, iStub.log_ERROR);
			}
		}else if(dbm!=null && section!=null) {
			try {
				section = sqlTransformer.getTransformed(section, null, h_parameters, lang);
			}catch(Exception e) {
				new bsException(e, iStub.log_ERROR);
			}
			ResultSet rs = null;
			try {
	            rs = dbm.executeQuery(section);
	            if (rs != null) {
	                if (rs.next())
	                	description_view = rs.getString("Desc");
	            }
	        } catch (Exception e) {
	        	new bsException(e, iStub.log_ERROR);
	        } finally {
	        	try {
	        	if(rs!=null)
	        		rs.close();
	        	}catch(Exception e) {
	        	}
	        }
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	public String getDescription_view() {
		return description_view;
	}
	public void setDescription_view(String descriptionView) {
		description_view = descriptionView;
	}
}
