

package it.classhidra.scheduler.scheduling.db;
import java.util.*;

import it.classhidra.core.tool.elements.elementDBBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.init.batch_init;

public class db_batch extends elementDBBase implements i_elementDBBase, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
//	TODO @Deprecated
    public final static short STATE_NORMAL = 0;    
//	TODO @Deprecated
    public final static short STATE_SCHEDULED = -1;    
//	TODO @Deprecated
    public final static short STATE_INEXEC = 1;    
//	TODO @Deprecated
    public final static short STATE_SUSPEND = 10;
    
    private Integer cd_ist;
    private String cd_btch;
    private String cd_p_btch;
    private String cls_btch;
    private String dsc_btch;
    private Integer ord;
    private String period;
    private java.sql.Timestamp tm_next;
    private Integer state;
    private Integer initialState;
    private Integer st_exec;
    
    private batch_init b_init;

    
    private java.sql.Timestamp tm_last;
public db_batch() {
    super();
    reimposta();
}
public void reimposta() {
    super.reimposta_super();
    cd_ist = new Integer(0);
    cd_btch = "";
    cd_p_btch = "";
    cls_btch = "";
    dsc_btch = "";
    ord = new Integer(0);
    period = "";
    tm_next = new java.sql.Timestamp(new java.util.Date().getTime());
    state = new Integer(0);
    initialState = new Integer(0);
    st_exec = new Integer(0);
    try{
    	b_init = DriverScheduling.getConfiguration();
    }catch(Exception e){
    	b_init = new batch_init();
    }
}
public void reInit(i_elementDBBase _i_el) {
    db_batch el = (db_batch)_i_el;
    this.setCd_ist(el.getCd_ist());
    this.setCd_btch(el.getCd_btch());
    this.setCd_p_btch(el.getCd_p_btch());
    this.setCls_btch(el.getCls_btch());
    this.setDsc_btch(el.getDsc_btch());
    this.setOrd(el.getOrd());
    this.setPeriod(el.getPeriod());
    this.setTm_next(el.getTm_next());
    this.setState(el.getState());
    this.setSt_exec(el.getSt_exec());
}
public void reInit(java.sql.ResultSet rs) {
	if(rs==null) return;
	try{
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		HashMap rsmdH = new HashMap();
		for(int i=1;i<=rsmd.getColumnCount();i++) rsmdH.put(rsmd.getColumnLabel(i).toLowerCase(),rsmd.getColumnLabel(i));
		if(rsmdH.get("cd_ist")!=null) this.setCd_ist(new Integer(rs.getInt("cd_ist")));
//		if(rsmdH.get("cd_ist")!=null) this.setOrd(new Integer(rs.getInt("cd_ist")));
		if(rsmdH.get("cd_btch")!=null) this.setCd_btch(new String(rs.getString("cd_btch")));
		if(rsmdH.get("cd_p_btch")!=null) this.setCd_p_btch(new String(rs.getString("cd_p_btch")));
		if(rsmdH.get("cls_btch")!=null) this.setCls_btch(new String(rs.getString("cls_btch")));
		if(rsmdH.get("dsc_btch")!=null) this.setDsc_btch(new String(rs.getString("dsc_btch")));
		if(rsmdH.get("ord")!=null) this.setOrd(new Integer(rs.getInt("ord")));
		if(rsmdH.get("period")!=null) this.setPeriod(new String(rs.getString("period")));
		if(rsmdH.get("tm_next")!=null) this.setTm_next(new java.sql.Timestamp(rs.getTimestamp("tm_next").getTime()));
		if(rsmdH.get("state")!=null) this.setState(new Integer(rs.getInt("state")));
		if(rsmdH.get("st_exec")!=null) this.setSt_exec(new Integer(rs.getInt("st_exec")));
		if(rsmdH.get("tm_last")!=null) this.setTm_last(new java.sql.Timestamp(rs.getTimestamp("tm_last").getTime()));
	}catch(Exception e){
	}
}
public String sql_Select() {
    String result="";
    result+="SELECT * FROM "+b_init.get_db_prefix()+"batch ";
    result+="WHERE ";
    result+="cd_btch='"+util_format.convertAp(this.getCd_btch())+"' \n";
    result+="and cd_ist='"+this.getCd_ist()+"' \n";

    return result;
}
public String sql_Delete() {
    String result="";
    result+="DELETE FROM "+b_init.get_db_prefix()+"batch ";
    result+="WHERE ";
    result+="cd_btch='"+util_format.convertAp(this.getCd_btch())+"' \n";
    result+="and cd_ist='"+this.getCd_ist()+"' \n";

    return result;
}
public String sql_Insert() {
    String result="";
    result+="INSERT INTO "+b_init.get_db_prefix()+"batch \n";
    result+="( \n";
    result+="cd_ist, \n";
    result+="cd_btch, \n";
    result+="cd_p_btch, \n";
    result+="cls_btch, \n";
    result+="dsc_btch, \n";
    result+="ord, \n";
    result+="period, \n";
    result+="tm_next, \n";
    result+="state, \n";
    result+="st_exec \n";
    result+=") \n";
    result+="VALUES \n";
    result+="( \n";
    if(fields.get("cd_ist")!=null) result+=fields.get("cd_ist") + ", \n";
    else result+=this.getCd_ist() + ", \n";
    if(fields.get("cd_btch")!=null) result+=fields.get("cd_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getCd_btch()) + "', \n";
    if(fields.get("cd_p_btch")!=null) result+=fields.get("cd_p_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getCd_p_btch()) + "', \n";
    if(fields.get("cls_btch")!=null) result+=fields.get("cls_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getCls_btch()) + "', \n";
    if(fields.get("dsc_btch")!=null) result+=fields.get("dsc_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getDsc_btch()) + "', \n";
    if(fields.get("ord")!=null) result+=fields.get("ord") + ", \n";
    else result+=this.getOrd() + ", \n";
    if(fields.get("period")!=null) result+=fields.get("period") + ", \n";
    else result+="'"+util_format.convertAp(this.getPeriod()) + "', \n";
    if(fields.get("tm_next")!=null) result+=fields.get("tm_next") + ", \n";
    else result+="'"+util_format.timestampToString(this.getTm_next()) + "', \n";
    if(fields.get("state")!=null) result+=fields.get("state") + ", \n";
    else result+=this.getState() + ", \n";
    if(fields.get("st_exec")!=null) result+=fields.get("st_exec") + ", \n";
    else result+=this.getSt_exec() + " \n";
    result+=") \n";
    return result;
}
public String sql_Update(i_elementDBBase _i_element_mod) {
    String result="";
    db_batch element_mod = (db_batch)_i_element_mod;
    result+="UPDATE "+b_init.get_db_prefix()+"batch \n";
    result+="SET \n";
    if(element_mod.getFields().get("cd_ist")!=null) result+=element_mod.getFields().get("cd_ist") + ", \n";
    else result+="cd_ist="+element_mod.getCd_ist()+", \n";
    if(element_mod.getFields().get("cd_btch")!=null) result+=element_mod.getFields().get("cd_btch") + ", \n";
    else result+="cd_btch='"+util_format.convertAp(element_mod.getCd_btch())+"', \n";
    if(element_mod.getFields().get("cd_p_btch")!=null) result+=element_mod.getFields().get("cd_p_btch") + ", \n";
    else result+="cd_p_btch='"+util_format.convertAp(element_mod.getCd_p_btch())+"', \n";
    if(element_mod.getFields().get("cls_btch")!=null) result+=element_mod.getFields().get("cls_btch") + ", \n";
    else result+="cls_btch='"+util_format.convertAp(element_mod.getCls_btch())+"', \n";
    if(element_mod.getFields().get("dsc_btch")!=null) result+=element_mod.getFields().get("dsc_btch") + ", \n";
    else result+="dsc_btch='"+util_format.convertAp(element_mod.getDsc_btch())+"', \n";
    if(element_mod.getFields().get("ord")!=null) result+=element_mod.getFields().get("ord") + ", \n";
    else result+="ord="+element_mod.getOrd()+", \n";
    if(element_mod.getFields().get("period")!=null) result+=element_mod.getFields().get("period") + ", \n";
    else result+="period='"+util_format.convertAp(element_mod.getPeriod())+"', \n";
    if(element_mod.getFields().get("tm_next")!=null) result+=element_mod.getFields().get("tm_next") + ", \n";
    else result+="tm_next='"+util_format.timestampToString(element_mod.getTm_next())+"', \n";
    if(element_mod.getFields().get("state")!=null) result+=element_mod.getFields().get("state") + ", \n";
    else result+="state="+element_mod.getState()+", \n";
    if(element_mod.getFields().get("st_exec")!=null) result+=element_mod.getFields().get("st_exec") + ", \n";
    else result+="st_exec="+element_mod.getSt_exec()+" \n";
    result+="WHERE ";
    result+="cd_ist="+this.getCd_ist()+" AND \n";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    return result;
}

public String sql_UpdateOnlyState(i_elementDBBase _i_element_mod) {
    String result="";
    db_batch element_mod = (db_batch)_i_element_mod;
    result+="UPDATE "+b_init.get_db_prefix()+"batch \n";
    result+="SET \n";
    result+="state="+element_mod.getState()+", \n";
    result+="tm_next=null,  \n";
    result+="st_exec=0 \n";
    result+="WHERE ";
    result+="cd_ist="+this.getCd_ist()+" AND \n";
    result+="cd_btch='"+this.getCd_btch()+"' \n";
    return result;
}
public boolean find(i_elementDBBase _i_el) {
    return true;
}
public boolean equals(i_elementDBBase _i_el) {
    db_batch el = (db_batch)_i_el;
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
public String getDsc_btch() {
    return dsc_btch;
}
public void setDsc_btch(String value) {
    if(value==null) dsc_btch=value;
    else{
        if(value.length()>100) dsc_btch = value.substring(0,100);
        else dsc_btch = value;
    }
}
public Integer getOrd() {
    return ord;
}
public void setOrd(Integer value) {
    this.ord=value;
}
public String getPeriod() {
    return period;
}
public void setPeriod(String value) {
    if(value==null) period=value;
    else{
        if(value.length()>50) period = value.substring(0,50);
        else period = value;
    }
}
public java.sql.Timestamp getTm_next() {
    return tm_next;
}
public void setTm_next(java.sql.Timestamp value) {
    this.tm_next=value;
}
public Integer getState() {
    return state;
}
public void setState(Integer value) {
    this.state=value;
}
public Integer getSt_exec() {
    return st_exec;
}
public void setSt_exec(Integer value) {
    this.st_exec=value;
}

public String getDesSt_exec() {
	if(st_exec.shortValue()== i_batch.STATE_KO) return "KO";
	if(st_exec.shortValue()== i_batch.STATE_OK) return "OK";
	if(st_exec.shortValue()== i_batch.STATE_WARNING) return "WARNING";
	return "WARNING";
}
public String getDesSt_exec(Integer _st_exec) {
	st_exec=_st_exec;
	if(st_exec.shortValue()== i_batch.STATE_KO) return "KO";
	if(st_exec.shortValue()== i_batch.STATE_OK) return "OK";
	if(st_exec.shortValue()== i_batch.STATE_WARNING) return "WARNING";
	return "WARNING";
}
public java.sql.Timestamp getTm_last() {
	return tm_last;
}
public void setTm_last(java.sql.Timestamp tmLast) {
	tm_last = tmLast;
}
public String getCd_p_btch() {
	return cd_p_btch;
}
public void setCd_p_btch(String cdPBtch) {
	cd_p_btch = cdPBtch;
}
public String getCls_btch() {
	return cls_btch;
}
public void setCls_btch(String clsBtch) {
	cls_btch = clsBtch;
}
public Integer getCd_ist() {
	return cd_ist;
}
public void setCd_ist(Integer cdIst) {
	cd_ist = cdIst;
}
public Integer getInitialState() {
	return initialState;
}
public void setInitialState(Integer initialState) {
	this.initialState = initialState;
}


}




