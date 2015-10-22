

package it.classhidra.scheduler.scheduling.db;
import java.util.*;

import it.classhidra.core.tool.elements.elementDBBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.servlets.servletBatchScheduling;
public class db_batch_log extends elementDBBase implements i_elementDBBase, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public final static short STATE_WARNING = 1;
    public final static short STATE_OK = 0;
    public final static short STATE_KO = 2;
    
    private String cd_btch;
    private java.sql.Timestamp tm_start;
    private java.sql.Timestamp tm_fin;
    private Integer st_exec;
    private String dsc_exec;
    
    private batch_init b_init;
    
    private boolean writeLog=true;
public db_batch_log() {
    super();
    reimposta();
}
public void reimposta() {
     super.reimposta_super();
    cd_btch = "";
    tm_start = new java.sql.Timestamp(new java.util.Date().getTime());
    tm_fin = new java.sql.Timestamp(new java.util.Date().getTime());
    st_exec = new Integer("0");
    dsc_exec = "";
    try{
    	b_init = servletBatchScheduling.getConfiguration();
    }catch(Exception e){
    	b_init = new batch_init();
    }}
public void reInit(i_elementDBBase _i_el) {
    db_batch_log el = (db_batch_log)_i_el;
    this.setCd_btch(el.getCd_btch());
    this.setTm_start(el.getTm_start());
    this.setTm_fin(el.getTm_fin());
    this.setSt_exec(el.getSt_exec());
    this.setDsc_exec(el.getDsc_exec());
}
public void reInit(java.sql.ResultSet rs) {
	if(rs==null) return;
	try{
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		HashMap rsmdH = new HashMap();
		for(int i=1;i<=rsmd.getColumnCount();i++) rsmdH.put(rsmd.getColumnLabel(i).toLowerCase(),rsmd.getColumnLabel(i));
		if(rsmdH.get("cd_btch")!=null) this.setCd_btch(new String(rs.getString("cd_btch")));
		if(rsmdH.get("tm_start")!=null) this.setTm_start(new java.sql.Timestamp(rs.getTimestamp("tm_start").getTime()));
		if(rsmdH.get("tm_fin")!=null) this.setTm_fin(new java.sql.Timestamp(rs.getTimestamp("tm_fin").getTime()));
		if(rsmdH.get("st_exec")!=null) this.setSt_exec(new Integer(rs.getInt("st_exec")));
		if(rsmdH.get("dsc_exec")!=null) this.setDsc_exec(new String(rs.getString("dsc_exec")));
	}catch(Exception e){
	}
}
public String sql_Select() {
    String result="";
    result+="SELECT * FROM "+b_init.get_db_prefix()+"batch_log ";
    result+="WHERE ";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    return result;
}
public String sql_Delete() {
    String result="";
    result+="DELETE FROM "+b_init.get_db_prefix()+"batch_log ";
    result+="WHERE ";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    return result;
}
public String sql_Insert() {
    String result="";
    result+="INSERT INTO "+b_init.get_db_prefix()+"batch_log \n";
    result+="( \n";
    result+="cd_btch, \n";
    result+="tm_start, \n";
    result+="tm_fin, \n";
    result+="st_exec, \n";
    result+="dsc_exec \n";
    result+=") \n";
    result+="VALUES \n";
    result+="( \n";
    if(fields.get("cd_btch")!=null) result+=fields.get("cd_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getCd_btch()) + "', \n";
    if(fields.get("tm_start")!=null) result+=fields.get("tm_start") + ", \n";
    else result+="'"+util_format.timestampToString(this.getTm_start()) + "', \n";
    if(fields.get("tm_fin")!=null) result+=fields.get("tm_fin") + ", \n";
    else result+="'"+util_format.timestampToString(this.getTm_fin()) + "', \n";
    if(fields.get("st_exec")!=null) result+=fields.get("st_exec") + ", \n";
    else result+=this.getSt_exec() + ", \n";
    if(fields.get("dsc_exec")!=null) result+=fields.get("dsc_exec") + ", \n";
    else result+="'"+util_format.convertAp(this.getDsc_exec()) + "' \n";
    result+=") \n";
    return result;
}
public String sql_Update(i_elementDBBase _i_element_mod) {
    String result="";
    db_batch_log element_mod = (db_batch_log)_i_element_mod;
    result+="UPDATE "+b_init.get_db_prefix()+"batch_log \n";
    result+="SET \n";
    if(element_mod.getFields().get("cd_btch")!=null) result+=element_mod.getFields().get("cd_btch") + ", \n";
    else result+="cd_btch='"+util_format.convertAp(element_mod.getCd_btch())+"', \n";
    if(element_mod.getFields().get("tm_start")!=null) result+=element_mod.getFields().get("tm_start") + ", \n";
    else result+="tm_start='"+util_format.timestampToString(element_mod.getTm_start())+"', \n";
    if(element_mod.getFields().get("tm_fin")!=null) result+=element_mod.getFields().get("tm_fin") + ", \n";
    else result+="tm_fin='"+util_format.timestampToString(element_mod.getTm_fin())+"', \n";
    if(element_mod.getFields().get("st_exec")!=null) result+=element_mod.getFields().get("st_exec") + ", \n";
    else result+="st_exec="+element_mod.getSt_exec()+", \n";
    if(element_mod.getFields().get("dsc_exec")!=null) result+=element_mod.getFields().get("dsc_exec") + ", \n";
    else result+="dsc_exec='"+util_format.convertAp(element_mod.getDsc_exec())+"' \n";
    result+="WHERE ";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    return result;
}
public boolean find(i_elementDBBase _i_el) {
    return true;
}
public boolean equals(i_elementDBBase _i_el) {
    db_batch_log el = (db_batch_log)_i_el;
    if( el==null) return false;
    if( !this.control() && !el.control()) return true;
    if(this.cd_btch.equals(el.getCd_btch())) return true;
    return false;
}
public boolean control() {
    if(this.cd_btch==null) return false;
    return true;
}
public String getCd_btch() {
    return cd_btch;
}
public void setCd_btch(String value) {
    if(value==null) cd_btch=value;
    else{
        if(value.length()>50) cd_btch = value.substring(0,50);
        else cd_btch = value;
    }
}
public java.sql.Timestamp getTm_start() {
    return tm_start;
}
public void setTm_start(java.sql.Timestamp value) {
    this.tm_start=value;
}
public java.sql.Timestamp getTm_fin() {
    return tm_fin;
}
public void setTm_fin(java.sql.Timestamp value) {
    this.tm_fin=value;
}
public Integer getSt_exec() {
    return st_exec;
}
public void setSt_exec(Integer value) {
    this.st_exec=value;
}
public String getDsc_exec() {
    return dsc_exec;
}
public String getDsc_exec_br() {
    if(dsc_exec==null)
    	return dsc_exec;
    else 
    	return dsc_exec.replace("\n", "<br>");
}
public void setDsc_exec(String value) {
    if(value==null) dsc_exec=value;
    else{
        if(value.length()>1000) dsc_exec = value.substring(0,1000);
        else dsc_exec = value;
    }
}
public boolean isWriteLog() {
	return writeLog;
}
public void setWriteLog(boolean writeLog) {
	this.writeLog = writeLog;
}
}




