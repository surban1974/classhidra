/**
* Creation date: (07/04/2006)
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

package it.classhidra.core.tool.elements;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.init.db_init;
import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_reflect;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public abstract class elementDBBase extends elementBase implements i_elementDBBase, i_elementBase,  java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected HashMap<String,Object> fields;
	protected HashMap<String,Object> hashDatas;

public boolean control(){
	return false;
}
public boolean equals(i_elementDBBase el){
	return false;
}
public boolean find(i_elementDBBase el){
	return false;
}
public abstract void reimposta();
public void reimposta_super(){
	super.reimposta();
	fields=new HashMap<String, Object>();
	hashDatas=new HashMap<String, Object>();
}

public void reInit(i_bean another_bean){
	try{
		Method[] methods = util_reflect.getMethods(this,"get");
		for(int i=0;i<methods.length;i++){
			String methodName = methods[i].getName().substring(3);
			try{
				Object[] par = new Object[1];
				par[0]=another_bean.get(methodName);
				util_reflect.setValue(this, "set"+util_reflect.adaptMethodName(methodName.trim()), par,false);
//				setCampoValue(methodName,another_bean.get(methodName));
			}catch(Exception e){
				e.toString();
			}
		}
	}catch(Exception ex){
		new bsException(ex,iStub.log_ERROR);
	}
}


public void reInit(java.sql.ResultSet rs){
	try{
		sql_getFromResultSet(rs,rs.getMetaData());
	}catch(Exception e){
		new bsException(e,iStub.log_ERROR);
	}
}
public boolean sql_getFromResultSet(java.sql.ResultSet rs){
	boolean result = true;
	if(rs==null) return false;
	try{
		java.lang.reflect.Method metodi[] = util_reflect.getMethods(this);
		if (metodi.length==0) return false;
		for(int i=0;i<metodi.length;i++){
			if(metodi[i].getName().indexOf("set")==0){
				String column_name = metodi[i].getName().substring(3,metodi[i].getName().length());
				boolean finish = false;
				Object prm[] = new Object[1];
				try{
					prm[0] = rs.getObject(column_name);
					metodi[i].invoke(this, prm);
					finish=true;
				}catch(Exception e){}
				if(!finish){
					if(!finish){
						try{
							prm[0] = rs.getTimestamp(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Short.valueOf(rs.getShort(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getBigDecimal(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Boolean.valueOf(rs.getBoolean(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Byte.valueOf(rs.getByte(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getDate(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Double.valueOf(rs.getDouble(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Integer.valueOf(rs.getInt(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = Long.valueOf(rs.getLong(column_name));
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getString(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getTime(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getBlob(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}
					if(!finish){
						try{
							prm[0] = rs.getClob(column_name);
							metodi[i].invoke(this, prm);
							finish=true;
						}catch(Exception e){}
					}

				}
			}
		}
	}catch(Exception e){
		return false;
	}
	return result;
}
public boolean sql_getFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd){
	return sql_getFromResultSet(rs, rsmd,true);
}
public boolean sql_getFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, boolean log){
	if(rs==null) return false;
	try{
		java.lang.reflect.Method[] mths = this.getClass().getMethods();

		for(int i=1;i<=rsmd.getColumnCount();i++){
			boolean isIn = false;
			int j=0;
			while(j<mths.length && !isIn){
				if(	mths[j].getName().equals("set"+rsmd.getColumnName(i)) ||
				mths[j].getName().toUpperCase().equals(("set"+rsmd.getColumnName(i)).toUpperCase())) isIn=true;
				else j++;
			}
			if(isIn){
				String class_name="";
				try{
					class_name = rsmd.getColumnClassName(i);
				}catch(Exception ex){
					try{
						Object obj = rs.getObject(rsmd.getColumnName(i));
						class_name = obj.getClass().getName();
					}catch(Exception exc){
					}
				}
				sql_getColumnFromResultSet(rs,rsmd,rsmd.getColumnName(i),class_name,log);
			}
		}
	}catch(Exception e){
		new bsException("sql_getFromResultSet  ERROR:"+e.toString(),null);
		return false;
	}
	return true;
}



public String get_real_column_method(String column_name){
	String set_column_method="";
	try{
		Class<?>[] cl = new Class[0];
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+util_reflect.adaptMethodName(column_name.toLowerCase()),cl)!=null)
				set_column_method = util_reflect.adaptMethodName(column_name.toLowerCase());
		}
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+util_reflect.adaptMethodName(column_name.toUpperCase()),cl)!=null)
				set_column_method = util_reflect.adaptMethodName(column_name.toUpperCase());
		}
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+util_reflect.adaptMethodName(column_name),cl)!=null)
				set_column_method = util_reflect.adaptMethodName(column_name);
		}
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+column_name,cl)!=null)
				set_column_method = column_name;
		}
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+column_name.toUpperCase(),cl)!=null)
				set_column_method = column_name.toUpperCase();
		}
		if(set_column_method.equals("")){
			if(this.getClass().getMethod("get"+column_name.toLowerCase(),cl)!=null)
				set_column_method = column_name.toLowerCase();
		}
	}catch(Exception e){
	}
	return set_column_method;
}
public boolean sql_getColumnFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, String column_name, String column_class_name){
	return sql_getColumnFromResultSet(rs, rsmd, column_name, column_class_name, true);
}

public boolean sql_getColumnFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, String column_name, String column_class_name, boolean log){
	try{


		String column_method=get_real_column_method(column_name);
		if(column_method.equals("")) column_method = util_reflect.adaptMethodName(column_name);

			try{
				Object[] par = new Object[1];
				par[0] = rs.getObject(column_name);
				if(util_reflect.setValue(this,"set"+column_method,par,log))
					return true;

			}catch(Exception ex){

			}


			try{
				this.setCampoValue_light("set"+column_method, rs.getObject(column_name));
				return true;
			}catch(Exception e){}

				if(	column_class_name.equals("java.sql.Timestamp") ||
					column_class_name.equals("java.sql.Date") ||
					column_class_name.equals("java.sql.Time") ||
					column_class_name.equals("java.lang.String")){
					try{
						this.setCampoValue_light("set"+column_method, rs.getDate(column_name));
						return true;
					}catch(Exception e){
						try{
							java.util.Date objd = new java.util.Date(rs.getDate(column_name).getTime());
							this.setCampoValue_light("set"+column_method,objd);
							return true;
						}catch(Exception ex){
						}
					}
					try{
						this.setCampoValue_light("set"+column_method, rs.getTimestamp(column_name));
						return true;
					}catch(Exception e){
						e.toString();
					}
					try{
						this.setCampoValue_light("set"+column_method, rs.getTime(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, rs.getString(column_name));
						return true;
					}catch(Exception e){}
				}

				if(	column_class_name.equals("java.lang.Short") ||
					column_class_name.equals("java.math.BigDecimal") ||
					column_class_name.equals("java.lang.Double") ||
					column_class_name.equals("java.lang.Integer") ||
					column_class_name.equals("java.lang.Long") ||
					column_class_name.equals("java.lang.Byte") ||
					column_class_name.equals("java.lang.Boolean")
					){
					try{
						this.setCampoValue_light("set"+column_method, Integer.valueOf(rs.getInt(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, Short.valueOf(rs.getShort(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, rs.getBigDecimal(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, Double.valueOf(rs.getDouble(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, Long.valueOf(rs.getLong(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, Boolean.valueOf(rs.getBoolean(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light("set"+column_method, Byte.valueOf(rs.getByte(column_name)));
						return true;
					}catch(Exception e){}
				}
				if(column_class_name.equals("java.sql.Blob")){
					try{
						this.setCampoValue_light("set"+column_method, rs.getBlob(column_name));
						return true;
					}catch(Exception e){}
				}
				if(column_class_name.equals("java.sql.Clob")){
					try{
						this.setCampoValue_light("set"+column_method, rs.getClob(column_name));
						return true;
					}catch(Exception e){}
				}
	}catch(Exception e){
		return false;
	}
	return true;
}

private boolean sql_getColumnFromResultSet_0(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, String column_name, String column_class_name,String prefisso){
	try{
		try{
			this.setCampoValue_light("set"+util_reflect.adaptMethodName(replace(column_name, prefisso,"")), rs.getObject(column_name));
			return true;
		}catch(Exception e){
			try{
				this.setCampoValue_light("set"+replace(column_name, prefisso,""), rs.getObject(column_name));
				return true;
			}catch(Exception ex){
			}
		}
		String realName = "set"+replace(column_name, prefisso,"");

				if(	column_class_name.equals("java.sql.Timestamp") ||
					column_class_name.equals("java.sql.Date") ||
					column_class_name.equals("java.sql.Time") ||
					column_class_name.equals("java.lang.String")){
					try{
						this.setCampoValue_light(realName, rs.getTimestamp(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, rs.getDate(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, rs.getTime(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, rs.getString(column_name));
						return true;
					}catch(Exception e){}
				}

				if(	column_class_name.equals("java.lang.Short") ||
					column_class_name.equals("java.math.BigDecimal") ||
					column_class_name.equals("java.lang.Double") ||
					column_class_name.equals("java.lang.Integer") ||
					column_class_name.equals("java.lang.Long") ||
					column_class_name.equals("java.lang.Byte") ||
					column_class_name.equals("java.lang.Boolean")
					){
					try{
						this.setCampoValue_light(realName, Integer.valueOf(rs.getInt(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, Short.valueOf(rs.getShort(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, rs.getBigDecimal(column_name));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, Double.valueOf(rs.getDouble(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, Long.valueOf(rs.getLong(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, Boolean.valueOf(rs.getBoolean(column_name)));
						return true;
					}catch(Exception e){}
					try{
						this.setCampoValue_light(realName, Byte.valueOf(rs.getByte(column_name)));
						return true;
					}catch(Exception e){}
				}
				if(column_class_name.equals("java.sql.Blob")){
					try{
						this.setCampoValue_light(realName, rs.getBlob(column_name));
						return true;
					}catch(Exception e){}
				}
				if(column_class_name.equals("java.sql.Clob")){
					try{
						this.setCampoValue_light(realName, rs.getClob(column_name));
						return true;
					}catch(Exception e){}
				}
	}catch(Exception e){
		return false;
	}
	return true;
}

public boolean sql_getFromResultSet(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd, String prefisso){
	if(rs==null) return false;
	try{
		java.lang.reflect.Method[] mths = this.getClass().getMethods();

		for(int i=1;i<=rsmd.getColumnCount();i++){
			if(!prefisso.equals("") && rsmd.getColumnName(i).indexOf(prefisso)==0){
				boolean isIn = false;
				int j=0;
				while(j<mths.length && !isIn){
					String realName = replace(rsmd.getColumnName(i),prefisso,"");
					if(mths[j].getName().equals("set"+realName)) isIn=true;
					else j++;
				}
				if(isIn) sql_getColumnFromResultSet_0(rs,rsmd,rsmd.getColumnName(i),rsmd.getColumnClassName(i),prefisso);
			}

		}
	}catch(Exception e){
		return false;
	}
	return true;
}
private static String replace (String target, String from, String to) {
	int start = target.indexOf (from);
  	if (start==-1) return target;
  	int lf = from.length();
  	char [] targetChars = target.toCharArray();
  	StringBuffer buffer = new StringBuffer();
  	int copyFrom=0;
  	while (start != -1) {
    	buffer.append (targetChars, copyFrom, start-copyFrom);
    	buffer.append (to);
    	copyFrom=start+lf;
    	start = target.indexOf (from, copyFrom);
    }
  	buffer.append (targetChars, copyFrom, targetChars.length-copyFrom);
  	return buffer.toString();
}


public i_elementDBBase load_use_itself() throws Exception{
	Connection conn=null;
	Statement st=null;
	ResultSet rs=null;
	try{
		db_init init = new db_init();
		init.init();
		conn = new db_connection().getContent(init);
		conn.setAutoCommit(false);
		st = conn.createStatement();
		rs = st.executeQuery(this.sql_Select());
		if(rs.next()){
			reInit(rs);
		}else{
			 return null;
		}
		rs.close();

	}catch(Exception e){
		throw e;
	}finally{
		db_connection.release(rs, st, conn);
	}
	return this;
 }

public i_elementDBBase load_use_sql(String sql) throws Exception{
	Connection conn=null;
	Statement st=null;
	ResultSet rs=null;
	try{
		db_init init = new db_init();
		init.init();
		conn = new db_connection().getContent(init);
		conn.setAutoCommit(false);
		st = conn.createStatement();
		rs = st.executeQuery(sql);
		if(rs.next()){
			reInit(rs);
		}else{
			return null;
		}
		rs.close();

	}catch(Exception e){
		throw e;
	}finally{
		db_connection.release(rs, st, conn);
	}
	return this;
 }
public HashMap<String,Object> sql2HashMap(java.sql.ResultSet rs, java.sql.ResultSetMetaData rsmd) throws Exception{
	if(hashDatas==null) hashDatas = new HashMap<String, Object>();
	for(int i=1;i<=rsmd.getColumnCount();i++){
		String column_name=rsmd.getColumnName(i);
		hashDatas.put(column_name.toUpperCase(), rs.getObject(column_name));
	}
	return hashDatas;
}
public HashMap<String,Object> getHashDatas() {
	return hashDatas;
}
public void setHashDatas(HashMap<String,Object> hashDatas) {
	this.hashDatas = hashDatas;
}
public HashMap<String, Object> getFields() {
	return fields;
}
public void setFields(HashMap<String, Object> fields) {
	this.fields = fields;
}

public String sql_Select(){
	return "";
}
public String sql_Select(String alias){
	return "";
}
public String sql_Delete(){
	return "";
}
public String sql_Delete(String alias){
	return "";
}
public String sql_Insert(){
	return "";
}
public String sql_Insert(String alias){
	return "";
}
public String sql_Update(i_elementDBBase element_mod){
	return "";
}
public String sql_Update(i_elementDBBase element_mod,String alias){
	return "";
}

public PreparedStatement sql_Select(PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Delete(PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Insert(PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Update(i_elementDBBase element_mod,PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Select(String alias,PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Delete(String alias,PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Insert(String alias,PreparedStatement pst){
	return pst;
}

public PreparedStatement sql_Update(i_elementDBBase element_mod,String alias,PreparedStatement pst){
	return pst;
}


}
