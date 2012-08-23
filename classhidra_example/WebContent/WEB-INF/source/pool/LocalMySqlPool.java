package pool;

import it.classhidra.core.init.db_init;
import it.classhidra.core.tool.db.pool.iConnectionForPool;
import it.classhidra.core.tool.db.pool.iConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Vector;

class ConnectionReaper extends Thread {

    private iConnectionPool pool;
    private long delay=300000;

    ConnectionReaper(iConnectionPool pool,long _delay) {
        this.pool=pool;
        if(_delay!=-1) delay = _delay;
    }

    public void run() {
        while(true) {
           try {
              sleep(delay);
           } catch( InterruptedException e) { }
           pool.reapConnections();
        }
    }
}

public class LocalMySqlPool implements iConnectionPool {

   private Vector connections;
   private long timeout=60000;
   private ConnectionReaper reaper;
   private int poolsize=10;
   private db_init init;
   
   public LocalMySqlPool() {
	   super();
   }

   public LocalMySqlPool(db_init _init) {
	  super();
	  init(_init);
   }

   public void init(db_init _init) {
		  
		  
		  init = new db_init();
		  init.init();
		  
		  try{
			  timeout = new Long(init.get_local_pool_timeout()).longValue();
		  }catch(Exception e){		  
		  }
		  try{
			  poolsize = new Integer(init.get_local_pool_size()).intValue();
		  }catch(Exception e){		  
		  }
		  
		  
	      connections = new Vector(poolsize);
	      
	      long r_delay=-1;
		  try{
			  r_delay = new Long(init.get_local_pool_delay()).longValue();
		  }catch(Exception e){		  
		  }
	      
	      reaper = new ConnectionReaper(this,r_delay);
	      reaper.start();
	   }

   
public synchronized void reapConnections() {

      long stale = System.currentTimeMillis() - timeout;
      Enumeration connlist = connections.elements();
    
      while((connlist != null) && (connlist.hasMoreElements())) {
          LocalMySqlConnectionForPool conn = (LocalMySqlConnectionForPool)connlist.nextElement();

          if((conn.inUse()) && (stale >conn.getLastUse()) && 
                                            (!conn.validate())) {
 	      removeConnection(conn);
         }
      }
   }

 
public synchronized void closeConnections() {
        
      Enumeration connlist = connections.elements();

      while((connlist != null) && (connlist.hasMoreElements())) {
          iConnectionForPool conn = (iConnectionForPool)connlist.nextElement();
          removeConnection(conn);
      }
   }

   private synchronized void removeConnection(iConnectionForPool conn) {
       connections.removeElement(conn);
   }


 
public synchronized Connection getConnection() throws Exception {

       LocalMySqlConnectionForPool c;
       for(int i = 0; i < connections.size(); i++) {
           c = (LocalMySqlConnectionForPool)connections.elementAt(i);
           if (c.lease()) {
System.out.println("POOL:get #"+i);        	   
              return c;
           }
       }
       
       Connection conn = null;
       boolean getConnection=false;
       int cnt = 0;
       Exception forThrow = null;
       while(cnt<3 && !getConnection){
    	   try{
     		   Class.forName(init.get_driver()).newInstance();
    		   conn = DriverManager.getConnection(init.get_url(),init.get_user(), init.get_password());
    	   }catch(Exception e){  
System.out.println("POOL:Exception try #"+cnt +" :"+e.toString());     		   
    		   forThrow=e;
    	   }catch (Throwable t) {
System.out.println("POOL:Throwable try #"+cnt +" :"+t.toString());     		   
    		   forThrow = new Exception(t.toString());
    	   }
    	   if(conn!=null) getConnection=true;
    	   else{	
    		   try{    	   
    			   wait(100);
    		   }catch(Exception e){    			   
    		   }
    	   }
    	   cnt++;
       }
       if(conn==null) throw forThrow;
       c = new LocalMySqlConnectionForPool(conn, this);
       c.lease();
       connections.addElement(c);
System.out.println("POOL:add #"+(connections.size()-1));        
       return c;
  } 

   /* (non-Javadoc)
 * @see it.classhidra.core.tool.db.pool.iConnectionPool#returnConnection(it.classhidra.core.tool.db.pool.JDCConnection)
 */
public synchronized void returnConnection(Connection conn) {
      ((LocalMySqlConnectionForPool)conn).expireLease();
   }

/* (non-Javadoc)
 * @see it.classhidra.core.tool.db.pool.iConnectionPool#getConnections()
 */
public Vector getConnections() {
	return connections;
}
   

}
