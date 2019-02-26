package it.classhidra.plugin.vfs;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;

public class util_virtualvfs_classes {

	public util_virtualvfs_classes() {

	}
	
	public static ArrayList<String> getResources(URL resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null || !resource.getProtocol().equalsIgnoreCase("vfs"))
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getRoot(resource);
		}catch(Exception e){
		}
		if(vFile==null) 
			return res;
	

		try {
			List<VirtualFile> array = vFile.getChildren();
			for(int i=0;i<array.size();i++)
				res.add(  ((VirtualFile)array.get(i)).getName() );
		}catch(Exception e) {
			new bsException(e,iStub.log_ERROR);
		}
		
		return res;
	}
	
	public static ArrayList<String> getChildrenPathName(URL resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null || !resource.getProtocol().equalsIgnoreCase("vfs"))
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getRoot(resource);
		}catch(Exception e){
		}
		if(vFile==null)
			return res;
		
		
		res.add(vFile.getPathName());
		
		try {
			List<VirtualFile> files = vFile.getChildren();
			for(int i=0;i<files.size();i++)
				res.add(((VirtualFile)files.get(i)).getPathName());
		}catch(Exception e) {
			new bsException(e,iStub.log_ERROR);
		}	
		
		return res;
	}
	
	public static ArrayList<String> getChildrenPathName(String resource) throws ClassNotFoundException {
		ArrayList<String> res = new ArrayList<String>();
		if(resource==null)
			return res;
		
		VirtualFile vFile = null;
		try{
			vFile = VFS.getRoot(new URI(resource));
		}catch(Exception e){
		}
		if(vFile==null)
			return res;
		
		
		res.add(vFile.getPathName());
		
		try {
			List<VirtualFile> files = vFile.getChildren();
			for(int i=0;i<files.size();i++)
				res.add(((VirtualFile)files.get(i)).getPathName());
		}catch(Exception e) {
			new bsException(e,iStub.log_ERROR);
		}		
		
		return res;
	}	

}
