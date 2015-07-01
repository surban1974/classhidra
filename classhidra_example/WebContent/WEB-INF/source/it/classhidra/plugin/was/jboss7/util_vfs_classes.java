package it.classhidra.plugin.was.jboss7;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class util_vfs_classes {

	public util_vfs_classes() {

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

}
