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

package it.classhidra.core.tool.db;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.db_init;
import it.classhidra.core.tool.db.pool.db_pool_container;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_container;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;

import javax.sql.DataSource;



public class db_connection implements Serializable{ 

	private static final long serialVersionUID = 1L;

	public static String CONST_CONNECTION_LOCAL_CONTAINER="CONST_CONNECTION_LOCAL_CONTAINER";

//	private Connection conn;
	private db_init init;

public db_connection() {
	super();	
	init = new db_init();
	init.init();
}



public Connection getContent() throws Exception{
	
	Connection conn = null;
	if(init.get_allwayone().equals("true"))
		conn = (Connection)util_container.getContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER);
	
	try{	
		if(conn==null || conn.isClosed()){
			conn = getFreeConnection(init); 
			if(init.get_allwayone().equals("true")){
				util_container.setContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER, conn);
				util_format.writeToConsole(bsController.getLogInit(),"APP:GETCONNECTION:"+util_format.dataToString(new Date(), "yyyy-MM-dd:HHmm:ssssss")+":" +conn.hashCode());	
			}
		}	
	}catch(Exception e){
		throw new bsException(e, iStub.log_ERROR);
	}catch(Throwable e){
		throw new bsException(e, iStub.log_ERROR);
	}
	
	return conn;
}

public Connection getContent(boolean writeException) throws Exception{
	
	Connection conn = null;
	if(init.get_allwayone().equals("true"))
		conn = (Connection)util_container.getContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER);
	
	try{	
		if(conn==null || conn.isClosed()){
			conn = getFreeConnection(init); 
			if(init.get_allwayone().equals("true")){
				util_container.setContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER, conn);
				util_format.writeToConsole(bsController.getLogInit(),"APP:GETCONNECTION:"+util_format.dataToString(new Date(), "yyyy-MM-dd:HHmm:ssssss")+":" +conn.hashCode());	
			}
		}	
	}catch(Exception e){
		if(writeException)
			throw new bsException(e, iStub.log_ERROR);
		else 
			util_format.writeToConsole(null,"GetDBFreeConnection:"+e.toString());
	}catch(Throwable e){
		if(writeException)
			throw new bsException(e, iStub.log_ERROR);
		else 
			util_format.writeToConsole(null,"GetDBFreeConnection:"+e.toString());
	}
	
	return conn;
}

public Connection getContentNoLog(){

	Connection conn = null;
	if(init.get_allwayone().equals("true"))
		conn = (Connection)util_container.getContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER);

	try{	
		if(conn==null || conn.isClosed()){
			conn = getFreeConnection(init); 
			if(init.get_allwayone().equals("true")){
				util_container.setContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER, conn);
				util_format.writeToConsole(bsController.getLogInit(),"APP:GETCONNECTION:"+util_format.dataToString(new Date(), "yyyy-MM-dd:HHmm:ssssss")+":" +conn.hashCode());	
			}
		}	
	}catch(Exception e){

	}catch(Throwable e){

	}
		
	return conn;
}

public Connection getContent(db_init init_ext) throws Exception{
	Connection conn = null;
	if(init_ext.get_allwayone().equals("true"))
		conn = (Connection)util_container.getContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER);

	try{	
		if(conn==null || conn.isClosed()){
			conn = getFreeConnection(init_ext); 
			if(init_ext.get_allwayone().equals("true")){
				util_container.setContentAsObject(CONST_CONNECTION_LOCAL_CONTAINER, conn);
				util_format.writeToConsole(bsController.getLogInit(),"APP:GETCONNECTION:"+util_format.dataToString(new Date(), "yyyy-MM-dd:HHmm:ssssss")+":" +conn.hashCode());	
			}
		}	
	}catch(Exception e){
		throw new bsException(e, iStub.log_ERROR);
	}catch(Throwable e){
		throw new bsException(e, iStub.log_ERROR);
	}

	return conn;
}
	
public static void release_dbrs(db_ResultSet rs, Statement st, Connection conn){
	db_init init = new db_init();
	init.init();

	try{
		if(rs!=null) rs.close();
	}catch(Exception e){		
	}
	try{
		if(st!=null) st.close();
	}catch(Exception e){		
	}
	try{
		if(conn!=null && !conn.isClosed()){
			if(!init.get_allwayone().equals("true")) conn.close();
		}
	}catch(Exception e){		
	}
}

public static void release_rs(ResultSet rs, Statement st, Connection conn){
	db_init init = new db_init();
	init.init();

	try{
		if(rs!=null) rs.close();
	}catch(Exception e){		
	}
	try{
		if(st!=null) st.close();
	}catch(Exception e){		
	}
	try{
		if(conn!=null && !conn.isClosed()){
			if(!init.get_allwayone().equals("true")) conn.close();
		}
	}catch(Exception e){		
	}
}

public static void release(Object rs, Statement st, Connection conn){
	if(rs==null) release(st, conn);
	if(rs instanceof ResultSet) release_rs((ResultSet)rs, st, conn);
	if(rs instanceof db_ResultSet) release_dbrs((db_ResultSet)rs, st, conn);
}

public static void release(Statement st, Connection conn){
	db_init init = new db_init();
	init.init();

	try{
		if(st!=null) st.close();
	}catch(Exception e){		
	}
	try{
		if(conn!=null && !conn.isClosed()){		
			if(!init.get_allwayone().equals("true")) conn.close();
		}
	}catch(Exception e){		
	}
}

public static void releaseAllweyOneConnection(){
	db_init init = new db_init();
	init.init();
	if(init.get_allwayone().equals("true")){
		try{
			Connection conn = (Connection)util_container.getContentAsObject(db_connection.CONST_CONNECTION_LOCAL_CONTAINER);
			util_container.setContentAsObject(db_connection.CONST_CONNECTION_LOCAL_CONTAINER,null);
			conn.close();
		}catch(Exception ex){					
		}catch (Throwable th) {
		}
	}

}
public void reimposta() {
//	conn=null;
}



protected  Connection getFreeConnection(db_init init) throws Exception,Throwable{
					
		if(init.get_connectiontype().equals(db_init.CT_DRIVERMANAGER)){
				Class.forName(init.get_driver());
				if(init.get_user().equals("") && init.get_password().equals("")) return java.sql.DriverManager.getConnection(init.get_url());
				else return java.sql.DriverManager.getConnection(init.get_url(),init.get_user(),init.get_password());
		}
		if(init.get_connectiontype().equals(db_init.CT_JSQLDATASOURCE)){
				Hashtable parms = new Hashtable();
					parms.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, init.get_driver());
					parms.put(javax.naming.Context.PROVIDER_URL, init.get_server());
				javax.naming.InitialContext Ctx = new javax.naming.InitialContext(parms);
				if(init.get_user().equals("") && init.get_password().equals("")) ((DataSource)Ctx.lookup(init.get_datasource())).getConnection();
				else return ((DataSource)Ctx.lookup(init.get_datasource())).getConnection(init.get_user(),init.get_password());
		}
		if(init.get_connectiontype().equals(db_init.CT_POOLDATASOURCE4)){
				Object db2DirectConnectionsPool = Class.forName("it.iss.dm0.dbtool.db2DirectConnectionsPool").newInstance();
				Object[] par = new Object[1];
					par[0] = init;
				Connection con = (Connection)util_reflect.getValue(db2DirectConnectionsPool,"get_pooldatasourcev4", par);	
				return con; 
		}
		
		if(init.get_connectiontype().equals(db_init.CT_POOLDATASOURCE5)){
				Hashtable parms = new Hashtable();
					parms.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, init.get_driver());
				javax.naming.InitialContext Ctx = new javax.naming.InitialContext(parms);
				if(init.get_user()==null || init.get_password()==null || init.get_user().equals("") && init.get_password().equals("")) return ((DataSource)Ctx.lookup(init.get_datasource())).getConnection();
				else return ((DataSource)Ctx.lookup(init.get_datasource())).getConnection(init.get_user(),init.get_password());
		}

		if(init.get_connectiontype().equals(db_init.CT_POOLDATASOURCE51)){
				javax.naming.InitialContext Ctx = new javax.naming.InitialContext();
				if(init.get_user()==null || init.get_password()==null || init.get_user().equals("") && init.get_password().equals("")) return ((DataSource)Ctx.lookup(init.get_datasource())).getConnection();
				else return ((DataSource)Ctx.lookup(init.get_datasource())).getConnection(init.get_user(),init.get_password());
		}
		
		if(init.get_connectiontype().equals(db_init.CT_LOCALPOOL)){
			return db_pool_container.getConnection();
	}		

	
		return null;
	}
}
