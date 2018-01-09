package application.cmex;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CmexContainerOld<T extends Object> implements Serializable {
	private static final long serialVersionUID = 1L;
	public final static String CMEX_ID = 	"CMEX_ID";
	public final static String CMEX_QUEUE = "CMEX_QUEUE";
	public final static String CMEX_TOPIC = "CMEX_TOPIC";
	protected ConcurrentMap<String, Queue<T>> queue = new ConcurrentHashMap<>();
	protected ConcurrentMap<String, Queue<T>> topic = new ConcurrentHashMap<>();
	
	public Queue<T> subscribe(String address, String type, long time){
		if(type==null || address==null)
			return null;
		else if(type.equalsIgnoreCase(CMEX_QUEUE))
			return this.queue.putIfAbsent(address, new LinkedList<T>());
		if(type.equalsIgnoreCase(CMEX_TOPIC))
			return this.topic.putIfAbsent(address, new LinkedList<T>());
		else
			return null;
	}
	
	public Queue<T> unsubscribe(String address, String type){
		if(type==null || address==null)
			return null;
		else if(type.equalsIgnoreCase(CMEX_QUEUE))
			return this.queue.remove(address);
		if(type.equalsIgnoreCase(CMEX_TOPIC))
			return this.topic.remove(address);
		else
			return null;
	}	
	
	public boolean push(String address, String type, T message, long time){
		if(type==null || address==null || message==null)
			return false;
		Queue<T> list = null;
		if(type.equalsIgnoreCase(CMEX_QUEUE))
			list = this.queue.get(address);
		else if(type.equalsIgnoreCase(CMEX_TOPIC))
			list = this.topic.get(address);
		if(list==null)
			return false;
		synchronized (list) {
			boolean added = list.add(message);
			if(added)
				list.notifyAll();
			return added;
		}
	}
	
	public Queue<T> subscribeAndPush(String address, String type, T message, long time){
		if(type==null || address==null || message==null)
			return null;
		Queue<T> list = null;
		if(type.equalsIgnoreCase(CMEX_QUEUE))
			list = this.queue.putIfAbsent(address, new LinkedList<T>());
		else if(type.equalsIgnoreCase(CMEX_TOPIC))
			list = this.topic.putIfAbsent(address, new LinkedList<T>());
		if(list==null)
			return list;
		synchronized (list) {
			boolean added = list.add(message);
			if(added)
				list.notifyAll();
			return list;
		}
	}
	

}
