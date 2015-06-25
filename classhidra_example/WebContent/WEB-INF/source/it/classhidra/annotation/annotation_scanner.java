package it.classhidra.annotation;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.NavigatedScoped;
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
import java.util.Map;
import java.util.Vector;



public class annotation_scanner implements i_annotation_scanner {
	
	protected HashMap _actions = new HashMap();
	protected HashMap _streams = new HashMap();


	protected HashMap _beans = new HashMap();
	protected HashMap _redirects = new HashMap();
	protected HashMap _redirectsjustloaded = new HashMap();
	protected HashMap _transformationoutput = new HashMap();
	
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

	

	protected String package_annotated = "";
	protected File directory;
	
	
	public annotation_scanner(){
		super();
	}


	public void loadAllObjects(String _package_annotated,HashMap redirects){
		if(redirects!=null) _redirects=redirects;
		package_annotated=_package_annotated;
		bsController.writeLog("Start Load_actions from "+package_annotated,iStub.log_INFO);
		loadObject();
		bsController.writeLog("Load_actions from "+package_annotated+" OK ",iStub.log_INFO);
	}
	


	public void loadAllObjects(HashMap redirects){
		


		
		if(redirects!=null) _redirects=redirects;
		
		List list_package_annotated = bsController.getAppInit().get_list_package_annotated();
				
		
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
						directory = util_classes.convertUrl2File(resource);
					}catch(Exception ex){
						new bsException("Load_actions ClassLoader Error Annotation scaner: "+ex.toString(), iStub.log_ERROR);
					}catch(Throwable t){
						new bsException("Load_actions ClassLoader Error Annotation scaner: "+t.toString(), iStub.log_ERROR);
					}
					
					if(directory==null) return;
					

					
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
					
				}catch(Exception e){
					new bsException("Load_actions Loader Error Annotation scaner: "+e.toString(), iStub.log_ERROR);
				}catch(Throwable t){
					new bsException("Load_actions Loader Error Annotation scaner: "+t.toString(), iStub.log_ERROR);
				}
			}
	}
	
	
	private List checkBranch(String path){
		List array = new ArrayList();
		try{
			array = util_classes.getResourcesAsFile(path);
		}catch(Exception e){
			array = new ArrayList();
		}

		for(int i=0;i<array.size();i++){
			File current =  (File)array.get(i);
			String package_path = current.getAbsolutePath().replace(directory.getAbsolutePath(), "").replace("/", ".").replace("\\", ".");
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



	public void checkClassAnnotation(String class_path) {
		try{
			Class classType = Class.forName(class_path);
			
			Map subAnnotations = new HashMap();
			Annotation subAnnotation = classType.getAnnotation(NavigatedScoped.class);
				if(subAnnotation!=null){
					checkClassAnnotation(class_path, subAnnotation, subAnnotations);
					subAnnotations.put("NavigatedScoped", subAnnotation);
				}
	
			ActionMapping annotationActionMapping = (ActionMapping)classType.getAnnotation(ActionMapping.class);
			if(annotationActionMapping!=null){
				error=annotationActionMapping.error();
				session_error=annotationActionMapping.session_error();
				auth_error=annotationActionMapping.auth_error();

				listener_actions=annotationActionMapping.listener_actions();
				listener_beans=annotationActionMapping.listener_beans();
				listener_streams=annotationActionMapping.listener_streams();
				memoryInContainer_streams=annotationActionMapping.memoryInContainer_streams();
				provider=annotationActionMapping.provider();
				instance_navigated=annotationActionMapping.instance_navigated();
				instance_local_container=annotationActionMapping.instance_local_container();
				
				Stream[] streams = annotationActionMapping.streams();
				if(streams!=null && streams.length>0){
					for(int i=0;i<streams.length;i++)
						checkClassAnnotation(class_path, streams[i], subAnnotations);
				}
				Bean[] beans = annotationActionMapping.beans();
				if(beans!=null && beans.length>0){
					for(int i=0;i<beans.length;i++)
						checkClassAnnotation(class_path, beans[i], subAnnotations);
				}
				Redirect[] redirects = annotationActionMapping.redirects();
				if(redirects!=null && redirects.length>0){
					for(int i=0;i<redirects.length;i++)
						checkClassAnnotation(class_path, redirects[i], subAnnotations);
				}
				Action[] actions = annotationActionMapping.actions();
				if(actions!=null && actions.length>0){
					for(int i=0;i<actions.length;i++)
						checkClassAnnotation(class_path, actions[i], subAnnotations);
				}
				Transformation[] transformations = annotationActionMapping.transformations();
				if(transformations!=null && transformations.length>0){
					for(int i=0;i<transformations.length;i++)
						checkClassAnnotation(class_path, transformations[i], subAnnotations);
				}
			}
			

				
				
			Annotation annotation = classType.getAnnotation(Bean.class);
				if(annotation!=null) checkClassAnnotation(class_path, annotation, subAnnotations);
			annotation = classType.getAnnotation(Action.class);
				if(annotation!=null) checkClassAnnotation(class_path, annotation, subAnnotations);
			annotation = classType.getAnnotation(Stream.class);
				if(annotation!=null) checkClassAnnotation(class_path, annotation, subAnnotations);
			annotation = classType.getAnnotation(Transformation.class);
				if(annotation!=null) checkClassAnnotation(class_path, annotation, subAnnotations);
		    
		    
		}catch(Exception e){			
		}
	    
	}
	
	private void checkClassAnnotation(String class_path, Annotation annotation, Map subAnnotations) {
		try{
			
		    Bean annotationBean = null;
		    Action annotationAction = null;
		    Stream annotationStream = null;
		    Transformation annotationTransformation = null;
		    Redirect annotationRedirect = null;
		    NavigatedScoped navigatedScoped = null;


		    if(annotation instanceof Bean) annotationBean = (Bean)annotation;
		    if(annotation instanceof Redirect) annotationRedirect = (Redirect)annotation;
		    if(annotation instanceof Action) annotationAction = (Action)annotation;
		    if(annotation instanceof Stream) annotationStream = (Stream)annotation;
		    if(annotation instanceof Transformation) annotationTransformation = (Transformation)annotation;
		    if(annotation instanceof NavigatedScoped) navigatedScoped = (NavigatedScoped)annotation;
	
	
		    if (annotationBean != null) {
		    	info_bean iBean = new info_bean();
		    	iBean.setName(annotationBean.name());
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

    				iRedirect.getV_info_sections().addAll(new Vector(iRedirect.get_sections().values()));
    				iRedirect.setV_info_sections(new util_sort().sort(iRedirect.getV_info_sections(),"int_order"));

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

    					setEntity(iTransformationoutput,annotationTransf.entity());
    					if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(j+1).toString());
    					iTransformationoutput.setAnnotationLoaded(true);
    					iRedirect.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
    				}
    				
    				iRedirect.getV_info_transformationoutput().addAll(new Vector(iRedirect.get_transformationoutput().values()));
    				iRedirect.setV_info_transformationoutput(new util_sort().sort(iRedirect.getV_info_transformationoutput(),"int_order"));

    			}
    			_redirects.put(bodyURI(iRedirect.getPath()),iRedirect);
    			_redirectsjustloaded.put(bodyURI(iRedirect.getPath()),iRedirect);
		    	
		    }
		    
		    if (annotationAction != null) {
		    	info_action iAction = new info_action();
		    	iAction.setPath(annotationAction.path());
		    	iAction.setType(class_path);
		    	iAction.setName(annotationAction.name());
		    	iAction.setRedirect(annotationAction.redirect());
		    	iAction.setError(annotationAction.error());
		    	iAction.setMemoryInSession(annotationAction.memoryInSession());
		    	iAction.setMemoryAsLastInstance(annotationAction.memoryAsLastInstance());
		    	iAction.setReloadAfterAction(annotationAction.reloadAfterAction());
		    	iAction.setReloadAfterNextNavigated(annotationAction.reloadAfterNextNavigated());
		    	iAction.setNavigated(annotationAction.navigated());
		    	
		    	if(!iAction.getNavigated().trim().equalsIgnoreCase("true") && subAnnotations.get("NavigatedScoped")!=null)
		    		iAction.setNavigated("true");
		    		
		    	iAction.setSyncro(annotationAction.syncro());
		    	iAction.setStatistic(annotationAction.statistic());
		    	iAction.setHelp(annotationAction.help());
		    	iAction.setListener(annotationAction.listener());
		    	setEntity(iAction,annotationAction.entity());
		    	iAction.setAnnotationLoaded(true);
		    	Redirect[] redirects = annotationAction.redirects();
		    	if(redirects!=null && redirects.length>0){
		    		for(int i=0;i<redirects.length;i++){
		    			Redirect annotationRedirect1 = redirects[i];
		    			info_redirect iRedirect = new info_redirect();		    			
		    			iRedirect.setPath(annotationRedirect1.path());
		    			iRedirect.setAuth_id(annotationRedirect1.auth_id());
		    			iRedirect.setError(annotationRedirect1.error());
		    			iRedirect.setDescr(annotationRedirect1.descr());
		    			iRedirect.setMess_id(annotationRedirect1.mess_id());
		    			iRedirect.setUnited_id(annotationRedirect1.united_id());
		    			iRedirect.setImg(annotationRedirect1.img());
		    			iRedirect.setNavigated(annotationRedirect1.navigated());
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
		    					setEntity(iSection,annotationSection.entity());
		    					if(iSection.getOrder().equals("")) iSection.setOrder(Integer.valueOf(j+1).toString());
		    					iSection.setAnnotationLoaded(true);
		    					iRedirect.get_sections().put(iSection.getName(),iSection);
		    				}

		    				iRedirect.getV_info_sections().addAll(new Vector(iRedirect.get_sections().values()));
		    				iRedirect.setV_info_sections(new util_sort().sort(iRedirect.getV_info_sections(),"int_order"));

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
		    					setEntity(iTransformationoutput,annotationTransf.entity());
		    					if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(j+1).toString());
		    					iTransformationoutput.setAnnotationLoaded(true);
		    					iRedirect.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
		    				}
		    				
		    				iRedirect.getV_info_transformationoutput().addAll(new Vector(iRedirect.get_transformationoutput().values()));
		    				iRedirect.setV_info_transformationoutput(new util_sort().sort(iRedirect.getV_info_transformationoutput(),"int_order"));

		    			}
		    			iAction.get_redirects().put(bodyURI(iRedirect.getPath()),iRedirect);
		    		}
		    	}
		    	
		    	if(iAction.get_redirects().size()>0){
		    		iAction.getV_info_redirects().addAll(new Vector(iAction.get_redirects().values()));
		    		iAction.setV_info_redirects(new util_sort().sort(iAction.getV_info_redirects(),"int_order"));
		    		
		    		Object[] keys = iAction.get_redirects().keySet().toArray();
		    		for (int i = 0; i < keys.length; i++){
		    			info_redirect iRedirect = (info_redirect)iAction.get_redirects().get((String)keys[i]);
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
    					setEntity(iTransformationoutput,annotationTransf.entity());
    					if(iTransformationoutput.getOrder().equals("")) iTransformationoutput.setOrder(Integer.valueOf(i+1).toString());
    					iTransformationoutput.setAnnotationLoaded(true);
    					iAction.get_transformationoutput().put(iTransformationoutput.getName(),iTransformationoutput);
    				}
    				
    				iAction.getV_info_transformationoutput().addAll(new Vector(iAction.get_transformationoutput().values()));
    				iAction.setV_info_transformationoutput(new util_sort().sort(iAction.getV_info_transformationoutput(),"int_order"));
    			}

    			ActionCall[] calls = annotationAction.calls();
    			if(calls!=null && calls.length>0){
    				for(int i=0;i<calls.length;i++){
    					ActionCall annotationCall = calls[i];
    					info_call iCall = new info_call();
    					iCall.setName(annotationCall.name());
    					iCall.setMethod(annotationCall.method());
    					iCall.setNavigated(annotationCall.navigated());
    					setEntity(iCall,annotationCall.entity());
    					if(iCall.getOrder().equals("")) iCall.setOrder(Integer.valueOf(i+1).toString());
    					iCall.setAnnotationLoaded(true);
    					iAction.get_calls().put(iCall.getName(),iCall);
    				}
    				iAction.getV_info_calls().addAll(new Vector(iAction.get_calls().values()));
    				iAction.setV_info_calls(new util_sort().sort(iAction.getV_info_calls(),"int_order"));
    			}
    			
		    	_actions.put(iAction.getPath(),iAction);
		    }
	
		    if (annotationTransformation != null) {
				info_transformation iTransformationoutput = new info_transformation();
				iTransformationoutput.setName(annotationTransformation.name());
				iTransformationoutput.setType(class_path);
				iTransformationoutput.setPath(annotationTransformation.path());
				iTransformationoutput.setEvent(annotationTransformation.event());
				iTransformationoutput.setInputformat(annotationTransformation.inputformat());

				setEntity(iTransformationoutput,annotationTransformation.entity());
				iTransformationoutput.setAnnotationLoaded(true);
				_transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
		    	
		    }
		    
		    if (annotationStream != null) {
		    	info_stream iStream = new info_stream();
		    	iStream.setName(annotationStream.name());
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
		    		iStream.getV_info_apply_to_action().addAll(new Vector(iStream.get_apply_to_action().values()));
		    		iStream.setV_info_apply_to_action(new util_sort().sort(iStream.getV_info_apply_to_action(),"int_order"));
		    	}
		    	_streams.put(iStream.getName(),iStream);
		    }
		    
		    
		}catch(Exception e){			
		}
	    
	}
	
	private void setEntity(info_entity iEntity, Entity entity){
		if(iEntity==null || entity==null) return;
		iEntity.setProvider(entity.provider());
		iEntity.setProperty(entity.property());
		iEntity.setOrder(entity.order());
		iEntity.setComment(entity.comment());
		iEntity.setSystem(entity.system());
		iEntity.setAnnotated(entity.annotated());
	}

	private String bodyURI(String uri){
		if(uri==null || uri.indexOf("?")==-1) return uri;
		return uri.substring(0,uri.indexOf("?"));
	}
	

	public HashMap get_streams() {
		return _streams;
	}



	public HashMap get_beans() {
		return _beans;
	}


	public HashMap get_redirects() {
		return _redirects;
	}


	public HashMap get_transformationoutput() {
		return _transformationoutput;
	}


	public HashMap get_actions() {
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


	public HashMap get_redirectsjustloaded() {
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



}
