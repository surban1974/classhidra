package it.classhidra.core.tool.util; 



import java.io.File;


public class util_container {

	private static util_container_servlet container;


	
	
	public util_container(){
		super();
		
	}
	
	public static byte[] getContentAsByte(File _resource){
		byte[] result=null;		
		try{
			if(container == null){
				container = new util_container_servlet();
				container.init();
			}

			result = (byte[])util_container_servlet.getFromLocalContainer(_resource.getAbsolutePath());
			if(result!=null) return result;
		}catch (Exception e) {
			e.toString();
		}catch (Throwable t) {
			t.toString();
		}
		
		try{
			result = util_file.getBytesFromFile(_resource);
			if(container!=null)
				util_container_servlet.setToLocalContainer(_resource.getAbsolutePath(), result);
			
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public static String getContentAsString(File _resource){
		byte[] result=null;		
		try{
			if(container == null){
				container = new util_container_servlet();
				container.init();
			}

			result = (byte[])util_container_servlet.getFromLocalContainer(_resource.getAbsolutePath());
			if(result!=null) return new String(result);
		}catch (Exception e) {
			e.toString();
		}catch (Throwable t) {
			t.toString();
		}	
		
		try{
			result = util_file.getBytesFromFile(_resource);
			if(container!=null)
				util_container_servlet.setToLocalContainer(_resource.getAbsolutePath(), result);
			
			return new String(result);
		}catch(Exception e){
			return "";
		}
	}	
	public static Object getContentAsObject(String key){
		Object result=null;		
		try{
			if(container == null){
				container = new util_container_servlet();
				container.init();
			}

			result = util_container_servlet.getFromLocalContainer(key);
			 
		}catch (Exception e) {
			e.toString();
		}catch (Throwable t) {
			t.toString();
		}	
		return result;
	}	
	
	public static void setContentAsObject(String key, Object obj){

		try{
			if(container == null){
				container = new util_container_servlet();
				container.init();
			}

			util_container_servlet.setToLocalContainer(key,obj);
			 
		}catch (Exception e) {
			e.toString();
		}catch (Throwable t) {
			t.toString();
		}	

	}

	public static util_container_servlet getContainer() {
		return container;
	}

	public static void setContainer(util_container_servlet container) {
		util_container.container = container;
	}	
}
