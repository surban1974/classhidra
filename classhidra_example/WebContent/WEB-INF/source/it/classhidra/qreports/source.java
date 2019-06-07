package it.classhidra.qreports;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.serialize.Serialized;


@Serialized
public class source extends elementBase{
	private static final long serialVersionUID = 1L;
	
	@Serialized
	private List<source_item> list;
	
	@Serialized(false)
	private List<source_item> static_list;
	
	@Serialized(false)
	private String sql;
	
	@Serialized(false)
	private String sql_transformed;
	

	public source(){
		super();
		init();
	}

	public void init(){
		sql="";
		sql_transformed="";
		list=new ArrayList<source_item>();

	}

	public void load4view(Statement st, parameter current, HashMap<String, parameter> h_parameters){
		load4view(st, current, h_parameters, null);
	}
	
	public void load4view(Statement st, parameter current, HashMap<String, parameter> h_parameters, String lang){
		if(list==null) 
			list=new ArrayList<source_item>();
		if(static_list==null){
			static_list=new ArrayList<source_item>();
			static_list.addAll(list);
		}
		
		if(!sql.equals("")){
			list.clear();
			list.addAll(static_list);
			ResultSet rs = null;
			try {	    	   
	    	   	sql_transformed=sqlTransformer.getTransformed(sql, current, h_parameters, lang);
	            rs = st.executeQuery(sql_transformed);
	            if (rs != null) {
	            	while (rs.next()) {
	                	source_item si = new source_item();
	                	si.setValue(rs.getString(1));
	                	try{
	                		si.setDescription(rs.getString(2));
	                	}catch(Exception e){
	                		si.setDescription("");
	                	}
	                	list.add(si);
	            	}
	                rs.close();
	            }
	        } catch (Exception e) {
	        	new bsException(e, iStub.log_ERROR);
	        } finally {
	        	try {
	        		if(rs!=null)
	        			rs.close();
	        	}catch (Exception e) {
				}
	        }
		}

		for(source_item si:list){
			if(si.getLanguagetranslationtable()!=null){
				si.getLanguagetranslationtable().load4view(st,h_parameters, lang);
				if(si.getLanguagetranslationtable().getDescription_view()!=null && !si.getLanguagetranslationtable().getDescription_view().equals(""))
					si.setDescription_view(si.getLanguagetranslationtable().getDescription_view());
				else {
					try{
						si.setDescription_view(sqlTransformer.getTransformed(si.getDescription_view(), null, h_parameters, lang));
					}catch(Exception e){	
						new bsException(e, iStub.log_ERROR);
					}					
				}
			}else {
				try{
					si.setDescription_view(sqlTransformer.getTransformed(si.getDescription_view(), null, h_parameters, lang));
				}catch(Exception e){	
					new bsException(e, iStub.log_ERROR);
				}
			}

		}

	}

	public String loadParameter(Statement st, parameter current, HashMap<String, parameter> h_parameters){
		return loadParameter(st, current, h_parameters, null);
	}
	
	public String loadParameter(Statement st, parameter current, HashMap<String, parameter> h_parameters, String lang){
		String result="";
		if(!sql.equals("")){
			ResultSet rs = null;
			try {
	            String sql_loc=sqlTransformer.getTransformed(sql, current, h_parameters, lang);
	            rs = st.executeQuery(sql_loc);
	            if (rs != null) {
	            	while (rs.next()) 
	            		result=rs.getString(1);
	            	
	                rs.close();
	            }
	        } catch (Exception e) {
	        	new bsException(e, iStub.log_ERROR);
	        } finally {
	        	try {
	        		if(rs!=null)
	        			rs.close();
	        	}catch (Exception e) {
				}
	        }
		}
		return result;

	}


	public List<source_item> getList() {
		return list;
	}
	public void setList(List<source_item> list) {
		this.list = list;
	}

	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTagname(){
		return "source";
	}

	public String getSql_transformed() {
		return sql_transformed;
	}

	public List<source_item> getStatic_list() {
		return static_list;
	}
}
