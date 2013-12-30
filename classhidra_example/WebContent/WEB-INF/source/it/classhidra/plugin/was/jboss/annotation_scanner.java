package it.classhidra.plugin.was.jboss;

import it.classhidra.annotation.i_annotation_scanner;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.Section;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.annotation.elements.Transformation;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_apply_to_action;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.controller.info_call;
import it.classhidra.core.controller.info_entity;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.info_transformation;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_sort;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;



public class annotation_scanner extends it.classhidra.annotation.annotation_scanner implements i_annotation_scanner{
	
	private VirtualFile vFile;
	
	
	public annotation_scanner(){
		super();
	}
	

	public void loadObject(){

			if(package_annotated!=null && !package_annotated.trim().equals("")){
				try{
					
					try {
						ClassLoader cld = Thread.currentThread().getContextClassLoader();
						if (cld == null)
							throw new ClassNotFoundException("Can't get class loader.");
						String path = null;
						if(package_annotated.lastIndexOf(".class")==package_annotated.length()-6){
							package_annotated=package_annotated.substring(0,package_annotated.length()-6);
							path = '/' + package_annotated.replace('.', '/')+".class";
						}else
							path = '/' + package_annotated.replace('.', '/');
						URL resource = cld.getResource(path);
						if (resource == null) {
							path = package_annotated.replace('.', '/');
							resource = cld.getResource(path);
						}
						if (resource == null) {
							throw new ClassNotFoundException("No resource for " + path);
						}
						
						try{
							vFile = VFS.getRoot(resource);
						}catch(Exception e){
						}

						try{
							directory = util_classes.convertUrl2File(resource);
						}catch(Exception e){
						}	
					}catch(Exception ex){				
						new bsException("Load_actions ClassLoader Error Annotation scaner: "+ex.toString(), iStub.log_ERROR);
					}catch(Throwable t){
						new bsException("Load_actions ClassLoader Error Annotation scaner: "+t.toString(), iStub.log_ERROR);
					}
					

					
					if(directory!=null){
						if (directory.exists() && directory.isDirectory()) {
							File[] files = directory.listFiles();
							for(int i=0;i<files.length;i++){
								String package_path = files[i].getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
								package_path=package_annotated+package_path;
								if(files[i].isDirectory())
									checkBranch(package_path);					
								else{
									String class_path=package_path;
									if(class_path.lastIndexOf(".class")==class_path.length()-6)
										class_path=class_path.substring(0,class_path.length()-6);
									checkClassAnnotation(class_path);
	
								}
							}
						}
						if (directory.exists() && directory.isFile()) {
								String package_path = directory.getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
								package_path=package_annotated+package_path;
								String class_path=package_path;
								if(class_path.lastIndexOf(".class")==class_path.length()-6)
									class_path=class_path.substring(0,class_path.length()-6);
								checkClassAnnotation(class_path);
	
						}
						return;
					}
					if(vFile!=null){
						if (vFile.exists() && vFile.getChildren().size()>0) {
							List files = vFile.getChildren();
							for(int i=0;i<files.size();i++){
								String package_path = ((VirtualFile)files.get(i)).getPathName();
//								package_path=package_annotated+package_path;
								if(((VirtualFile)files.get(i)).getChildren().size()>0)
									checkBranchVFile((VirtualFile)files.get(i));					
								else{
									String class_path=package_path;
									if(class_path.lastIndexOf(".class")==class_path.length()-6)
										class_path=class_path.substring(0,class_path.length()-6);
									class_path=class_path.replace("/", ".").replace("\\", ".");
									if(class_path.indexOf(package_annotated)!=0)
										class_path = class_path.substring(class_path.indexOf(package_annotated),class_path.length());

									checkClassAnnotation(class_path);
	
								}
							}
						}
						if (vFile.exists() && vFile.getChildren().size()==0) {
							String package_path = vFile.getPathName();
								String class_path=package_path;
								if(class_path.lastIndexOf(".class")==class_path.length()-6)
									class_path=class_path.substring(0,class_path.length()-6);
								class_path=class_path.replace("/", ".").replace("\\", ".");
								if(class_path.indexOf(package_annotated)!=0)
									class_path = class_path.substring(class_path.indexOf(package_annotated),class_path.length());

								checkClassAnnotation(class_path);
	
						}
						return;
						
					}
					
	
				}catch(Exception e){
					new bsException("Load_actions Loader Error Annotation scaner: "+e.toString(), iStub.log_ERROR);
				}catch(Throwable t){
					new bsException("Load_actions Loader Error Annotation scaner: "+t.toString(), iStub.log_ERROR);
				}
			}
	}
	
	
	private List checkBranchVFile(VirtualFile file_){
		List array = new ArrayList();
		try{
			array = file_.getChildren();
		}catch(Exception e){
		}

		for(int i=0;i<array.size();i++){
			VirtualFile current =  (VirtualFile)array.get(i);
			String package_path = current.getPathName();
			try{
				if(current.getChildren().size()>0)
					checkBranchVFile(current);
				else{
					String class_path=package_path;
					if(class_path.lastIndexOf(".class")==class_path.length()-6)
						class_path=class_path.substring(0,class_path.length()-6);	
					class_path=class_path.replace("/", ".").replace("\\", ".");
					if(class_path.indexOf(package_annotated)!=0)
						class_path = class_path.substring(class_path.indexOf(package_annotated),class_path.length());
					
					checkClassAnnotation(class_path);
	
				}
			}catch(Exception e){
				
			}
		}
		return array;
	}



}
