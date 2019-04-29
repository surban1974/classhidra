package it.classhidra.annotation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.jar.JarEntry;

import it.classhidra.annotation.elements.Access;
import it.classhidra.annotation.elements.AccessRelation;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.ActionService;
import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Async;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.Locked;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.ResponseHeader;
import it.classhidra.annotation.elements.Rest;
import it.classhidra.annotation.elements.Section;
import it.classhidra.annotation.elements.ServletContextDirective;
import it.classhidra.annotation.elements.SessionDirective;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.annotation.elements.Transformation;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_apply_to_action;
import it.classhidra.core.controller.info_async;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.controller.info_call;
import it.classhidra.core.controller.info_entity;
import it.classhidra.core.controller.info_header;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_relation;
import it.classhidra.core.controller.info_rest;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.info_transformation;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.v2.Util_sort;





public class annotation_scanner implements i_annotation_scanner {
	
	protected HashMap<String,info_action> _actions = new HashMap<String,info_action>();
	protected HashMap<String,info_stream> _streams = new HashMap<String,info_stream>();


	protected HashMap<String,info_bean> _beans = new HashMap<String,info_bean>();
	protected HashMap<String,info_redirect> _redirects = new HashMap<String,info_redirect>();
	protected HashMap<String,info_redirect> _redirectsjustloaded = new HashMap<String,info_redirect>();
	protected HashMap<String,info_transformation> _transformationoutput = new HashMap<String,info_transformation>();
	protected List<info_entity> v_permissions = new ArrayList<info_entity>();
	
	protected String error;
	protected String auth_error;
	protected String session_error;
	protected String listener_actions;
	protected String listener_beans;
	protected String listener_streams;
	protected String memoryInContainer_streams;
	protected String provider;
	protected String instance_navigated;
	protected String instance_local_container;
	protected String instance_scheduler_container;
	protected String instance_onlysession;
	protected String instance_servletcontext;

	

	protected String package_annotated = "";
	
	protected File directory = null;
	protected ClassLoader cld = Thread.currentThread().getContextClassLoader();
	
	
	public annotation_scanner(){
		super();
	}


	public void loadAllObjects(String _package_annotated,HashMap<String,info_redirect> redirects){
		if(redirects!=null) _redirects=redirects;
		package_annotated=_package_annotated;
		bsController.writeLog("Start Load_actions from "+package_annotated,iStub.log_INFO);
		loadObject();
		bsController.writeLog("Load_actions from "+package_annotated+" OK ",iStub.log_INFO);
	}
	


	public void loadAllObjects(HashMap<String,info_redirect> redirects){
		


		
		if(redirects!=null) _redirects=redirects;
		
		List<String> list_package_annotated = bsController.getAppInit().get_list_package_annotated();
				
		
		for(int n=0;n<list_package_annotated.size();n++){
			package_annotated=(String)list_package_annotated.get(n);
			bsController.writeLog("Start Load_actions from "+package_annotated,iStub.log_INFO);
			loadObject();
			bsController.writeLog("Load_actions from "+package_annotated+" OK ",iStub.log_INFO);
		}

	}
	


	public void loadObject(){

			if(package_annotated!=null && !package_annotated.trim().equals("")){
				try{
					String path = null;
					URL resource = null;
					URL resourceMETA = null;
					try {
						
						if (cld == null)
							throw new ClassNotFoundException("Can't get class loader.");
						
						if(package_annotated.lastIndexOf(".class")==package_annotated.length()-6){
							package_annotated=package_annotated.substring(0,package_annotated.length()-6);
							path = '/' + package_annotated.replace('.', '/')+".class";
						}else
							path = '/' + package_annotated.replace('.', '/');
						
						resource = cld.getResource(path);
						
						if (resource == null) {
							if(path.indexOf("/")==0)
								path = path.substring(1, path.length());
//							path = package_annotated.replace('.', '/');
							resource = cld.getResource(path);
						}
						

						if(bsController.getAppInit()!=null && bsController.getAppInit().isScannedManifest()){
							resourceMETA = cld.getResource("/META-INF/MANIFEST.MF");
							if(resourceMETA == null)
								resourceMETA = cld.getResource("META-INF/MANIFEST.MF");
						}
			
						
						if (resource == null && resourceMETA==null) {
							throw new ClassNotFoundException("No resource for " + path);
						}

					}catch(Exception ex){
						new bsException("Load_actions Loader Annotation scanner Error: "+ex.toString(), iStub.log_ERROR);
					}catch(Throwable t){
						new bsException("Load_actions Loader Annotation scanner Error: "+t.toString(), iStub.log_ERROR);
					}
					
					
					
					if(resource!=null){
						if(resource.getProtocol().equalsIgnoreCase("vfs")){
							
							@SuppressWarnings("unchecked")
							List<String> vfsList = (List<String>)util_reflect.execStaticMethod(util_classes.getVFSPluginPath(), "getChildrenPathName", new Class[]{URL.class}, new Object[]{resource});
							

							if(vfsList!=null){
								if (vfsList.size()>1) {
									for(String package_path:vfsList){
										@SuppressWarnings("unchecked")
										List<String> vfsChildren = (List<String>)util_reflect.execStaticMethod(util_classes.getVFSPluginPath(), "getChildrenPathName", new Class[]{String.class}, new Object[]{package_path});
										
										if(vfsChildren.size()>1)
											checkBranchVFS(vfsChildren);					
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
								}else if (vfsList.size()==1) {
									String package_path = (String)vfsList.get(0);
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
														
							
						}else{							
							try{
								directory = util_classes.convertUrl2File(resource);
							}catch(Exception e){
							}
							if(directory==null) return;	
							
							if(directory.exists() && directory.isDirectory()) {
								File[] files = directory.listFiles();
								for(int i=0;i<files.length;i++){
									String package_path = files[i].getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
									package_path=package_annotated+package_path;
									if(files[i].isDirectory())
										checkBranch(package_path);					
									else{
										String class_path=package_path;
										if(class_path.endsWith(".class")){
											class_path=class_path.substring(0,class_path.length()-6);
											checkClassAnnotation(class_path);
										}

									}
								}
							}else if(directory.exists() && directory.isFile()) {
									String package_path = directory.getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
									package_path=package_annotated+package_path;
									String class_path=package_path;
									if(class_path.endsWith(".class")){
										class_path=class_path.substring(0,class_path.length()-6);
										checkClassAnnotation(class_path);
									}
							}else if(resource.getProtocol().equalsIgnoreCase("jar")){
								try{
									JarURLConnection jarUrlConnection = (JarURLConnection)resource.openConnection(); 
									JarEntry rootEntry = jarUrlConnection.getJarEntry();

									Enumeration<JarEntry> en = null;
									try{
										en = jarUrlConnection.getJarFile().entries();
									}catch(Exception e){
									}

							        if (en!=null && en.hasMoreElements()){
							            while (en.hasMoreElements()){
							            	JarEntry entry = (JarEntry)en.nextElement();
							            	if(!entry.isDirectory()){
								            	String package_path = entry.getName();
								            	String correct_pack_name = null;
								            	if(package_path.startsWith(rootEntry.getName()))
								            		correct_pack_name = package_path;
								            	else if(("/"+package_path).startsWith(rootEntry.getName()))
								            		correct_pack_name = "/"+package_path;
								            	if(correct_pack_name!=null){
								            		String class_path=correct_pack_name.replace("/", ".").replace("\\", ".");
													if(class_path.endsWith(".class")){
														class_path=class_path.substring(0,class_path.length()-6);
														checkClassAnnotation(class_path);
													}
								            	}
							            	}
							            }

							        }else{
							        	String package_path = rootEntry.getName();
							        	String class_path=package_path.replace("/", ".").replace("\\", ".");
										if(class_path.endsWith(".class")){
											class_path=class_path.substring(0,class_path.length()-6);
											checkClassAnnotation(class_path);
										}
							        }
								}catch(Exception e){
								}								
							}
						}
					
					}
					
					if(resourceMETA!=null && resourceMETA.getProtocol().equalsIgnoreCase("jar")){
						try{
							JarURLConnection jarUrlConnection = (JarURLConnection)resourceMETA.openConnection(); 
							Enumeration<JarEntry> en = null;
							try{
								en = jarUrlConnection.getJarFile().entries();
							}catch(Exception e){
							}

					        if (en!=null && en.hasMoreElements()){
					            while (en.hasMoreElements()){
					            	JarEntry entry = (JarEntry)en.nextElement();
					            	if(!entry.isDirectory()){
						            	String package_path = entry.getName();
						            	String correct_pack_name = null;
						            	if(package_path.startsWith(path))
						            		correct_pack_name = package_path;
						            	else if(("/"+package_path).startsWith(path))
						            		correct_pack_name = "/"+package_path;
						            	if(correct_pack_name!=null){
							            	String class_path=correct_pack_name.replace("/", ".").replace("\\", ".");
											if(class_path.endsWith(".class")){
												class_path=class_path.substring(0,class_path.length()-6);
												checkClassAnnotation(class_path);
											}
							            }						            	
						            }
					            }

					        }else{
					        	String package_path = path;
					        	String class_path=package_path.replace("/", ".").replace("\\", ".");
								if(class_path.endsWith(".class")){
									class_path=class_path.substring(0,class_path.length()-6);
									checkClassAnnotation(class_path);
								}
					        }
						}catch(Exception e){
						}

					}
					
					if(resource==null && resourceMETA==null )
						throw new ClassNotFoundException("No resource for " + path);
					
				}catch(Exception e){
					new bsException("Load_actions Loader Annotation scanner Error: "+e.toString(), iStub.log_ERROR);
				}catch(Throwable t){
					new bsException("Load_actions Loader Annotation scanner Error: "+t.toString(), iStub.log_ERROR);
				}
			}
	}
	
	
	public List<File> checkBranch(String path){
		List<File> array = new ArrayList<File>();
		try{
			array = util_classes.getResourcesAsFile(path);
		}catch(Exception e){
			array = new ArrayList<File>();
		}

		for(File current:array){
			String package_path = "";
			if(directory!=null)
				package_path = current.getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
			package_path=package_annotated+package_path;
			if(current.isDirectory())
				checkBranch(package_path);
			else{
				String class_path=package_path;
				if(class_path.lastIndexOf(".class")==class_path.length()-6)
					class_path=class_path.substring(0,class_path.length()-6);	
				checkClassAnnotation(class_path);

			}
		}
		return array;
	}

	private void checkBranchVFS(List<String> vfsChildren){

		for(String package_path:vfsChildren){
			@SuppressWarnings("unchecked")
			List<String> vfsSubChildren = (List<String>)util_reflect.execStaticMethod(util_classes.getVFSPluginPath(), "getChildrenPathName", new Class[]{String.class}, new Object[]{package_path});

			try{
				if(vfsSubChildren.size()>1)
					checkBranchVFS(vfsSubChildren);
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
				
			}catch(Throwable t){
			
			}
				
		}

	}
	
	
	

	public void checkClassAnnotation(String class_path) {
		try{
			checkClassAnnotation(null, class_path, null);
		}catch(Exception e){
			new bsException("Load_actions Loader Annotation scann "+class_path+" Error: "+e.toString(), iStub.log_ERROR);
		}catch(Throwable t){
			new bsException("Load_actions Loader Annotation scann "+class_path+" Error: "+t.toString(), iStub.log_ERROR);
		}
	}
	
	public void checkClassAnnotation(Class<?> classType, String class_path, Class<?> checkClassType) {
		v_permissions = new ArrayList<info_entity>();
		try{
			if(classType==null)
				classType = Class.forName(class_path);
			
			if(classType==null)
				return;
			
			Map<String,Annotation> subAnnotations = new HashMap<String,Annotation>();
			Annotation subAnnotation = classType.getAnnotation(NavigatedDirective.class);
				if(subAnnotation!=null){
					checkClassAnnotation(classType, class_path, subAnnotation, subAnnotations);
					subAnnotations.put("NavigatedDirective", subAnnotation);
				}
			subAnnotation = classType.getAnnotation(SessionDirective.class);
				if(subAnnotation!=null){
					checkClassAnnotation(classType, class_path, subAnnotation, subAnnotations);
					subAnnotations.put("SessionDirective", subAnnotation);
				}	
			subAnnotation = classType.getAnnotation(ServletContextDirective.class);
				if(subAnnotation!=null){
					checkClassAnnotation(classType, class_path, subAnnotation, subAnnotations);
					subAnnotations.put("ServletContextDirective", subAnnotation);
				}	
				
			subAnnotation = classType.getAnnotation(Locked.class);
				if(subAnnotation!=null){
					checkClassAnnotation(classType, class_path, subAnnotation, subAnnotations);
					subAnnotations.put("Locked", subAnnotation);
				}	
	
			ActionMapping annotationActionMapping = (ActionMapping)classType.getAnnotation(ActionMapping.class);
			if(annotationActionMapping!=null){
				if(!annotationActionMapping.error().equals(""))
					error=annotationActionMapping.error();
				if(!annotationActionMapping.session_error().equals(""))
					session_error=annotationActionMapping.session_error();
				if(!annotationActionMapping.auth_error().equals(""))
					auth_error=annotationActionMapping.auth_error();
				if(!annotationActionMapping.listener_actions().equals(""))
					listener_actions=annotationActionMapping.listener_actions();
				if(!annotationActionMapping.listener_beans().equals(""))
					listener_beans=annotationActionMapping.listener_beans();
				if(!annotationActionMapping.listener_streams().equals(""))
					listener_streams=annotationActionMapping.listener_streams();
				if(!annotationActionMapping.memoryInContainer_streams().equals(""))
					memoryInContainer_streams=annotationActionMapping.memoryInContainer_streams();
				if(!annotationActionMapping.provider().equals(""))
					provider=annotationActionMapping.provider();
				if(!annotationActionMapping.instance_navigated().equals(""))
					instance_navigated=annotationActionMapping.instance_navigated();
				if(!annotationActionMapping.instance_local_container().equals(""))
					instance_local_container=annotationActionMapping.instance_local_container();
				if(!annotationActionMapping.instance_scheduler_container().equals(""))
					instance_scheduler_container=annotationActionMapping.instance_scheduler_container();
				if(!annotationActionMapping.instance_onlysession().equals(""))
					instance_onlysession=annotationActionMapping.instance_onlysession();
				if(!annotationActionMapping.instance_servletcontext().equals(""))
					instance_servletcontext=annotationActionMapping.instance_servletcontext();
				
				Stream[] streams = annotationActionMapping.streams();
				if(streams!=null && streams.length>0){
					for(int i=0;i<streams.length;i++)
						checkClassAnnotation(classType, class_path, streams[i], subAnnotations);
				}
				Bean[] beans = annotationActionMapping.beans();
				if(beans!=null && beans.length>0){
					for(int i=0;i<beans.length;i++)
						checkClassAnnotation(classType, class_path, beans[i], subAnnotations);
				}
				Redirect[] redirects = annotationActionMapping.redirects();
				if(redirects!=null && redirects.length>0){
					for(int i=0;i<redirects.length;i++)
						checkClassAnnotation(classType, class_path, redirects[i], subAnnotations);
				}
				Action[] actions = annotationActionMapping.actions();
				if(actions!=null && actions.length>0){
					for(int i=0;i<actions.length;i++)
						checkClassAnnotation(classType, class_path, actions[i], subAnnotations);
				}
				Transformation[] transformations = annotationActionMapping.transformations();
				if(transformations!=null && transformations.length>0){
					for(int i=0;i<transformations.length;i++)
						checkClassAnnotation(classType, class_path, transformations[i], subAnnotations);
				}
			}
			
			if(i_action.class.isAssignableFrom(classType)){
					
					List<Method> callMethods = new ArrayList<Method>();
					List<Method> actionMethods = new ArrayList<Method>();
					Method[] mtds = classType.getMethods();
					for(Method current:mtds){
						if(current.getAnnotation(Action.class)!=null)
							actionMethods.add(current);
						if(current.getAnnotation(ActionCall.class)!=null)
							callMethods.add( current);
					}
					for(int i=0;i<actionMethods.size();i++)
						checkActionAnnotations(classType, class_path, actionMethods.get(i).getAnnotation(Action.class), subAnnotations, (Method)actionMethods.get(i));
					
					for(int i=0;i<callMethods.size();i++)
						checkActionCallAnnotation(((Method)callMethods.get(i)).getAnnotation(ActionCall.class), null, callMethods.get(i), i);
				
			}
			

				
			Annotation annotation = null;
			if(checkClassType==null || (checkClassType!=null && checkClassType.equals(i_bean.class))){
				annotation = classType.getAnnotation(Bean.class);
					if(annotation!=null) 
						checkClassAnnotation(classType, class_path, annotation, subAnnotations);
				}
			if(checkClassType==null || (checkClassType!=null && checkClassType.equals(i_action.class))){
				annotation = classType.getAnnotation(Action.class);
					if(annotation!=null) 
						checkClassAnnotation(classType, class_path, annotation, subAnnotations);
			}
			if(checkClassType==null || (checkClassType!=null && checkClassType.equals(i_stream.class))){
				annotation = classType.getAnnotation(Stream.class);
					if(annotation!=null) checkClassAnnotation(classType, class_path, annotation, subAnnotations);
			}
			if(checkClassType==null || (checkClassType!=null && checkClassType.equals(i_transformation.class))){
				annotation = classType.getAnnotation(Transformation.class);
					if(annotation!=null) checkClassAnnotation(classType, class_path, annotation, subAnnotations);
			}
		    
		    

			if(bsController.getAuth_config()!=null){
				if(v_permissions.size()>0){
					for(info_entity iEntity:v_permissions){
						if(iEntity.getAccess_allowed()!=null && iEntity.getAccess_allowed().size()>0){
							Iterator<Entry<String, info_relation>> it = iEntity.getAccess_allowed().entrySet().iterator();
						    while (it.hasNext()) {
						        Map.Entry<String, info_relation> pair = it.next();
						        try{
						        	bsController.getAuth_config().addIRelation((info_relation)pair.getValue());
						        }catch(Exception e){
						        	e.toString();
						        }
						    }
						}
						if(iEntity.getAccess_forbidden()!=null && iEntity.getAccess_forbidden().size()>0){
							Iterator<Entry<String, info_relation>> it = iEntity.getAccess_forbidden().entrySet().iterator();
						    while (it.hasNext()) {
						        Map.Entry<String, info_relation> pair = it.next();
						        try{
						        	bsController.getAuth_config().addIRelation((info_relation)pair.getValue());
						        }catch(Exception e){
						        	e.toString();
						        }
						    }
						}
						
					}
					
				}
			}
			
			
			if(	i_action.class.isAssignableFrom(classType) ||
				i_bean.class.isAssignableFrom(classType) ||
				i_stream.class.isAssignableFrom(classType)){
				
	    		Class<?>[] interfaces = classType.getInterfaces();
	    		for(int k=0;k<interfaces.length;k++){
	    			if(i_action.class.isAssignableFrom(interfaces[k]) && !interfaces[k].equals(i_action.class))
	    				checkClassAnnotation(interfaces[k], class_path, i_action.class);
	    			if(i_bean.class.isAssignableFrom(interfaces[k]) && !interfaces[k].equals(i_bean.class) && !interfaces[k].equals(i_action.class))
	    				checkClassAnnotation(interfaces[k], class_path, i_bean.class);
	    			if(i_stream.class.isAssignableFrom(interfaces[k]) && !interfaces[k].equals(i_stream.class))
	    				checkClassAnnotation(interfaces[k], class_path, i_stream.class);
	    			if(i_transformation.class.isAssignableFrom(interfaces[k]) && !interfaces[k].equals(i_transformation.class))
	    				checkClassAnnotation(interfaces[k], class_path, i_transformation.class);
	    		}

				
			}
//			new bsException("OK scanned class: "+class_path, iStub.log_INFO);
		}catch(Exception e){	
			new bsException("Load_actions Loader Error Annotation scaner for class: "+class_path+" : "+e.toString(), iStub.log_ERROR);
		}
		
		
	    
	}
	
	private void checkClassAnnotation(Class<?> classType, String class_path, Annotation annotation, Map<String,Annotation> subAnnotations) {
		try{
			
		    Bean annotationBean = null;
		    Action annotationAction = null;
		    Stream annotationStream = null;
		    Transformation annotationTransformation = null;
		    Redirect annotationRedirect = null;



		    if(annotation instanceof Bean) annotationBean = (Bean)annotation;
		    if(annotation instanceof Redirect) annotationRedirect = (Redirect)annotation;
		    if(annotation instanceof Action) annotationAction = (Action)annotation;
		    if(annotation instanceof Stream) annotationStream = (Stream)annotation;
		    if(annotation instanceof Transformation) annotationTransformation = (Transformation)annotation;
	
	
		    if (annotationBean != null) {
		    	info_bean iBean = new info_bean();
		    	iBean.setName(annotationBean.name());
		    	if(classType!=null && i_bean.class.isAssignableFrom(classType))
		    		iBean.setType(class_path);
		    	iBean.setListener(annotationBean.listener());
		    	setEntity(iBean,annotationBean.entity());
		    	iBean.setAnnotationLoaded(true);
		    	_beans.put(iBean.getName(),iBean);
		    }
		    
		    if (annotationRedirect != null) {
    			info_redirect iRedirect = new info_redirect();		    			
    			iRedirect.setPath(annotationRedirect.path());
    			iRedirect.setAuth_id(annotationRedirect.auth_id());
    			iRedirect.setError(annotationRedirect.error());
    			iRedirect.setDescr(annotationRedirect.descr());
    			iRedirect.setMess_id(annotationRedirect.mess_id());
    			iRedirect.setUnited_id(annotationRedirect.united_id());
    			iRedirect.setImg(annotationRedirect.img());
    			iRedirect.setNavigated(annotationRedirect.navigated());
    			iRedirect.setContentType(annotationRedirect.contentType());
    			iRedirect.setContentEncoding(annotationRedirect.contentEncoding());
    			iRedirect.setContentName(annotationRedirect.contentName());
    			iRedirect.setFlushBuffer(new Boolean(annotationRedirect.flushBuffer()).toString());
    			iRedirect.setTransformationName(annotationRedirect.transformationName());
    			iRedirect.setAvoidPermissionCheck(annotationRedirect.avoidPermissionCheck());
    			setEntity(iRedirect,annotationRedirect.entity());
    			iRedirect.setAnnotationLoaded(true);

    			Section[] sections = annotationRedirect.sections();
    			if(sections!=null && sections.length>0){
    				for(int j=0;j<sections.length;j++){
    					Section annotationSection = sections[j];
    					info_section iSection = new info_section();
    					iSection.setName(annotationSection.name());
    					iSection.setAllowed(annotationSection.allowed());
    					setEntity(iSection,annotationSection.entity());
    					if(iSection.getOrder().equals("")) iSection.setOrder(Integer.valueOf(j+1).toString());
    					iSection.setAnnotationLoaded(true);
    					iRedirect.get_sections().put(iSection.getName(),iSection);
    				}

    				iRedirect.getV_info_sections().addAll(new Vector<info_section>(iRedirect.get_sections().values()));
//   				iRedirect.setV_info_sections(new util_sort().sort(iRedirect.getV_info_sections(),"int_order"));
    				iRedirect.setV_info_sections(Util_sort.sort(iRedirect.getV_info_sections(),"int_order"));

    			}
    			Transformation[] transformations = annotationRedirect.transformations();
    			if(transformations!=null && transformations.length>0){
    				for(int j=0;j<transformations.length;j++){
    					Transformation annotationTransf = transformations[j];
    					info_transformation iTransformationoutput = new info_transformation();
    					iTransformationoutput.setName(annotationTransf.name());
    					iTransformationoutput.setType(annotationTransf.type());
    					iTransformationoutput.setPath(annotationTransf.path());
    					iTransformationoutput.setEvent(annotationTransf.event());
    					iTransformationoutput.setInputformat(annotationTransf.inputformat());
    					iTransformationoutput.setMemoryInContainer(String.valueOf(annotationTransf.memoryInContainer()));

    					setEntity(iTransformationoutput,annotationTransf.entity());
    					if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(j+1).toString());
    					iTransformationoutput.setAnnotationLoaded(true);
    					iRedirect.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
    				}
    				
    				iRedirect.getV_info_transformationoutput().addAll(new Vector<info_transformation>(iRedirect.get_transformationoutput().values()));
//    				iRedirect.setV_info_transformationoutput(new util_sort().sort(iRedirect.getV_info_transformationoutput(),"int_order"));
    				iRedirect.setV_info_transformationoutput(Util_sort.sort(iRedirect.getV_info_transformationoutput(),"int_order"));
    			}
    			_redirects.put(bodyURI(iRedirect.getPath()),iRedirect);
    			_redirectsjustloaded.put(bodyURI(iRedirect.getPath()),iRedirect);
		    	
		    }
		    
		    if (annotationAction != null) {
		    	checkActionAnnotations(classType, class_path, annotationAction, subAnnotations, null);
		    }
	
		    if (annotationTransformation != null) {
				info_transformation iTransformationoutput = new info_transformation();
				iTransformationoutput.setName(annotationTransformation.name());
				if(classType!=null && i_transformation.class.isAssignableFrom(classType))
					iTransformationoutput.setType(class_path);
				iTransformationoutput.setPath(annotationTransformation.path());
				iTransformationoutput.setEvent(annotationTransformation.event());
				iTransformationoutput.setInputformat(annotationTransformation.inputformat());
				iTransformationoutput.setMemoryInContainer(String.valueOf(annotationTransformation.memoryInContainer()));

				setEntity(iTransformationoutput,annotationTransformation.entity());
				iTransformationoutput.setAnnotationLoaded(true);
				_transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
		    	
		    }
		    
		    if (annotationStream != null) {
		    	info_stream iStream = new info_stream();
		    	iStream.setName(annotationStream.name());
		    	if(classType!=null && i_stream.class.isAssignableFrom(classType))
		    		iStream.setType(class_path);
		    	iStream.setListener(annotationStream.listener());
		    	setEntity(iStream,annotationStream.entity());
		    	iStream.setAnnotationLoaded(true);
		    	Apply_to_action[] applied = annotationStream.applied();
		    	if(applied!=null){
		    		int order=0;
		    		for(int i=0;i<applied.length;i++){
		    			info_apply_to_action iApply = new info_apply_to_action();
		    			iApply.setAction(applied[i].action());
		    			iApply.setExcluded(applied[i].excluded());
		    			order++;
		    			iApply.setOrder(Integer.valueOf(order).toString());
		    			iApply.setAnnotationLoaded(true);
		    			iStream.get_apply_to_action().put(iApply.getAction(),iApply);		    			
		    		}
		    		iStream.getV_info_apply_to_action().addAll(new Vector<info_apply_to_action>(iStream.get_apply_to_action().values()));
//		    		iStream.setV_info_apply_to_action(new util_sort().sort(iStream.getV_info_apply_to_action(),"int_order"));
		    		iStream.setV_info_apply_to_action(Util_sort.sort(iStream.getV_info_apply_to_action(),"int_order"));
		    	}
				Redirect redirect = annotationStream.Redirect();
		    	if(redirect!=null){
		    		info_redirect iRedirect = checkRedirectAnnotation(iStream, redirect, -1);
		    		if(iRedirect!=null && !iRedirect.isEmpty()){
		    			iStream.setIRedirect(iRedirect);
			    		if(iRedirect!=null && iRedirect.getPath()!=null && !iRedirect.getPath().equals("")){
			    			info_redirect stored = (info_redirect)_redirects.get(iRedirect.getPath());
			    			if(stored!=null){
			    				try{
			    					stored.init(iRedirect);
			    				}catch(Exception e){
			    					_redirects.put(iRedirect.getPath(),iRedirect);
			    				}
			    			}else
			    				_redirects.put(iRedirect.getPath(),iRedirect);
			    		}
		    		}
		    	}
		    	_streams.put(iStream.getName(),iStream);
		    }
		    
		    
		}catch(Exception e){	
			new bsException(e, iStub.log_ERROR);
		}
	    
	}
	
	
	private info_action checkActionAnnotations(Class<?> classType, String class_path, Action annotationAction, Map<String,Annotation>  subAnnotations, Method method) throws Exception{
		if(annotationAction==null || annotationAction.path()==null || annotationAction.path().equals(""))
			return null;
    	info_action iAction = new info_action();
    	iAction.setPath(annotationAction.path());
    	if(classType!=null && i_action.class.isAssignableFrom(classType))
    		iAction.setType(class_path);
    	iAction.setName(annotationAction.name());
    	if(method==null)
    		iAction.setMethod(annotationAction.method());
    	else{
    		if(annotationAction.method()!=null && !annotationAction.method().equals(""))
    			iAction.setMethod(annotationAction.method());
    		else if(method!=null)
    			iAction.setMethod(method.getName());
    		iAction.setMappedMethodParameterTypes(method.getParameterTypes());
    	}
    		
    	iAction.setRedirect(annotationAction.redirect());
    	iAction.setError(annotationAction.error());
    	iAction.setMemoryInSession(annotationAction.memoryInSession());
    	iAction.setMemoryInServletContext(annotationAction.memoryInServletContext());
    	
    	SessionDirective sessionDirective = (SessionDirective)subAnnotations.get("SessionDirective");
    	if(sessionDirective!=null){
    		if(!iAction.getMemoryInSession().trim().equalsIgnoreCase("true"))
	    		iAction.setMemoryInSession("true");
    	}
    	
    	ServletContextDirective servletContextDirective = (ServletContextDirective)subAnnotations.get("ServletContextDirective");
    	if(servletContextDirective!=null){
    		if(!iAction.getMemoryInServletContext().trim().equalsIgnoreCase("true"))
	    		iAction.setMemoryInServletContext("true");
    		try {
    			iAction.setMemoryInServletContextLoadOnStartup(String.valueOf(servletContextDirective.loadOnStartup()));
    		}catch (Exception e) {
			}
    	}
    	
    	Locked locked = (Locked)subAnnotations.get("Locked");
    	if(locked!=null)
    		iAction.setLocked(String.valueOf(locked.value()));
    	
    	
    	iAction.setMemoryAsLastInstance(annotationAction.memoryAsLastInstance());
    	iAction.setReloadAfterAction(annotationAction.reloadAfterAction());
    	iAction.setReloadAfterNextNavigated(annotationAction.reloadAfterNextNavigated());
    	iAction.setNavigated(annotationAction.navigated());
    	iAction.setNavigatedMemoryContent(annotationAction.navigatedMemoryContent());
    	
    	NavigatedDirective navigatedDirective = (NavigatedDirective)subAnnotations.get("NavigatedDirective");
    	if(navigatedDirective!=null){
    		if(!iAction.getNavigated().trim().equalsIgnoreCase("true"))
	    		iAction.setNavigated("true");
    		if(navigatedDirective.memoryContent()!=null && !navigatedDirective.memoryContent().equals(""))
    			iAction.setNavigatedMemoryContent(navigatedDirective.memoryContent());
    	}

    		
    	iAction.setSyncro(annotationAction.syncro());
    	iAction.setStatistic(annotationAction.statistic());
    	iAction.setHelp(annotationAction.help());
    	iAction.setListener(annotationAction.listener());
    	
		Expose action_exposed = annotationAction.Expose();
		if(action_exposed!=null){
			iAction.addExposed(action_exposed.method()).addExposed(action_exposed.methods());
			if(action_exposed.restmapping()!=null && action_exposed.restmapping().length>0){
				for(int r=0;r<action_exposed.restmapping().length;r++)
					iAction.getRestmapping().add(checkRestAnnotation(action_exposed.restmapping()[r], iAction.getExpose(), iAction));

			}
		}
    	
    	setEntity(iAction,annotationAction.entity());
    	iAction.setAnnotationLoaded(true);
    	
    	
    	Redirect redirect = annotationAction.Redirect();
    	if(redirect!=null){
    		info_redirect iRedirect = checkRedirectAnnotation(iAction, redirect, -1);
    		if(iRedirect!=null && !iRedirect.isEmpty()){
	    		if(iAction.getRedirect()==null || iAction.getRedirect().equals("")){
	    			if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
	    				iAction.setRedirect(iRedirect.getPath());
	    			iAction.setIRedirect(iRedirect);
	    		}
    		}
    	}
    	
    	Async callAsync = annotationAction.Async();
    	if(callAsync!=null && callAsync.value()){
    		info_async iAsync = checkAsyncAnnotation(callAsync);
    		if(iAsync!=null)
    			iAction.setiAsync(iAsync);
    	}
    	
    	Bean bean = annotationAction.Bean();
    	if(bean!=null){
    		info_bean iBean = checkBeanAnnotation(classType, iAction, bean, class_path, -1);
    		if(iBean!=null && (iAction.getName()==null || iAction.getName().equals("")) && iBean.getName()!=null && !iBean.getName().equals(""))
    			iAction.setName(iBean.getName());
    		
    	}
    	
    	Redirect[] redirects = annotationAction.redirects();
    	if(redirects!=null && redirects.length>0){
    		for(int i=0;i<redirects.length;i++){
    			info_redirect iRedirect = checkRedirectAnnotation(iAction, redirects[i], i);
        		if(iRedirect!=null && !iRedirect.isEmpty()){
    	    		if(iAction.getRedirect()==null || iAction.getRedirect().equals("")){
    	    			if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
    	    				iAction.setRedirect(iRedirect.getPath());
    	    		}
        		}
    		}
    	}
    	
    	if(iAction.get_redirects().size()>0 || redirect!=null){
    		iAction.getV_info_redirects().addAll(new Vector<info_redirect>(iAction.get_redirects().values()));
//    		iAction.setV_info_redirects(new util_sort().sort(iAction.getV_info_redirects(),"int_order"));
    		iAction.setV_info_redirects(Util_sort.sort(iAction.getV_info_redirects(),"int_order"));
    		
    		Object[] keys = iAction.get_redirects().keySet().toArray();
    		for (int i = 0; i < keys.length; i++){
    			info_redirect iRedirect = (info_redirect)iAction.get_redirects().get((String)keys[i]);
    			if(_redirects.get((String)keys[i])==null && iRedirect.getPath()!=null && !iRedirect.getPath().equals("*") && !iRedirect.getPath().equals("")){
        			_redirects.put(bodyURI(iRedirect.getPath()),iRedirect);
        			_redirectsjustloaded.put(bodyURI(iRedirect.getPath()),iRedirect);
    			}
    			iRedirect.init((info_redirect)_redirects.get((String)keys[i]));
    			if(!iRedirect.getAuth_id().equals("")) iAction.get_auth_redirects().put(iRedirect.getAuth_id(),iRedirect);
    		}
    	}

    	
		Transformation[] transformations = annotationAction.transformations();
		if(transformations!=null && transformations.length>0){
			for(int i=0;i<transformations.length;i++){
				Transformation annotationTransf = transformations[i];
				info_transformation iTransformationoutput = new info_transformation();
				iTransformationoutput.setName(annotationTransf.name());
				iTransformationoutput.setType(annotationTransf.type());
				iTransformationoutput.setPath(annotationTransf.path());
				iTransformationoutput.setEvent(annotationTransf.event());
				iTransformationoutput.setInputformat(annotationTransf.inputformat());
				iTransformationoutput.setMemoryInContainer(String.valueOf(annotationTransf.memoryInContainer()));
				
				setEntity(iTransformationoutput,annotationTransf.entity());
				if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(i+1).toString());
				iTransformationoutput.setAnnotationLoaded(true);
				iAction.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
			}
			
			iAction.getV_info_transformationoutput().addAll(new Vector<info_transformation>(iAction.get_transformationoutput().values()));
//			iAction.setV_info_transformationoutput(new util_sort().sort(iAction.getV_info_transformationoutput(),"int_order"));
			iAction.setV_info_transformationoutput(Util_sort.sort(iAction.getV_info_transformationoutput(),"int_order"));
		}

		
		ActionCall[] calls = annotationAction.calls();
		if(calls!=null && calls.length>0){
			for(int i=0;i<calls.length;i++){
				ActionCall annotationCall = calls[i];
				info_call iCall = new info_call();
				iCall.setOwner(iAction.getPath());
				iCall.setName(annotationCall.name());
				iCall.setPath(annotationCall.path());
				iCall.setMethod(annotationCall.method());
				iCall.setNavigated(annotationCall.navigated());
				setEntity(iCall,annotationCall.entity());
				if(iCall.getOrder().equals("")) iCall.setOrder(Integer.valueOf(i+1).toString());
				iCall.setAnnotationLoaded(true);
				
				Redirect callRedirect = annotationCall.Redirect();
		    	if(callRedirect!=null){
		    		info_redirect iRedirect = checkRedirectAnnotation(iAction, callRedirect, -1);
		    		if(iRedirect!=null && !iRedirect.isEmpty()){
			    		iCall.setIRedirect(iRedirect);
			    		if(iRedirect!=null && iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
							_redirects.put(iRedirect.getPath(),iRedirect);
		    		}
		    	}
		    	
		    	Async callAsync1 = annotationCall.Async();
		    	if(callAsync1!=null && callAsync1.value()){
		    		info_async iAsync = checkAsyncAnnotation(callAsync1);
		    		if(iAsync!=null)
		    			iCall.setiAsync(iAsync);
		    	}

				Expose call_exposed = annotationCall.Expose();
				if(call_exposed!=null){
					iCall.addExposed(call_exposed.method()).addExposed(call_exposed.methods());
					if(call_exposed.restmapping()!=null && call_exposed.restmapping().length>0){
						for(int r=0;r<call_exposed.restmapping().length;r++)
							iCall.getRestmapping().add(checkRestAnnotation(call_exposed.restmapping()[r], iCall.getExpose(), iCall));

					}
				
				}
				if(iCall.getExposed().size()==0){
					if(iAction.get_calls().get(iCall.getName())==null)
						iAction.get_calls().put(iCall.getName(),iCall);
				}else{
					for(int e=0;e<iCall.getExposed().size();e++){
						String suffix = "."+iCall.getExposed().get(e).toString();
						if(iAction.get_calls().get(iCall.getName()+suffix)==null)
							iAction.get_calls().put(iCall.getName()+suffix,iCall);
					}
				}
			}
			iAction.getV_info_calls().addAll(new Vector<info_call>(iAction.get_calls().values()));
//			iAction.setV_info_calls(new util_sort().sort(iAction.getV_info_calls(),"int_order"));
			iAction.setV_info_calls(Util_sort.sort(iAction.getV_info_calls(),"int_order"));
		}
		
		Bean[] beans = annotationAction.beans();
		if(beans!=null && beans.length>0){
			for(int i=0;i<beans.length;i++){
				info_bean iBean = checkBeanAnnotation(classType, iAction, beans[i], class_path, i);
	    		if(iBean!=null && iAction.getName()==null || iAction.getName().equals("") && iBean.getName()!=null && !iBean.getName().equals(""))
	    			iAction.setName(iBean.getName());
			}
		}
		if((beans!=null && beans.length>0) || bean!=null){
			iAction.getV_info_beans().addAll(new Vector<info_bean>(iAction.get_beans().values()));
//			iAction.setV_info_beans(new util_sort().sort(iAction.getV_info_beans(),"int_order"));
			iAction.setV_info_beans(Util_sort.sort(iAction.getV_info_beans(),"int_order"));
		}
		

		
		if(method==null){
			if(classType!=null){
				List<Method> callMethods = new ArrayList<Method>();
				List<Method> actionMethods = new ArrayList<Method>();
				List<Method> actionServiceMethods = new ArrayList<Method>();
				Method[] mtds = classType.getMethods();
				for(int i=0;i<mtds.length;i++){
					Method current = mtds[i];
					if(current.getAnnotation(Action.class)!=null)
						actionMethods.add( mtds[i]);
					if(current.getAnnotation(ActionService.class)!=null)
						actionServiceMethods.add( mtds[i]);
					if(current.getAnnotation(ActionCall.class)!=null)
						callMethods.add( mtds[i]);
				}
				for(int i=0;i<actionMethods.size();i++)
					checkActionAnnotations(classType, class_path, ((Method)actionMethods.get(i)).getAnnotation(Action.class), subAnnotations, (Method)actionMethods.get(i));

				for(int i=0;i<actionServiceMethods.size();i++)
					checkActionServiceAnnotations(iAction, classType, class_path, ((Method)actionServiceMethods.get(i)).getAnnotation(ActionService.class), subAnnotations, (Method)actionServiceMethods.get(i));
				
				for(int i=0;i<callMethods.size();i++){
					checkActionCallAnnotation(((Method)callMethods.get(i)).getAnnotation(ActionCall.class), iAction, (Method)callMethods.get(i), i);

				}
			}
		}
		
		
    	_actions.put(iAction.getPath(),iAction);
    	
    	if(classType!=null && i_action.class.isAssignableFrom(classType)){
    		Class<?>[] interfaces = classType.getInterfaces();
    		for(int k=0;k<interfaces.length;k++){
    			if(i_action.class.isAssignableFrom(interfaces[k]) && !interfaces[k].equals(i_action.class)){
					List<Method> callMethods = new ArrayList<Method>();
					List<Method> actionMethods = new ArrayList<Method>();
					List<Method> actionServiceMethods = new ArrayList<Method>();
					Method[] mtds = interfaces[k].getMethods();
					for(int i=0;i<mtds.length;i++){
						Method current = mtds[i];
						if(current.getAnnotation(Action.class)!=null)
							actionMethods.add( mtds[i]);
						if(current.getAnnotation(ActionService.class)!=null)
							actionServiceMethods.add( mtds[i]);
						if(current.getAnnotation(ActionCall.class)!=null)
							callMethods.add( mtds[i]);
					}
					for(int i=0;i<actionMethods.size();i++)
						checkActionAnnotations(interfaces[k], class_path, ((Method)actionMethods.get(i)).getAnnotation(Action.class), subAnnotations, (Method)actionMethods.get(i));

					for(int i=0;i<actionServiceMethods.size();i++)
						checkActionServiceAnnotations(iAction, interfaces[k], class_path, ((Method)actionServiceMethods.get(i)).getAnnotation(ActionService.class), subAnnotations, (Method)actionServiceMethods.get(i));
					
					for(int i=0;i<callMethods.size();i++)
						checkActionCallAnnotation(((Method)callMethods.get(i)).getAnnotation(ActionCall.class), iAction, (Method)callMethods.get(i), i);

    			}
    		}
    	}
    	
    	return iAction;
	}
	
	private info_action checkActionServiceAnnotations(info_action iAction, Class<?> classType, String class_path, ActionService annotationActionService, Map<String,Annotation>  subAnnotations, Method method) throws Exception{
		if(iAction==null || annotationActionService==null || method==null)
			return null;
		iAction.setMethod(method.getName());
    	iAction.setMappedMethodParameterTypes(method.getParameterTypes());

    	
    	NavigatedDirective navigatedDirective = (NavigatedDirective)subAnnotations.get("NavigatedDirective");
    	if(navigatedDirective!=null){
    		if(!iAction.getNavigated().trim().equalsIgnoreCase("true"))
	    		iAction.setNavigated("true");
    		if(navigatedDirective.memoryContent()!=null && !navigatedDirective.memoryContent().equals(""))
    			iAction.setNavigatedMemoryContent(navigatedDirective.memoryContent());
    	}
    	
		Expose action_exposed = annotationActionService.Expose();
		if(action_exposed!=null){
			iAction.addExposed(action_exposed.method()).addExposed(action_exposed.methods());
			if(action_exposed.restmapping()!=null && action_exposed.restmapping().length>0){
				for(int r=0;r<action_exposed.restmapping().length;r++)
					iAction.getRestmapping().add(checkRestAnnotation(action_exposed.restmapping()[r], iAction.getExpose(), iAction));

			}
		}
    	
    	setEntity(iAction,annotationActionService.entity());
    	iAction.setAnnotationLoaded(true);
    	
    	
    	Redirect redirect = annotationActionService.Redirect();
    	if(redirect!=null){
    		info_redirect iRedirect = checkRedirectAnnotation(iAction, redirect, -1);
    		if(iRedirect!=null && !iRedirect.isEmpty()){
	    		if(iAction.getRedirect()==null || iAction.getRedirect().equals("")){
	    			if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
	    				iAction.setRedirect(iRedirect.getPath());
	    			iAction.setIRedirect(iRedirect);
	    		}
    		}
    	}
    	
    	Async callAsync = annotationActionService.Async();
    	if(callAsync!=null && callAsync.value()){
    		info_async iAsync = checkAsyncAnnotation(callAsync);
    		if(iAsync!=null)
    			iAction.setiAsync(iAsync);
    	}
    	
    	return iAction;
	}
	
	
	private info_bean checkBeanAnnotation(Class<?> classType, info_action iAction, Bean annotationBean, String class_path, int i){
		if(annotationBean==null || annotationBean.name()==null || annotationBean.name().equals(""))
			return null;
		info_bean iBean = new info_bean();
		iBean.setName(annotationBean.name());
		if(classType!=null && i_bean.class.isAssignableFrom(classType))
			iBean.setType(class_path);
    	iBean.setListener(annotationBean.listener());
    	setEntity(iBean,annotationBean.entity());
    	if(iBean.getOrder().equals("")) iBean.setOrder(Integer.valueOf(i+1).toString()); 
    	iBean.setAnnotationLoaded(true);
		if(iAction.get_beans().get(iBean.getName())==null)
			iAction.get_beans().put(iBean.getName(),iBean);
		if(_beans.get(iBean.getName())==null)
			_beans.put(bodyURI(iBean.getName()),iBean);
		
		return iBean;
	}
	
	private info_async checkAsyncAnnotation(Async annotationAsync){
		info_async iAsync = new info_async();


		iAsync.setValue(new Boolean(annotationAsync.value()).toString());
		iAsync.setTimeout(new Long(annotationAsync.timeout()).toString());
		iAsync.setFlushBuffer(new Boolean(annotationAsync.flushBuffer()).toString());
		iAsync.setLoopEvery(new Long(annotationAsync.loopEvery()).toString());
		iAsync.setReInitBeenEveryLoop(new Boolean(annotationAsync.reInitBeenEveryLoop()).toString());
		ResponseHeader[] headers = annotationAsync.headers();
		if(headers!=null && headers.length>0){
			for(int j=0;j<headers.length;j++){
				ResponseHeader header = headers[j];
				info_header iHeader = new info_header();
				iHeader.setName(header.name());
				iHeader.setValue(header.value());
				iAsync.getHeaders().add(iHeader);
			}
		}



		return iAsync;
	}
	
	private info_redirect checkRedirectAnnotation(info_entity entity, Redirect annotationRedirect1, int i){
		info_redirect iRedirect = new info_redirect();		    			
		iRedirect.setPath(annotationRedirect1.path());
		iRedirect.setAuth_id(annotationRedirect1.auth_id());
		iRedirect.setError(annotationRedirect1.error());
		iRedirect.setDescr(annotationRedirect1.descr());
		iRedirect.setMess_id(annotationRedirect1.mess_id());
		iRedirect.setUnited_id(annotationRedirect1.united_id());
		iRedirect.setImg(annotationRedirect1.img());
		iRedirect.setNavigated(annotationRedirect1.navigated());
		iRedirect.setContentType(annotationRedirect1.contentType());
		iRedirect.setContentEncoding(annotationRedirect1.contentEncoding());
		iRedirect.setContentName(annotationRedirect1.contentName());
		iRedirect.setFlushBuffer(new Boolean(annotationRedirect1.flushBuffer()).toString());
		iRedirect.setTransformationName(annotationRedirect1.transformationName());
		iRedirect.setAvoidPermissionCheck(annotationRedirect1.avoidPermissionCheck());
		iRedirect.setParent(entity);
		setEntity(iRedirect,annotationRedirect1.entity());
		if(iRedirect.getOrder().equals("")) iRedirect.setOrder(Integer.valueOf(i+1).toString()); 
		iRedirect.setAnnotationLoaded(true);
		
		Section[] sections = annotationRedirect1.sections();
		if(sections!=null && sections.length>0){
			for(int j=0;j<sections.length;j++){
				Section annotationSection = sections[j];
				info_section iSection = new info_section();
				iSection.setName(annotationSection.name());
				iSection.setAllowed(annotationSection.allowed());
				iSection.setParent(iRedirect);
				setEntity(iSection,annotationSection.entity());
				if(iSection.getOrder().equals("")) iSection.setOrder(Integer.valueOf(j+1).toString());
				iSection.setAnnotationLoaded(true);
				iRedirect.get_sections().put(iSection.getName(),iSection);
			}

			iRedirect.getV_info_sections().addAll(new Vector<info_section>(iRedirect.get_sections().values()));
//			iRedirect.setV_info_sections(new util_sort().sort(iRedirect.getV_info_sections(),"int_order"));
			iRedirect.setV_info_sections(Util_sort.sort(iRedirect.getV_info_sections(),"int_order"));

		}
		Transformation[] transformations = annotationRedirect1.transformations();
		if(transformations!=null && transformations.length>0){
			for(int j=0;j<transformations.length;j++){
				Transformation annotationTransf = transformations[j];
				info_transformation iTransformationoutput = new info_transformation();
				iTransformationoutput.setName(annotationTransf.name());
				iTransformationoutput.setType(annotationTransf.type());
				iTransformationoutput.setPath(annotationTransf.path());
				iTransformationoutput.setEvent(annotationTransf.event());
				iTransformationoutput.setInputformat(annotationTransf.inputformat());
				iTransformationoutput.setMemoryInContainer(String.valueOf(annotationTransf.memoryInContainer()));
				
				iTransformationoutput.setParent(iRedirect);
				setEntity(iTransformationoutput,annotationTransf.entity());
				if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(j+1).toString());
				iTransformationoutput.setAnnotationLoaded(true);
				iRedirect.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
			}
			
			iRedirect.getV_info_transformationoutput().addAll(new Vector<info_transformation>(iRedirect.get_transformationoutput().values()));
//			iRedirect.setV_info_transformationoutput(new util_sort().sort(iRedirect.getV_info_transformationoutput(),"int_order"));
			iRedirect.setV_info_transformationoutput(Util_sort.sort(iRedirect.getV_info_transformationoutput(),"int_order"));

		}
		if(iRedirect.getPath()!=null && !iRedirect.getPath().equals("")){
			if(entity instanceof info_action)
				((info_action)entity).get_redirects().put(bodyURI(iRedirect.getPath()),iRedirect);
		}
		
		return iRedirect;

	}
	
	private info_call checkActionCallAnnotation(ActionCall annotationCall, info_action iAction, Method current, int i){
		
		info_call iCall = new info_call();
		if((annotationCall.owner()==null || annotationCall.owner().equals("")) && iAction==null)
			return null;
		if(annotationCall.owner()==null || annotationCall.owner().equals("")){
			if(iAction!=null)
				iCall.setOwner(iAction.getPath());
		}else
			iCall.setOwner(annotationCall.owner());
		iCall.setName(annotationCall.name());
		iCall.setPath(annotationCall.path());
		if(annotationCall.method()==null || annotationCall.method().equals(""))
			iCall.setMethod(current.getName());
		else
			iCall.setMethod(annotationCall.method());
		if(annotationCall.navigated()==null || annotationCall.navigated().equals(""))
			iCall.setNavigated("false");
		else iCall.setNavigated(annotationCall.navigated());
		if(current!=null)
			iCall.setMappedMethodParameterTypes(current.getParameterTypes());
		
		iCall.setParent(iAction);
		setEntity(iCall,annotationCall.entity());
		if(iCall.getOrder().equals("")) iCall.setOrder(Integer.valueOf(i+1).toString());
		iCall.setAnnotationLoaded(true);
		
		Redirect redirect = annotationCall.Redirect();
    	if(redirect!=null){
    		info_redirect iRedirect = checkRedirectAnnotation(iAction, redirect, -1);
    		if(iRedirect!=null && !iRedirect.isEmpty()){
	    		iCall.setIRedirect(iRedirect);
	    		if(iRedirect!=null && iRedirect.getPath()!=null && !iRedirect.getPath().equals("")){
	    			info_redirect stored = (info_redirect)_redirects.get(iRedirect.getPath());
	    			if(stored!=null){
	    				try{
	    					stored.init(iRedirect);
	    				}catch(Exception e){
	    					_redirects.put(iRedirect.getPath(),iRedirect);
	    				}
	    			}else
	    				_redirects.put(iRedirect.getPath(),iRedirect);
	    		}
    		}
    	}
    	
    	Async callAsync = annotationCall.Async();
    	if(callAsync!=null && callAsync.value()){
    		info_async iAsync = checkAsyncAnnotation(callAsync);
    		if(iAsync!=null)
    			iCall.setiAsync(iAsync);
    	}
    	
		Expose call_exposed = annotationCall.Expose();
		if(call_exposed!=null){
			iCall.addExposed(call_exposed.method()).addExposed(call_exposed.methods());
			if(call_exposed.restmapping()!=null && call_exposed.restmapping().length>0){
				for(int r=0;r<call_exposed.restmapping().length;r++)
					iCall.getRestmapping().add(checkRestAnnotation(call_exposed.restmapping()[r], iCall.getExpose(),iCall));
			}
		}

		
		
		if(iAction!=null && iCall.getOwner().equals(iAction.getPath())){
			if(iCall.getExposed().size()==0){
				if(iAction.get_calls().get(iCall.getName())==null)
					iAction.get_calls().put(iCall.getName(),iCall);
			}else{
				for(int e=0;e<iCall.getExposed().size();e++){
					String suffix = "."+iCall.getExposed().get(e).toString();
					if(iAction.get_calls().get(iCall.getName()+suffix)==null)
						iAction.get_calls().put(iCall.getName()+suffix,iCall);
				}
			}

		}else{
			info_action iActionOwner = (info_action)_actions.get(iCall.getOwner());
			if(iActionOwner!=null){
				
				if(iCall.getExposed().size()==0){
					if(iActionOwner.get_calls().get(iCall.getName())==null)
						iActionOwner.get_calls().put(iCall.getName(),iCall);
				}else{
					for(int e=0;e<iCall.getExposed().size();e++){
						String suffix = "."+iCall.getExposed().get(e).toString();
						if(iActionOwner.get_calls().get(iCall.getName()+suffix)==null)
							iActionOwner.get_calls().put(iCall.getName()+suffix,iCall);
					}
				}

			}
		}

		return iCall;
	}
	
	private info_rest checkRestAnnotation(Rest annotationRest, String expose, info_entity mapped_entity){
		info_rest iRest = new info_rest();

		iRest.setPath(annotationRest.path());
		iRest.setParametermapping(annotationRest.parametermapping());
		iRest.setMapped_entity(mapped_entity);
		iRest.setExpose(expose);

		return iRest;
	}	
	
	private void setEntity(info_entity iEntity, Entity entity){
		if(iEntity==null || entity==null) return;
		iEntity.setProvider(entity.provider());
		iEntity.setLookup(entity.lookup());
		iEntity.setProperty(entity.property());
		iEntity.setOrder(entity.order());
		iEntity.setComment(entity.comment());
		iEntity.setSystem(entity.system());
		iEntity.setAnnotated(entity.annotated());
		
		String element = "";
		if(iEntity instanceof info_action)
			element=((info_action)iEntity).getPath()+".*.*;";
		else if(iEntity instanceof info_call){
			if(((info_call)iEntity).getPath()!=null && !((info_call)iEntity).getPath().equals(""))
				element=((info_call)iEntity).getPath()+".*.*;";
			else if(bsController.getAppInit()!=null && bsController.getAppInit().get_actioncall_separator()!=null)
				element=((info_call)iEntity).getOwner()+bsController.getAppInit().get_actioncall_separator()+((info_call)iEntity).getName()+".*.*;";
		}else if(iEntity instanceof info_redirect){
			info_entity parent_a = iEntity.getParent();			
			element=((parent_a==null || !(parent_a instanceof info_action))?"*":((info_action)parent_a).getPath())+"."+((info_redirect)iEntity).getAuth_id()+".*;";
		}
		else if(iEntity instanceof info_section ){
			info_entity parent_r = iEntity.getParent();	
			info_entity parent_a = (parent_r==null || !(parent_r instanceof info_redirect))?null:parent_r.getParent();
			
			element=((parent_a==null || !(parent_a instanceof info_action))?"*":((info_action)parent_a).getPath())+
					"."+
					((parent_r==null || !(parent_r instanceof info_redirect))?"*":((info_redirect)parent_r).getAuth_id())+
					"."
					+((info_section)iEntity).getName()+";";
		}
		
		Access permissions = entity.permissions();
		if(permissions!=null){
			AccessRelation[] relations = permissions.allowed();
			if(relations!=null && relations.length>0){
				for(int i=0;i<relations.length;i++){
					info_relation iRelation = new info_relation();
					if(relations[i].targets()!=null)
						iRelation.setTargets(relations[i].targets());
					if(relations[i].rules()!=null)
						iRelation.setGroups(relations[i].rules());
					if(relations[i].elements()!=null)
						iRelation.setElements(relations[i].elements());
					if(relations[i].middleactions()!=null)
						iRelation.setMiddleactions(relations[i].middleactions());
					if(!element.equals(""))
						iRelation.setElements(iRelation.getElements()+element);
					iRelation.parse();
					if(!iRelation.isEmpty())
						iEntity.addRelation(iRelation);
					v_permissions.add(iEntity);
				}					
			}
			relations = permissions.forbidden();
			if(relations!=null && relations.length>0){
				for(int i=0;i<relations.length;i++){
					info_relation iRelation = new info_relation();
					if(relations[i].targets()!=null)
						iRelation.setTargets(relations[i].targets());
					if(relations[i].rules()!=null)
						iRelation.setGroups(relations[i].rules());
					if(relations[i].elements()!=null)
						iRelation.setElements(relations[i].elements());
					if(relations[i].middleactions()!=null)
						iRelation.setMiddleactions(relations[i].middleactions());
					if(!element.equals(""))
						iRelation.setElements(iRelation.getElements()+element);
					iRelation.parse();
					if(!iRelation.isEmpty())
						iEntity.addRelation(iRelation);
					v_permissions.add(iEntity);
				}
					
			}
		}
		
	}

	private String bodyURI(String uri){
		if(uri==null || uri.indexOf("?")==-1) return uri;
		return uri.substring(0,uri.indexOf("?"));
	}
	

	public HashMap<String,info_stream> get_streams() {
		return _streams;
	}



	public HashMap<String,info_bean> get_beans() {
		return _beans;
	}


	public HashMap<String,info_redirect> get_redirects() {
		return _redirects;
	}


	public HashMap<String,info_transformation> get_transformationoutput() {
		return _transformationoutput;
	}


	public HashMap<String,info_action>  get_actions() {
		return _actions;
	}


	public String getError() {
		return error;
	}


	public String getAuth_error() {
		return auth_error;
	}


	public String getSession_error() {
		return session_error;
	}


	public HashMap<String,info_redirect> get_redirectsjustloaded() {
		return _redirectsjustloaded;
	}


	public String getListener_actions() {
		return listener_actions;
	}


	public String getListener_beans() {
		return listener_beans;
	}


	public String getListener_streams() {
		return listener_streams;
	}


	public String getMemoryInContainer_streams() {
		return memoryInContainer_streams;
	}

	public String getProvider() {
		return provider;
	}
	
	public String getInstance_navigated() {
		return instance_navigated;
	}

	public String getInstance_local_container() {
		return instance_local_container;
	}


	public String getInstance_onlysession() {
		return instance_onlysession;
	}
	
	public String getInstance_servletcontext() {
		return instance_servletcontext;
	}


	public String getInstance_scheduler_container() {
		return instance_scheduler_container;
	}






}
