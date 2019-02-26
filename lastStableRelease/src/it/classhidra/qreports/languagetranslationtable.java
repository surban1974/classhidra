package it.classhidra.qreports;


import java.sql.Statement;
import java.util.HashMap;

import it.classhidra.core.tool.elements.elementBase;


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
		if(id.equals("") || section.equals("")) return;
//	       try {
//	            String sql = "select desc from LanguageTranslationTable ";
//	            
//	            if (rs != null) {
//	                if (rs.next()) {
//	                	description_view = DataFormat.getString(rs.getString("Desc"));
//	                 }
//	            }
//	        } catch (Exception e) {
//
//	        } finally {
//	        }
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
