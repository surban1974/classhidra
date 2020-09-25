
package it.classhidra.scheduler.scheduling.db;
import java.util.*;
import it.classhidra.core.tool.elements.elementDBBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.util.util_format;
public class db_batch_property extends elementDBBase implements i_elementDBBase, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Short cd_ist;
    private String cd_btch;
    private String cd_property;
    private String value;
public db_batch_property() {
    super();
    reimposta();
}
public void reimposta() {
     super.reimposta_super();
    cd_ist = new Short("0");
    cd_btch = "";
    cd_property = "";
    value = "";
}
public void reInit(i_elementDBBase _i_el) {
    db_batch_property el = (db_batch_property)_i_el;
    this.setCd_ist(el.getCd_ist());
    this.setCd_btch(el.getCd_btch());
    this.setCd_property(el.getCd_property());
    this.setValue(el.getValue());
}
public void reInit(java.sql.ResultSet rs) {
	if(rs==null) return;
	try{
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		Map<String,String> rsmdH = new HashMap<String, String>();
		for(int i=1;i<=rsmd.getColumnCount();i++) rsmdH.put(rsmd.getColumnLabel(i).toLowerCase(),rsmd.getColumnLabel(i));
		if(rsmdH.get("cd_ist")!=null) this.setCd_ist(new Short(rs.getShort("cd_ist")));
		if(rsmdH.get("cd_btch")!=null && rs.getString("cd_btch")!=null) this.setCd_btch(new String(rs.getString("cd_btch")));
		if(rsmdH.get("cd_property")!=null && rs.getString("cd_property")!=null) this.setCd_property(new String(rs.getString("cd_property")));
		if(rsmdH.get("value")!=null && rs.getString("value")!=null) this.setValue(new String(rs.getString("value")));
	}catch(Exception e){
	}
}
public String sql_Select() {
    String result="";
    result+="SELECT * FROM gvch_batch_property ";
    result+="WHERE ";
    result+="cd_ist="+this.getCd_ist()+" AND \n";
    result+="cd_btch='"+this.getCd_btch()+"' AND \n";
    result+="cd_property='"+this.getCd_property()+"' \n";
    return result;
}
public String sql_Delete() {
    String result="";
    result+="DELETE FROM gvch_batch_property ";
    result+="WHERE ";
    result+="cd_ist="+this.getCd_ist()+" AND \n";
    result+="cd_btch='"+this.getCd_btch()+"' AND \n";
    result+="cd_property='"+this.getCd_property()+"' \n";
    return result;
}
public String sql_Insert() {
    String result="";
    result+="INSERT INTO gvch_batch_property \n";
    result+="( \n";
    result+="cd_ist, \n";
    result+="cd_btch, \n";
    result+="cd_property, \n";
    result+="value \n";
    result+=") \n";
    result+="VALUES \n";
    result+="( \n";
    if(fields.get("cd_ist")!=null) result+=fields.get("cd_ist") + ", \n";
    else result+=this.getCd_ist() + ", \n";
    if(fields.get("cd_btch")!=null) result+=fields.get("cd_btch") + ", \n";
    else result+="'"+util_format.convertAp(this.getCd_btch()) + "', \n";
    if(fields.get("cd_property")!=null) result+=fields.get("cd_property") + ", \n";
    else result+="'"+util_format.convertAp(this.getCd_property()) + "', \n";
    if(fields.get("value")!=null) result+=fields.get("value") + " \n";
    else result+="'"+util_format.convertAp(this.getValue()) + "' \n";
    result+=") \n";
    return result;
}
public String sql_Update(i_elementDBBase _i_element_mod) {
    String result="";
    db_batch_property element_mod = (db_batch_property)_i_element_mod;
    result+="UPDATE gvch_batch_property \n";
    result+="SET \n";
    if(element_mod.getFields().get("cd_ist")!=null) result+="cd_ist="+element_mod.getFields().get("cd_ist") + ", \n";
    else result+="cd_ist="+element_mod.getCd_ist()+", \n";
    if(element_mod.getFields().get("cd_btch")!=null) result+="cd_btch="+element_mod.getFields().get("cd_btch") + ", \n";
    else result+="cd_btch='"+util_format.convertAp(element_mod.getCd_btch())+"', \n";
    if(element_mod.getFields().get("cd_property")!=null) result+="cd_property="+element_mod.getFields().get("cd_property") + ", \n";
    else result+="cd_property='"+util_format.convertAp(element_mod.getCd_property())+"', \n";
    if(element_mod.getFields().get("value")!=null) result+="value="+element_mod.getFields().get("value") + " \n";
    else result+="value='"+util_format.convertAp(element_mod.getValue())+"' \n";
    result+="WHERE ";
    result+="cd_ist="+this.getCd_ist()+" AND \n";
    result+="cd_btch='"+this.getCd_btch()+"' AND \n";
    result+="cd_property='"+this.getCd_property()+"' \n";
    return result;
}
public boolean find(i_elementDBBase _i_el) {
    return true;
}
public boolean equals(i_elementDBBase _i_el) {
    db_batch_property el = (db_batch_property)_i_el;
    if( el==null) return false;
    if( !this.control() && !el.control()) return true;
    if(this.cd_ist.shortValue()==el.getCd_ist().shortValue() && this.cd_btch.equals(el.getCd_btch()) && this.cd_property.equals(el.getCd_property())) return true;
    return false;
}
public boolean control() {
    if(this.cd_ist==null && this.cd_btch==null && this.cd_property==null) return false;
    return true;
}
public Short getCd_ist() {
    return cd_ist;
}
public void setCd_ist(Short value) {
    this.cd_ist=value;
}
public String getCd_btch() {
    return cd_btch;
}
public void setCd_btch(String value) {
    if(value==null) cd_btch=value;
    else{
        if(value.length()>45) cd_btch = value.substring(0,45);
        else cd_btch = value;
    }
}
public String getCd_property() {
    return cd_property;
}
public void setCd_property(String value) {
    if(value==null) cd_property=value;
    else{
        if(value.length()>45) cd_property = value.substring(0,45);
        else cd_property = value;
    }
}
public String getValue() {
    return value;
}
public void setValue(String input) {
    if(input==null) value=input;
    else{
        if(input.length()>1000) value = input.substring(0,1000);
        else value = input;
    }
}
}




