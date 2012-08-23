package it.classhidra.core.tool.db.pool; 

import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.db_init;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;

public class db_pool_servlet extends HttpServlet   {
	private static final long serialVersionUID = 1L;
	private static iConnectionPool pool;

	public void init() throws ServletException, UnavailableException {
		db_init init = new db_init();
		init.init();
		try{
			pool = (iConnectionPool)Class.forName(init.get_local_pool_provider()).newInstance();
			pool.init(init);
		}catch(Exception e){
			if(bsController.getLogInit().get_Write2Concole().toLowerCase().equals("true")) 
				System.out.println("DB_POOL_SERVLET:EXCEPTION:"+e.toString());
		}catch(Throwable t){
			if(bsController.getLogInit().get_Write2Concole().toLowerCase().equals("true")) 
				System.out.println("DB_POOL_SERVLET:THROWABLE:"+t.toString());
		}
	}
	
	public static Connection getConnection() throws Exception {
		Connection conn = pool.getConnection();
		return conn;
	}
	
	public static String getPoolInfo(){
		String res = "POOL";
		if(pool!=null){
			res+="["+pool.getConnections().size()+"] ";
			for(int i=0;i<pool.getConnections().size();i++){
				iConnectionForPool current = (iConnectionForPool)pool.getConnections().get(i);
				try{
					res+="[#"+i+" "+((current.inUse())?"t":"f")+","+((current.isClosed())?"t":"f")+"]";
				}catch (Exception e) {
				}
			}
		}
		return res;
	}
}
