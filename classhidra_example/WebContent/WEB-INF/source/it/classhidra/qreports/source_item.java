package it.classhidra.qreports;

import java.sql.Statement;
import java.util.HashMap;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.serialize.Serialized;

@Serialized
public class source_item extends elementBase{
	private static final long serialVersionUID = 1L;
	
	@Serialized
	private String value;
	
	@Serialized(false)
	private String description;
	
	@Serialized(false)
	private languagetranslationtable languagetranslationtable;
	
	@Serialized
	private String description_view;
	
	public source_item(){
		super();
		init();
	}
	
	public void init(){
		value="";
		description="";
		languagetranslationtable = new languagetranslationtable();
		
		description_view = "";
	}
	
	public void load4view(Statement dbm, HashMap<String, parameter> h_parameters, String lang){
		description_view=description;
		if(	languagetranslationtable!=null){
			languagetranslationtable.load4view(dbm,h_parameters, lang);
			if(languagetranslationtable.getDescription_view()!=null && !languagetranslationtable.getDescription_view().equals(""))
				description_view=languagetranslationtable.getDescription_view();
		}
		
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		this.description_view = description;
	}


	public String getTagname(){
		return "option";
	}

	public languagetranslationtable getLanguagetranslationtable() {
		return languagetranslationtable;
	}

	public void setLanguagetranslationtable(
			languagetranslationtable languagetranslationtable) {
		this.languagetranslationtable = languagetranslationtable;
	}

	public String getDescription_view() {
		return description_view;
	}

	public void setDescription_view(String descriptionView) {
		description_view = descriptionView;
	}

}
