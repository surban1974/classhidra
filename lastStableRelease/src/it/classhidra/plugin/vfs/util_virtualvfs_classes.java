package it.classhidra.plugin.vfs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

public class util_virtualvfs_classes {

	public util_virtualvfs_classes() {

	}
	
	public static ArrayList getResources(URL resource) throws ClassNotFoundException {
		ArrayList res = new ArrayList();
		if(resource==null || !resource.getProtocol().equalsIgnoreCase("vfs"))
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getChild(resource);
		}catch(Exception e){
		}
		if(vFile==null) 
			return res;
	


		List array = vFile.getChildren();
		for(int i=0;i<array.size();i++)
			res.add(  ((VirtualFile)array.get(i)).getName() );
		
		return res;
	}
	
	public static ArrayList getChildrenPathName(URL resource) throws ClassNotFoundException {
		ArrayList res = new ArrayList();
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
		
		List files = vFile.getChildren();
		for(int i=0;i<files.size();i++)
			res.add(((VirtualFile)files.get(i)).getPathName());
	
		
		return res;
	}
	
	public static ArrayList getChildrenPathName(String resource) throws ClassNotFoundException {
		ArrayList res = new ArrayList();
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
		
		List files = vFile.getChildren();
		for(int i=0;i<files.size();i++)
			res.add(((VirtualFile)files.get(i)).getPathName());
	
		
		return res;
	}	

}
