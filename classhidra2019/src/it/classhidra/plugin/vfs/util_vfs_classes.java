package it.classhidra.plugin.vfs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class util_vfs_classes {

	public util_vfs_classes() {

	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<String> getResources(URL resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null || !resource.getProtocol().equalsIgnoreCase("vfs"))
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getChild(resource);
		}catch(Exception e){
		}
		if(vFile==null) 
			return res;
	


		List<VirtualFile> array = vFile.getChildren();
		for(int i=0;i<array.size();i++)
			res.add(  ((VirtualFile)array.get(i)).getName() );
		
		return res;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<String> getChildrenPathName(URL resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null || !resource.getProtocol().equalsIgnoreCase("vfs"))
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getChild(resource);
		}catch(Exception e){
		}
		if(vFile==null)
			return res;
		
		
		res.add(vFile.getPathName());
		
		List<VirtualFile> files = vFile.getChildren();
		for(int i=0;i<files.size();i++)
			res.add(((VirtualFile)files.get(i)).getPathName());
	
		
		return res;
	}
	
	public static ArrayList<String> getChildrenPathName(String resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null)
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getChild(resource);
		}catch(Exception e){
		}
		if(vFile==null)
			return res;
		
		
		res.add(vFile.getPathName());
		
		List<VirtualFile> files = vFile.getChildren();
		for(int i=0;i<files.size();i++)
			res.add(((VirtualFile)files.get(i)).getPathName());
	
		
		return res;
	}
}
