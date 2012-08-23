package it.classhidra.framework.web.beans; 

import java.util.HashMap;

import it.classhidra.core.tool.elements.elementDBBase;
import it.classhidra.core.tool.elements.i_elementDBBase;



public class option_element extends elementDBBase implements i_elementDBBase{
	private static final long serialVersionUID = 5532967482926778963L;
	protected String code;
	protected String desc;
	protected boolean filtered;
	protected String filterCode;

	public option_element(){
		super();
		reimposta();
	}

	public option_element(String code, String desc){
		reimposta();
		this.code = code;
		this.desc = desc;
	}
	public option_element(String code, String desc, String filterCode){
			reimposta();
			this.code = code;
			this.desc = desc;
			this.filterCode = filterCode;
	}

	public void reInit(java.sql.ResultSet rs) {
		if(rs==null) return;
		try{
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			HashMap rsmdH = new HashMap();
			for(int i=1;i<=rsmd.getColumnCount();i++) rsmdH.put(rsmd.getColumnLabel(i).toLowerCase(),rsmd.getColumnLabel(i));
			if(rsmdH.get("code")!=null) this.setCode(rs.getString("code"));
			if(rsmdH.get("desc")!=null) this.setDesc(rs.getString("desc"));
			if(rsmdH.get("filtercode")!=null) this.setFiltercode(rs.getString("filterCode"));
		}catch(Exception e){
		}
	}	
	
	public void reimposta(){
		code="-1";
		desc="";
		filtered=false;
		filterCode="";
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	public String getDescAdapt() {
		final String possible=" \\1234567890'ì|!\"$%&/()=?^qwertyuiopè+asdfghjklòàùzxcvbnm,.-QWERTYUIOPé*§°çLKJHGFDSAZXCVBNM;:_<>@#ù][{}";
		String result="";
		for(int i=0;i<desc.length();i++){
			if(possible.indexOf(desc.charAt(i))>-1) result+=desc.charAt(i);
		}
		return result;
	}	
	public void setCode(String string) {
		code = string;
	}
	public void setDesc(String string) {
		desc = string;
	}
	public void setFiltered(boolean bool){
		filtered = bool;
	}
	public boolean isFiltered(){
		return filtered;
	}
	public void setFiltercode(String string){
		filterCode = string;
	}
	public String getFiltercode(){
		return filterCode;
	}

}
