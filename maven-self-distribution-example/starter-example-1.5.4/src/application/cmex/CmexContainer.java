package application.cmex;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_usersInSession;

public class CmexContainer<T extends Object> implements Serializable {
	private static final long serialVersionUID = 1L;
	public final static String CMEX_ID = 	"CMEX_ID";
	public final static String CMEX_QUEUE = "CMEX_QUEUE";
	public final static String CMEX_TOPIC = "CMEX_TOPIC";
	protected String type = null;
	protected final ConcurrentMap<String, Queue<T>> list = new ConcurrentHashMap<>();
	protected final DelayQueue<ExpiringKey<String>> keyQueue = new DelayQueue<ExpiringKey<String>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K extends Object> CmexContainer<K> topic(HttpSession session) throws Exception{
		CmexContainer<K> cmex = (CmexContainer<K>)session.getAttribute(CmexContainer.CMEX_ID);
		if(cmex==null){
			cmex = new CmexContainer<>(CMEX_TOPIC);
			session.setAttribute(CmexContainer.CMEX_ID,cmex);
		}
		HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(util_usersInSession.CONST_APP_USER_CONTAINER);
		if(h_user_container==null || (h_user_container!=null && h_user_container.get(session.getId())==null)){
			if(h_user_container==null){
				h_user_container = new HashMap();
				bsController.setToLocalContainer(util_usersInSession.CONST_APP_USER_CONTAINER,h_user_container);
			}
			HttpSession session_instance = (HttpSession)h_user_container.get(session.getId());
			if(session_instance==null)
				h_user_container.put(session.getId(), session);
			
		}
		return cmex;
	}
	
	@SuppressWarnings({ "unchecked"})
	public static <K extends Object> CmexContainer<K> queue() throws Exception{
		CmexContainer<K> cmex = (CmexContainer<K>)bsController.getLocalContainer().get(CmexContainer.CMEX_ID);
		if(cmex==null){
			cmex = new CmexContainer<>(CMEX_QUEUE);
			bsController.getLocalContainer().put(CmexContainer.CMEX_ID, cmex);
		}
		return cmex;
	}
	
	public CmexContainer(String type){
		super();
		if(type!=null)
			this.type = type; 
		
		new Thread() {			
			@Override
			public void run() {				
				try{
					while(true){
						synchronized (keyQueue) {
							ExpiringKey<String> ek = null;
							while((ek=keyQueue.poll())==null){
								keyQueue.wait();
							}
							Queue<T> queue = list.get(ek.getKey());
							if(queue!=null){
								synchronized (queue) {
									queue.add(null);
									queue.notifyAll();
									queue.clear();
								}
								list.remove(ek.getKey());
							}
							
						}
					}
				}catch(Exception e){	
					new bsException(e, iStub.log_ERROR);
				}
				
			}
		}.start();

	}
	
	public Queue<T> subscribe(String address, long maxLifeTimeMillis){
		if(address==null || address.equals(""))
			return null;
		else{ 
			if(maxLifeTimeMillis>0){
				Queue<T> queue = this.list.get(address);
				if(queue==null){
					queue = new LinkedList<T>();
					this.list.put(address, queue);
					synchronized (keyQueue) {
						keyQueue.add(new ExpiringKey<String>(address, maxLifeTimeMillis));
						keyQueue.notifyAll();
					}
				}
				return queue;
			}else
				return
					this.list.putIfAbsent(address, new LinkedList<T>());
		}
	}
	
	public T waitForMessage(String address, long maxLifeTimeMillis){
		Queue<T> list = this.subscribe(address, maxLifeTimeMillis);
		if(list!=null){
			try{
				synchronized (list) {
					while (list.isEmpty()) 
						list.wait();
					return list.poll();
				}
			}catch(InterruptedException e){
				new bsException(e, iStub.log_ERROR);
			}
		}
		return null;
	}
	
	public Queue<T> unsubscribe(String address){
		if(address==null || address.equals(""))
			return null;
		else
			return this.list.remove(address);
	}	
	
	public boolean push(String address, T message){
		if(address==null || address.equals("") || message==null)
			return false;
		Queue<T> queue = this.list.get(address);
		if(queue==null)
			return false;
		synchronized (queue) {
			boolean added = queue.add(message);
			if(added)
				queue.notifyAll();
			return added;
		}
	}
	
	public Queue<T> push(String address, T message, long maxLifeTimeMillis){
		if(address==null || address.equals("") || message==null)
			return null;
		Queue<T> queue = null;
		if(this.list.containsKey(address))
			queue = this.list.get(address);
		else{
			queue = new LinkedList<T>();
			synchronized (keyQueue) {
				keyQueue.add(new ExpiringKey<String>(address, maxLifeTimeMillis));
				keyQueue.notifyAll();
			}
			this.list.put(address, queue);
		}
//		Queue<T> queue = this.list.putIfAbsent(address, new LinkedList<T>());
		if(queue==null)
			return queue;
		synchronized (queue) {
			boolean added = queue.add(message);
			if(added)
				queue.notifyAll();
			return queue;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void subscribeAndPush(String address, T message, long maxLifeTimeMillis){
//		Queue<T> result = this.push(address, message, maxLifeTimeMillis);
		if(CMEX_TOPIC.equalsIgnoreCase(this.type)){
			HashMap h_user_container = (HashMap)bsController.getFromLocalContainer(util_usersInSession.CONST_APP_USER_CONTAINER);
			if(h_user_container==null) h_user_container = new HashMap();
			for(Object current: h_user_container.values()){
				if(current instanceof HttpSession){
					try{
						CmexContainer.topic((HttpSession)current).push(address, message, maxLifeTimeMillis);
					}catch(Exception e){
						new bsException(e, iStub.log_ERROR);
					}
				}
			}
		}
		if(CMEX_QUEUE.equalsIgnoreCase(this.type)){
			try{
				CmexContainer.queue().push(address, message, maxLifeTimeMillis);
			}catch(Exception e){
				new bsException(e, iStub.log_ERROR);
			}
		}
//		return result;
	}

	

	private class ExpiringKey<K> implements Delayed {

        private long startTime = System.currentTimeMillis();
		private final long maxLifeTimeMillis;
        private final K key;

        public ExpiringKey(K key, long maxLifeTimeMillis) {
            this.maxLifeTimeMillis = maxLifeTimeMillis;
            this.key = key;
        }

        public K getKey() {
            return this.key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExpiringKey<K> other = (ExpiringKey<K>) obj;
            if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
                return false;
            }
            return true;
        }


        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (this.key != null ? this.key.hashCode() : 0);
            return hash;
        }


        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
        }

        private long getDelayMillis() {
            return (startTime + maxLifeTimeMillis) - System.currentTimeMillis();
        }

//        public void renew() {
//            startTime = System.currentTimeMillis();
//        }
//
//        public void expire() {
//            startTime =  System.currentTimeMillis() - maxLifeTimeMillis - 1;
//        }

        @Override
        public int compareTo(Delayed that) {
            return Long.compare(this.getDelayMillis(), ((ExpiringKey) that).getDelayMillis());
        }
    }

}
