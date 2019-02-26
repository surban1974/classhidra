package it.classhidra.core.tool.db.pool; 

import java.sql.Connection;


public class db_pool_container {

	private static db_pool_servlet container;


	
	
	public db_pool_container(){
		super();		
	}
	
	public static Connection getConnection() throws Exception{
		Connection result=null;		
		try{
			if(container == null){
				container = new db_pool_servlet();
				container.init();
			}

			result = db_pool_servlet.getConnection();
			if(result!=null) return result;
		}catch (Exception e) {
			throw e;
		}catch (Throwable t) {
			throw new Exception(t.toString());
		}
		

		return null;

	}
	

}
