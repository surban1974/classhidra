

package it.classhidra.scheduler.scheduling.db;
import java.util.*;


import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.util.util_format;


public class db_batch_log2 extends db_batch_log implements i_elementDBBase, java.io.Serializable, i_batch_log {
    private static final long serialVersionUID = 1L;

public db_batch_log2() {
    super();
    reimposta();
}

public void reInit(java.sql.ResultSet rs) {
	if(rs==null) return;
	try{
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		HashMap rsmdH = new HashMap();
		for(int i=1;i<=rsmd.getColumnCount();i++) rsmdH.put(rsmd.getColumnLabel(i).toLowerCase(),rsmd.getColumnLabel(i));
		if(rsmdH.get("cd_ist")!=null) this.setCd_ist(new Integer(rs.getInt("cd_ist")));
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
    result+="and cd_ist='"+this.getCd_ist()+"' \n";
    return result;
}
public String sql_Delete() {
    String result="";
    result+="DELETE FROM "+b_init.get_db_prefix()+"batch_log ";
    result+="WHERE ";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    result+="and cd_ist='"+this.getCd_ist()+"' \n";
    return result;
}
public String sql_Insert() {
    String result="";
    result+="INSERT INTO "+b_init.get_db_prefix()+"batch_log \n";
    result+="( \n";
    result+="cd_ist, \n";
    result+="cd_btch, \n";
    result+="tm_start, \n";
    result+="tm_fin, \n";
    result+="st_exec, \n";
    result+="dsc_exec \n";
    result+=") \n";
    result+="VALUES \n";
    result+="( \n";
    if(fields.get("cd_ist")!=null) result+=fields.get("cd_ist") + ", \n";
    else result+=this.getCd_ist() + ", \n";
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
    i_batch_log element_mod = (i_batch_log)_i_element_mod;
    result+="UPDATE "+b_init.get_db_prefix()+"batch_log \n";
    result+="SET \n";
    if(element_mod.getFields().get("cd_ist")!=null) result+=element_mod.getFields().get("cd_ist") + ", \n";
    else result+="cd_ist="+element_mod.getCd_ist()+", \n";   
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

public boolean equals(i_elementDBBase _i_el) {
    i_batch_log el = (i_batch_log)_i_el;
    if( el==null) return false;
    if( !this.control() && !el.control()) return true;
    if(this.cd_btch.equals(el.getCd_btch())) return true;
    return false;
}
public boolean control() {
    if(this.cd_btch==null) return false;
    return true;
}

}




