package it.classhidra.core.tool.util; 


import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;

public class util_container_servlet extends HttpServlet   {
	private static final long serialVersionUID = 1959097983289925130L;
	private static ConcurrentHashMap local_container;

	public void init() throws ServletException, UnavailableException {
		local_container = new ConcurrentHashMap();
	}
	public static Object getFromLocalContainer(String key) {		
		return local_container.get(key);
	}
	
	public static void setToLocalContainer(String key, Object value) {		
		if(local_container!=null) 
			local_container.put(key,value);
	}
	
	public static Vector getContainerKeys() {		
		if(local_container!=null) 
			return new Vector(local_container.keySet());
		else return new Vector();
	}	
	
	public static Vector getContainerElements() {		
		if(local_container!=null) 
			return new Vector(local_container.entrySet());
		else return new Vector();
	}	
	
	public static void removeFromLocalContainer(String key) {		
		if(local_container!=null) 
			local_container.remove(key);
	}
	public static ConcurrentHashMap getLocal_container() {
		return local_container;
	}
}
