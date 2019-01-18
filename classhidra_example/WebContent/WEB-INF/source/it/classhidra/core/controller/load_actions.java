/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;

import it.classhidra.annotation.annotation_scanner;
import it.classhidra.annotation.i_annotation_scanner;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_find;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_sort;
import it.classhidra.core.tool.util.util_xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class load_actions extends elementBase{



	private static final long serialVersionUID = 6337896256233259229L;

	private String error;
	private String auth_error;
	private String session_error;

	private String listener_actions;
	private String listener_beans;
	private String listener_streams;
	private String memoryInContainer_streams;
	private String provider;
	private String instance_navigated;
	private String instance_local_container;
	private String instance_scheduler_container;
	private String instance_onlysession;
	private String instance_servletcontext;


	private Vector  v_info_actions;
	private Vector  v_info_streams;
	private Vector  v_info_beans;
	private Vector  v_info_redirects;
	private Vector  v_info_transformationoutput;


	private static HashMap _actions;
	private static HashMap _actioncalls;
	private static HashMap _restmapping;
	private static HashMap _streams;
	private static HashMap _streams_apply_to_actions;

	private static HashMap _beans;
	private static HashMap _redirects;
	private static HashMap _transformationoutput;
	
	

	private boolean readDef;

	private boolean readOk_Resource=false;
	private boolean readOk_Folder=false;
	private boolean readOk_File=false;
	private boolean readOk_Db=false;
	private boolean readOk_ExtLoader=false;
	private boolean reimposted = false;

	private String externalloader;
	private String xmlEncoding;

	private String loadedFrom="";

	private load_actions_builder builder=null;


public load_actions(){
	super();
	reimposta();

}

public void init() throws bsControllerException{

	String app_path="";
	app_init ainit = bsController.getAppInit(); 
	
	if(ainit.get_external_loader()!=null && !ainit.get_external_loader().equals("")){
		try{ 
			i_externalloader extl = (i_externalloader)util_provider.getInstanceFromProvider(
					new String[]{
							getProvider(),
							bsController.getAppInit().get_context_provider(),
							bsController.getAppInit().get_cdi_provider(),
							bsController.getAppInit().get_ejb_provider()
					},
					ainit.get_external_loader());
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_actions from "+ainit.get_external_loader()+" ERROR "+e.toString(),iStub.log_WARN);
		}catch(Throwable t){
			bsController.writeLog("Load_actions from "+ainit.get_external_loader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}


	if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						this.getExternalloader());
			extl.load();
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_actions from "+this.getExternalloader()+" ERROR "+e.toString(),iStub.log_WARN);
		}catch(Throwable t){
			bsController.writeLog("Load_actions from "+this.getExternalloader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}	
	

	try{
		
		if(ainit.get_db_name()!=null && ainit.isDb_name_valid()){
			if(initDB(ainit)){
				loadedFrom=ainit.get_db_name();
				readOk_Db=true;
				return;
			}
		}

		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
	}catch(Exception e){
	}

	String xml_name = System.getProperty(
			(ainit.getSynonyms_path().getProperty(bsController.actions_id_xml)==null)?bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(bsController.actions_id_xml)
		);
	if(xml_name==null || xml_name.equals("")) 
		xml_name = System.getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml)==null)?app_path+bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(bsController.actions_id_xml)==null)?bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(bsController.actions_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml)==null)?app_path+bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml)
			);


	if(xml_name!=null && !xml_name.equals("")){
		initProperties(xml_name);
	}


}


public void reInit(i_externalloader _externalloader){
	if(_externalloader==null) return;
	boolean loaded = false;
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_actions)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_actions) instanceof HashMap){
		_actions.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_actions));
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_actioncalls)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_actioncalls) instanceof HashMap){
		_actioncalls.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_actioncalls));
		loaded=true;
	}	
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_restmapping)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_restmapping) instanceof HashMap){
		_actioncalls.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_restmapping));
		loaded=true;
	}		
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_streams)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_streams) instanceof HashMap){
		_streams.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_streams));
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_streams_apply_to_actions)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_streams_apply_to_actions) instanceof HashMap){
		_streams_apply_to_actions.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_streams_apply_to_actions));
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_beans)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_beans) instanceof HashMap){
		_beans.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_beans));
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_redirects)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_redirects) instanceof HashMap){
		_redirects.putAll((HashMap)_externalloader.getProperty(i_externalloader.ACTIONS_redirects));
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_error)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_error) instanceof String){
		error=(String)_externalloader.getProperty(i_externalloader.ACTIONS_error);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_auth_error)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_auth_error) instanceof String){
		auth_error=(String)_externalloader.getProperty(i_externalloader.ACTIONS_auth_error);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_session_error)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_session_error) instanceof String){
		session_error=(String)_externalloader.getProperty(i_externalloader.ACTIONS_session_error);
		loaded=true;
	}
	
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_listener_actions)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_listener_actions) instanceof String){
		listener_actions=(String)_externalloader.getProperty(i_externalloader.ACTIONS_listener_actions);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_listener_beans)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_listener_beans) instanceof String){
		listener_beans=(String)_externalloader.getProperty(i_externalloader.ACTIONS_listener_beans);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_listener_streams)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_listener_streams) instanceof String){
		listener_streams=(String)_externalloader.getProperty(i_externalloader.ACTIONS_listener_streams);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_memoryInContainer_streams)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_memoryInContainer_streams) instanceof String){
		memoryInContainer_streams=(String)_externalloader.getProperty(i_externalloader.ACTIONS_memoryInContainer_streams);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_provider)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_provider) instanceof String){
		provider=(String)_externalloader.getProperty(i_externalloader.ACTIONS_provider);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_instance_navigated)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_instance_navigated) instanceof String){
		instance_navigated=(String)_externalloader.getProperty(i_externalloader.ACTIONS_instance_navigated);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_instance_local_container)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_instance_local_container) instanceof String){
		instance_local_container=(String)_externalloader.getProperty(i_externalloader.ACTIONS_instance_local_container);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_instance_scheduler_container)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_instance_scheduler_container) instanceof String){
		instance_scheduler_container=(String)_externalloader.getProperty(i_externalloader.ACTIONS_instance_scheduler_container);
		loaded=true;
	}	
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_instance_onlysession)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_instance_onlysession) instanceof String){
		instance_onlysession=(String)_externalloader.getProperty(i_externalloader.ACTIONS_instance_onlysession);
		loaded=true;
	}
	if(	_externalloader.getProperty(i_externalloader.ACTIONS_instance_servletcontext)!=null &&
		_externalloader.getProperty(i_externalloader.ACTIONS_instance_servletcontext) instanceof String){
		instance_servletcontext=(String)_externalloader.getProperty(i_externalloader.ACTIONS_instance_servletcontext);
		loaded=true;
	}
	
	
	
	
	if(loaded)
		loadedFrom+=" "+_externalloader.getClass().getName();
	
	v_info_streams = (new Vector(_streams.values()));
	v_info_streams = new util_sort().sort(v_info_streams,"int_order");

	v_info_actions = (new Vector(_actions.values()));
	v_info_actions = new util_sort().sort(v_info_actions,"int_order");
	
	v_info_beans = (new Vector(_beans.values()));
	v_info_beans = new util_sort().sort(v_info_beans,"int_order");

	v_info_redirects = (new Vector(_redirects.values()));
	v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");

	v_info_transformationoutput = (new Vector(_transformationoutput.values()));
	v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
//	readOk_ExtLoader=true;
	

}

public void reimposta(){

	if(v_info_actions==null) v_info_actions=new Vector();
	if(v_info_streams==null) v_info_streams=new Vector();
	if(v_info_beans==null) v_info_beans=new Vector();
	if(v_info_redirects==null) v_info_redirects=new Vector();
	if(v_info_transformationoutput==null) v_info_transformationoutput=new Vector();

	if(_actions==null) _actions = new HashMap();
	if(_actioncalls==null) _actioncalls = new HashMap();
	if(_restmapping==null) _restmapping = new HashMap();
	if(_streams==null){
		_streams = new HashMap();
		readDef = false;
	}else readDef = true;
	if(_streams_apply_to_actions==null){
		_streams_apply_to_actions = new HashMap();
		_streams_apply_to_actions.put("*",new Vector());
	}
	if(_beans==null) _beans = new HashMap();
	if(_redirects==null) _redirects = new HashMap();
	if(_transformationoutput==null) _transformationoutput = new HashMap();


	if(error==null) error="";
	if(auth_error==null) auth_error="";
	if(session_error==null) session_error="";
	if(listener_actions==null) listener_actions="";
	if(listener_beans==null) listener_beans="";
	if(listener_streams==null) listener_streams="";
	if(memoryInContainer_streams==null) memoryInContainer_streams="";
	if(provider==null) provider="";
	if(instance_navigated==null) instance_navigated="";
	if(instance_local_container==null) instance_local_container="";
	if(instance_scheduler_container==null) instance_scheduler_container="";
	if(instance_onlysession==null) instance_onlysession="";
	if(instance_servletcontext==null) instance_servletcontext="";
	readOk_Resource=false;
	readOk_Folder=false;
	readOk_File=false;
	readOk_Db=false;
	readOk_ExtLoader=false;
	externalloader="";
	xmlEncoding="";
	loadedFrom="";

//	bsController.removeFromLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE);
//	bsController.removeFromLocalContainer(bsConstants.CONST_CONTAINER_REFMETHODS);
	
	reimposted = true;
	if(!readDef){
		load_def_actions();
	}

}

public load_actions_builder initBuilder(String xml) throws bsControllerException{
	try{
		load_actions_builder _builder = new load_actions_builder();
		_builder.builder_init(xml);
		this.builder=_builder;
	}catch(Exception e){
		throw new bsControllerException(e, iStub.log_ERROR);
	}
	return this.builder;
}

public void syncroWithBuilder(){
	if(builder!=null) builder.syncroWithBuilder();

}

public boolean initProperties(String xml) throws bsControllerException{
	try{
		if(initWithFile(xml)){
			readOk_File = true;
			loadedFrom+=" "+xml;
			return true;
		}else{
			readOk_File = false;
			return false;
		}
	}catch(Exception e){
		readOk_File=false;
	}
	return false;
}

public void initWithFOLDER(String dir) throws bsControllerException{
	File input = new File(dir);

	if(input.exists()){
		File[] list = input.listFiles();
		for(int i=0;i<list.length;i++){
			try{
				boolean res = initWithFile(list[i].getAbsolutePath());
				if(res) bsController.writeLog("Load_action from "+input.getAbsolutePath()+" OK ",iStub.log_INFO);
				readOk_File=readOk_File || res;
			}catch(Exception e){
			}
		}

	}
	if(isReadOk()) loadedFrom+=" "+dir;
}


public void load_def_actions() {
	String property_name = "resources/"+ bsController.CONST_XML_ACTIONS;

	InputStream is = null;
    BufferedReader br = null;
    String result=null;
    String line="";


    try {
    	is = this.getClass().getResourceAsStream(property_name);
    	if(is!=null){
    		result="";
	    	br = new BufferedReader(new InputStreamReader(is));
	    	while (null != (line = br.readLine())) {
	    		result+=(line+"\n");
	    	}
    	}
    }catch (Exception e) {
    }finally {
    	try {
    		if (br != null) br.close();
    		if (is != null) is.close();
    	}catch (Exception e) {
    	}
	}

    try{
    	if(result!=null){
    		initWithData(result);
    		readDef=true;
    		loadedFrom+=" "+property_name;
    		bsController.writeLog("Load_actions from "+property_name+" OK ",iStub.log_INFO);

    	}
    }catch (Exception e) {
    	readDef=false;
	}
    loadFromAnnotations();
}

public void loadFromAnnotations(){
	
	if(_actions==null) _actions = new HashMap();
	if(_actioncalls==null) _actioncalls = new HashMap();
	if(_restmapping==null) _restmapping = new HashMap();
	if(_streams==null){
		_streams = new HashMap();
		readDef = false;
	}
	if(_streams_apply_to_actions==null){
		_streams_apply_to_actions = new HashMap();
		_streams_apply_to_actions.put("*",new Vector());
	}
	if(_beans==null) _beans = new HashMap();
	if(_redirects==null) _redirects = new HashMap();

	app_init ainit = bsController.getAppInit();
	i_annotation_scanner l_annotated = null;
	
	if(ainit.get_annotation_scanner()==null || ainit.get_annotation_scanner().equals("")){
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						annotation_scanner.class.getName());	
		}catch(Exception e){
			l_annotated = new annotation_scanner();
		}
	}else{
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
								},
						ainit.get_annotation_scanner());
		}catch(Exception e){
			new bsException("Load Error Annotation scaner: "+ainit.get_annotation_scanner() + " WARNING "+e.toString(), iStub.log_WARN);
			new bsException("Loading Default Annotation", iStub.log_INFO);
			l_annotated = new annotation_scanner();
		}
	}
	
	if(l_annotated==null){
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						annotation_scanner.class.getName());	
		}catch(Exception e){
			l_annotated = new annotation_scanner();
		}
	}
	bsController.writeLog("Start Load_actions with Annotation scaner: "+l_annotated.getClass().getName(),iStub.log_INFO);
	l_annotated.loadAllObjects(_redirects);
	
	if(l_annotated.getError()!=null && !l_annotated.getError().equals(""))
		error = l_annotated.getError();
	if(l_annotated.getSession_error()!=null && !l_annotated.getSession_error().equals(""))
		session_error = l_annotated.getSession_error();	
	if(l_annotated.getAuth_error()!=null && !l_annotated.getAuth_error().equals(""))
		auth_error = l_annotated.getAuth_error();	
	if(l_annotated.getListener_actions()!=null && !l_annotated.getListener_actions().equals(""))
		listener_actions = l_annotated.getListener_actions();
	if(l_annotated.getListener_beans()!=null && !l_annotated.getListener_beans().equals(""))
		listener_beans = l_annotated.getListener_beans();
	if(l_annotated.getListener_streams()!=null && !l_annotated.getListener_streams().equals(""))
		listener_streams = l_annotated.getListener_streams();
	if(l_annotated.getMemoryInContainer_streams()!=null && !l_annotated.getMemoryInContainer_streams().equals(""))
		memoryInContainer_streams = l_annotated.getMemoryInContainer_streams();
	if(l_annotated.getProvider()!=null && !l_annotated.getProvider().equals(""))
		provider = l_annotated.getProvider();	
	if(l_annotated.getInstance_navigated()!=null && !l_annotated.getInstance_navigated().equals(""))
		instance_navigated = l_annotated.getInstance_navigated();
	if(l_annotated.getInstance_local_container()!=null && !l_annotated.getInstance_local_container().equals(""))
		instance_local_container = l_annotated.getInstance_local_container();	
	if(l_annotated.getInstance_scheduler_container()!=null && !l_annotated.getInstance_scheduler_container().equals(""))
		instance_scheduler_container = l_annotated.getInstance_scheduler_container();	
	if(l_annotated.getInstance_onlysession()!=null && !l_annotated.getInstance_onlysession().equals(""))
		instance_onlysession = l_annotated.getInstance_onlysession();	
	if(l_annotated.getInstance_servletcontext()!=null && !l_annotated.getInstance_servletcontext().equals(""))
		instance_servletcontext = l_annotated.getInstance_servletcontext();
	
	Vector a_streams = new Vector(l_annotated.get_streams().values());
	if(a_streams.size()>0){
		int stream_order=0;
		HashMap _streams_order = new HashMap();
		for(int k=0;k<a_streams.size();k++){
			info_stream iStream = (info_stream)a_streams.get(k);
			info_stream old_stream = (info_stream)_streams.get(iStream.getName());
			if(old_stream!=null){
				_streams.remove(old_stream.getName());
				_streams_order.remove(Integer.valueOf(old_stream.getInt_order()));
				info_stream fromVinfo = (info_stream)util_find.findElementFromList(v_info_streams, old_stream.getName(), "name");
				if(fromVinfo!=null)
					v_info_streams.remove(fromVinfo);
				Vector app_action = new Vector(old_stream.get_apply_to_action().keySet());
				if(app_action.size()==0){
					int l=0;
					while(l< ((Vector)_streams_apply_to_actions.get("*")).size()){
						info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get("*")).get(l);
						if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get("*")).remove(l);
						else l++;
					}
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						int l=0;
						while(l< ((Vector)_streams_apply_to_actions.get(key)).size()){
							info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get(key)).get(l);
							if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get(key)).remove(l);
							else l++;
						}
					}
				}
				
			}
			_streams.put(iStream.getName(),iStream);
			if(iStream.getInt_order()==-1){
				while(_streams_order.get(Integer.valueOf(stream_order))!=null) stream_order++;
				_streams_order.put(Integer.valueOf(stream_order) , iStream.getName());
				stream_order++;
			}else{
				_streams_order.put(Integer.valueOf(iStream.getInt_order()) , iStream.getName());
				stream_order=iStream.getInt_order();
				stream_order++;
			}
		}
	
		Vector v_streams_order = new util_sort().sort(new  Vector(_streams_order.keySet()),"");
		for(int i=0;i<v_streams_order.size();i++){
			info_stream current = (info_stream)_streams.get(_streams_order.get(v_streams_order.get(i)));
			if(current!=null){
				Vector app_action = new Vector(current.get_apply_to_action().keySet());
				if(app_action.size()==0){
					String key="*";
					((Vector)_streams_apply_to_actions.get(key)).add(current);
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						if(_streams_apply_to_actions.get(key)==null) _streams_apply_to_actions.put(key,new Vector());
						((Vector)_streams_apply_to_actions.get(key)).add(current);
					}
				}
			}
		}
		
		//v_info_streams.addAll(new Vector(_streams.values()));
		v_info_streams = (new Vector(_streams.values()));
		v_info_streams = new util_sort().sort(v_info_streams,"int_order");
	}
	
	Vector a_beans = new Vector(l_annotated.get_beans().values());
	if(a_beans.size()>0){
		int max_int_order = -1;
		if(v_info_beans!=null && v_info_beans.size()>0){
			try{
				max_int_order = ((info_bean)v_info_beans.get(v_info_beans.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_beans = new util_sort().sort(a_beans,"int_order");
			
		for(int i=0;i<a_beans.size();i++){
			if(max_int_order>-1) ((info_bean)a_beans.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_beans.put(((info_bean)a_beans.get(i)).getName(), a_beans.get(i));
		}
		v_info_beans = (new Vector(_beans.values()));
		v_info_beans = new util_sort().sort(v_info_beans,"int_order");
	}
	
	
	Vector a_redirects = new Vector(l_annotated.get_redirects().values());
	if(a_redirects.size()>0){
		int max_int_order = -1;
		if(v_info_redirects!=null && v_info_redirects.size()>0){
			try{
				max_int_order = ((info_bean)v_info_redirects.get(v_info_redirects.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_redirects = new util_sort().sort(a_redirects,"int_order");
			
		for(int i=0;i<a_redirects.size();i++){
			if(max_int_order>-1) ((info_redirect)a_redirects.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_redirects.put(((info_redirect)a_redirects.get(i)).getPath(), a_redirects.get(i));
		}
		v_info_redirects = (new Vector(_redirects.values()));
		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");
	}

	Vector a_actions = new Vector(l_annotated.get_actions().values());
	if(a_actions.size()>0){
		int max_int_order = -1;
		if(v_info_actions!=null && v_info_actions.size()>0){
			try{
				max_int_order = ((info_action)v_info_actions.get(v_info_actions.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_actions = new util_sort().sort(a_actions,"int_order");
			
		for(int i=0;i<a_actions.size();i++){
			if(max_int_order>-1) ((info_action)a_actions.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_actions.put(((info_action)a_actions.get(i)).getPath(), a_actions.get(i));
			if(((info_action)a_actions.get(i)).get_calls().size()>0){
				Vector a_actioncalls = new Vector(((info_action)a_actions.get(i)).get_calls().values());
				for(int j=0;j<a_actioncalls.size();j++){
					if(((info_call)a_actioncalls.get(j)).getExposed()==null || ((info_call)a_actioncalls.get(j)).getExposed().size()==0){
						_actioncalls.put(
								((info_call)a_actioncalls.get(j)).getOwner()+
								((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
								((info_call)a_actioncalls.get(j)).getName(),
								a_actioncalls.get(j));
						if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
							_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getPath(),
									a_actioncalls.get(j));
					}else{
						for(int e=0;e<((info_call)a_actioncalls.get(j)).getExposed().size();e++){
							String suffix = "."+((info_call)a_actioncalls.get(j)).getExposed().get(e).toString();
							_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getOwner()+
									((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
									((info_call)a_actioncalls.get(j)).getName()+suffix,
									a_actioncalls.get(j));
							if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
								_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getPath()+suffix,
										a_actioncalls.get(j));

						}
					}
					
					if(((info_call)a_actioncalls.get(j)).getRestmapping()!=null && ((info_call)a_actioncalls.get(j)).getRestmapping().size()>0){
						for(int r=0;r<((info_call)a_actioncalls.get(j)).getRestmapping().size();r++){
							info_rest iRest = (info_rest)((info_call)a_actioncalls.get(j)).getRestmapping().get(r);
							if(_restmapping.get(iRest.getPath())==null)
								_restmapping.put(iRest.getPath(), new Vector());
							((Vector)_restmapping.get(iRest.getPath())).add(iRest);
						}
					}
				}
				
			}
			if(((info_action)a_actions.get(i)).getRestmapping().size()>0){
				for(int r=0;r<((info_action)a_actions.get(i)).getRestmapping().size();r++){
					info_rest iRest = (info_rest)((info_action)a_actions.get(i)).getRestmapping().get(r);
					if(_restmapping.get(iRest.getPath())==null)
						_restmapping.put(iRest.getPath(), new Vector());
					((Vector)_restmapping.get(iRest.getPath())).add(iRest);
				}
			}
		}
		v_info_actions = (new Vector(_actions.values()));
		v_info_actions = new util_sort().sort(v_info_actions,"int_order");
	}
	
	Vector a_transformations = new Vector(l_annotated.get_transformationoutput().values());
	if(a_transformations.size()>0){
	int max_int_order = -1;
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			try{
				max_int_order = ((info_transformation)v_info_transformationoutput.get(v_info_transformationoutput.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_transformations = new util_sort().sort(a_transformations,"int_order");
			
		for(int i=0;i<a_transformations.size();i++){
			if(max_int_order>-1) ((info_transformation)a_transformations.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_transformationoutput.put(((info_transformation)a_transformations.get(i)).getName(), a_transformations.get(i));
		}
		v_info_transformationoutput = (new Vector(_transformationoutput.values()));
		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
	}
	
	
}


public info_entity loadFromAnnotations(info_entity iEntity){
	

	app_init ainit = bsController.getAppInit();
	i_annotation_scanner l_annotated = null;
	
	if(ainit.get_annotation_scanner()==null || ainit.get_annotation_scanner().equals("")){
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						annotation_scanner.class.getName());	
		}catch(Exception e){
			l_annotated = new annotation_scanner();
		}
	}else{
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						ainit.get_annotation_scanner());
		}catch(Exception e){
			new bsException("Load Error Annotation scaner: "+ainit.get_annotation_scanner()+ " WARNING "+e.toString(), iStub.log_WARN);
			new bsException("Loading Default Annotation", iStub.log_INFO);
			l_annotated = new annotation_scanner();
		}
	}
	
	if(l_annotated==null){
		try{
			l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
						new String[]{
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						annotation_scanner.class.getName());	
		}catch(Exception e){
			l_annotated = new annotation_scanner();
		}
	}
	bsController.writeLog("Start Load_actions with Annotation scaner: "+l_annotated.getClass().getName(),iStub.log_INFO);
	
	if(iEntity instanceof info_action)
		l_annotated.loadAllObjects(((info_action)iEntity).getType()+".class", _redirects);
	else if(iEntity instanceof info_bean)
		l_annotated.loadAllObjects(((info_bean)iEntity).getType()+".class", _redirects);
	else if(iEntity instanceof info_stream)
		l_annotated.loadAllObjects(((info_stream)iEntity).getType()+".class", _redirects);
	else if(iEntity instanceof info_transformation)
		l_annotated.loadAllObjects(((info_transformation)iEntity).getType()+".class", _redirects);	
	else return null;
	
	iEntity.setAnnotationLoaded(true);
	
	if(l_annotated.getError()!=null && !l_annotated.getError().equals(""))
		error = l_annotated.getError();
	if(l_annotated.getSession_error()!=null && !l_annotated.getSession_error().equals(""))
		session_error = l_annotated.getSession_error();	
	if(l_annotated.getAuth_error()!=null && !l_annotated.getAuth_error().equals(""))
		auth_error = l_annotated.getAuth_error();	
	if(l_annotated.getListener_actions()!=null && !l_annotated.getListener_actions().equals(""))
		listener_actions = l_annotated.getListener_actions();
	if(l_annotated.getListener_beans()!=null && !l_annotated.getListener_beans().equals(""))
		listener_beans = l_annotated.getListener_beans();
	if(l_annotated.getListener_streams()!=null && !l_annotated.getListener_streams().equals(""))
		listener_streams = l_annotated.getListener_streams();	
	if(l_annotated.getMemoryInContainer_streams()!=null && !l_annotated.getMemoryInContainer_streams().equals(""))
		memoryInContainer_streams = l_annotated.getMemoryInContainer_streams();	
	if(l_annotated.getProvider()!=null && !l_annotated.getProvider().equals(""))
		provider = l_annotated.getProvider();	
	if(l_annotated.getInstance_navigated()!=null && !l_annotated.getInstance_navigated().equals(""))
		instance_navigated = l_annotated.getInstance_navigated();
	if(l_annotated.getInstance_local_container()!=null && !l_annotated.getInstance_local_container().equals(""))
		instance_local_container = l_annotated.getInstance_local_container();	
	if(l_annotated.getInstance_scheduler_container()!=null && !l_annotated.getInstance_scheduler_container().equals(""))
		instance_scheduler_container = l_annotated.getInstance_scheduler_container();	
	if(l_annotated.getInstance_onlysession()!=null && !l_annotated.getInstance_onlysession().equals(""))
		instance_onlysession = l_annotated.getInstance_onlysession();	
	if(l_annotated.getInstance_servletcontext()!=null && !l_annotated.getInstance_servletcontext().equals(""))
		instance_servletcontext = l_annotated.getInstance_servletcontext();
	

	Vector a_streams = new Vector(l_annotated.get_streams().values());
	if(a_streams.size()>0){
		int stream_order=0;
		HashMap _streams_order = new HashMap();
		for(int k=0;k<a_streams.size();k++){
			info_stream iStream = (info_stream)a_streams.get(k);
			info_stream old_stream = (info_stream)_streams.get(iStream.getName());
			if(old_stream!=null){
				_streams.remove(old_stream.getName());
				_streams_order.remove(Integer.valueOf(old_stream.getInt_order()));
				info_stream fromVinfo = (info_stream)util_find.findElementFromList(v_info_streams, old_stream.getName(), "name");
				if(fromVinfo!=null)
					v_info_streams.remove(fromVinfo);
				Vector app_action = new Vector(old_stream.get_apply_to_action().keySet());
				if(app_action.size()==0){
					int l=0;
					while(l< ((Vector)_streams_apply_to_actions.get("*")).size()){
						info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get("*")).get(l);
						if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get("*")).remove(l);
						else l++;
					}
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						int l=0;
						while(l< ((Vector)_streams_apply_to_actions.get(key)).size()){
							info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get(key)).get(l);
							if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get(key)).remove(l);
							else l++;
						}
					}
				}
				
			}
			_streams.put(iStream.getName(),iStream);
			if(iStream.getInt_order()==-1){
				while(_streams_order.get(Integer.valueOf(stream_order))!=null) stream_order++;
				_streams_order.put(Integer.valueOf(stream_order) , iStream.getName());
				stream_order++;
			}else{
				_streams_order.put(Integer.valueOf(iStream.getInt_order()) , iStream.getName());
				stream_order=iStream.getInt_order();
				stream_order++;
			}
		}
	
		Vector v_streams_order = new util_sort().sort(new  Vector(_streams_order.keySet()),"");
		for(int i=0;i<v_streams_order.size();i++){
			info_stream current = (info_stream)_streams.get(_streams_order.get(v_streams_order.get(i)));
			if(current!=null){
				Vector app_action = new Vector(current.get_apply_to_action().keySet());
				if(app_action.size()==0){
					String key="*";
					((Vector)_streams_apply_to_actions.get(key)).add(current);
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						if(_streams_apply_to_actions.get(key)==null) _streams_apply_to_actions.put(key,new Vector());
						((Vector)_streams_apply_to_actions.get(key)).add(current);
					}
				}
			}
		}
		
		//v_info_streams.addAll(new Vector(_streams.values()));
		v_info_streams = (new Vector(_streams.values()));
		v_info_streams = new util_sort().sort(v_info_streams,"int_order");
	}
	
	Vector a_beans = new Vector(l_annotated.get_beans().values());
	if(a_beans.size()>0){
		int max_int_order = -1;
		if(v_info_beans!=null && v_info_beans.size()>0){
			try{
				max_int_order = ((info_bean)v_info_beans.get(v_info_beans.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_beans = new util_sort().sort(a_beans,"int_order");
			
		for(int i=0;i<a_beans.size();i++){
			if(max_int_order>-1) ((info_bean)a_beans.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_beans.put(((info_bean)a_beans.get(i)).getName(), a_beans.get(i));
		}
		v_info_beans = (new Vector(_beans.values()));
		v_info_beans = new util_sort().sort(v_info_beans,"int_order");
	}
	
	
	
	Vector a_redirects_loaded = new Vector(l_annotated.get_redirectsjustloaded().values());
	if(a_redirects_loaded.size()>0){
		Vector a_redirects = new Vector(l_annotated.get_redirects().values());
		
		int max_int_order = -1;
		if(v_info_redirects!=null && v_info_redirects.size()>0){
			try{
				max_int_order = ((info_bean)v_info_redirects.get(v_info_redirects.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_redirects = new util_sort().sort(a_redirects,"int_order");
			
		for(int i=0;i<a_redirects.size();i++){
			if(max_int_order>-1) ((info_redirect)a_redirects.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_redirects.put(((info_redirect)a_redirects.get(i)).getPath(), a_redirects.get(i));
		}
		v_info_redirects = (new Vector(_redirects.values()));
		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");
	}


	Vector a_actions = new Vector(l_annotated.get_actions().values());
	if(a_actions.size()>0){
		int max_int_order = -1;
		if(v_info_actions!=null && v_info_actions.size()>0){
			try{
				max_int_order = ((info_action)v_info_actions.get(v_info_actions.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_actions = new util_sort().sort(a_actions,"int_order");
			
		for(int i=0;i<a_actions.size();i++){
			if(max_int_order>-1) ((info_action)a_actions.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_actions.put(((info_action)a_actions.get(i)).getPath(), a_actions.get(i));
			if(((info_action)a_actions.get(i)).get_calls().size()>0){
				Vector a_actioncalls = new Vector(((info_action)a_actions.get(i)).get_calls().values());
				for(int j=0;j<a_actioncalls.size();j++){
/*
					_actioncalls.put(
							((info_call)a_actioncalls.get(j)).getOwner()+
							((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
							((info_call)a_actioncalls.get(j)).getName(),
							a_actioncalls.get(j));
					if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
						_actioncalls.put(
								((info_call)a_actioncalls.get(j)).getPath(),
								a_actioncalls.get(j));
*/					
					if(((info_call)a_actioncalls.get(j)).getExposed()==null || ((info_call)a_actioncalls.get(j)).getExposed().size()==0){
						_actioncalls.put(
								((info_call)a_actioncalls.get(j)).getOwner()+
								((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
								((info_call)a_actioncalls.get(j)).getName(),
								a_actioncalls.get(j));
						if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
							_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getPath(),
									a_actioncalls.get(j));
					}else{
						for(int e=0;e<((info_call)a_actioncalls.get(j)).getExposed().size();e++){
							String suffix = "."+((info_call)a_actioncalls.get(j)).getExposed().get(e).toString();
							_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getOwner()+
									((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
									((info_call)a_actioncalls.get(j)).getName()+suffix,
									a_actioncalls.get(j));
							if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
								_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getPath()+suffix,
										a_actioncalls.get(j));

						}
					}
					if(((info_call)a_actioncalls.get(j)).getRestmapping()!=null && ((info_call)a_actioncalls.get(j)).getRestmapping().size()>0){
						for(int r=0;r<((info_call)a_actioncalls.get(j)).getRestmapping().size();r++){
							info_rest iRest = (info_rest)((info_call)a_actioncalls.get(j)).getRestmapping().get(r);
							if(_restmapping.get(iRest.getPath())==null)
								_restmapping.put(iRest.getPath(), new Vector());
							((Vector)_restmapping.get(iRest.getPath())).add(iRest);
						}
					}
				}
				
			}
			if(((info_action)a_actions.get(i)).getRestmapping().size()>0){
				for(int r=0;r<((info_action)a_actions.get(i)).getRestmapping().size();r++){
					info_rest iRest = (info_rest)((info_action)a_actions.get(i)).getRestmapping().get(r);
					if(_restmapping.get(iRest.getPath())==null)
						_restmapping.put(iRest.getPath(), new Vector());
					((Vector)_restmapping.get(iRest.getPath())).add(iRest);
				}
			}

		}
		v_info_actions = (new Vector(_actions.values()));
		v_info_actions = new util_sort().sort(v_info_actions,"int_order");
	}
	
	Vector a_transformations = new Vector(l_annotated.get_transformationoutput().values());
	if(a_transformations.size()>0){
		int max_int_order = -1;
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			try{
				max_int_order = ((info_transformation)v_info_transformationoutput.get(v_info_transformationoutput.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_transformations = new util_sort().sort(a_transformations,"int_order");
			
		for(int i=0;i<a_transformations.size();i++){
			if(max_int_order>-1) ((info_transformation)a_transformations.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_transformationoutput.put(((info_transformation)a_transformations.get(i)).getName(), a_transformations.get(i));
		}
		v_info_transformationoutput = (new Vector(_transformationoutput.values()));
		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
	}
	if(iEntity instanceof info_action)
		return (info_entity)_actions.get(((info_action)iEntity).getPath());
	else if(iEntity instanceof info_bean)
		return (info_entity)_beans.get(((info_bean)iEntity).getName());
	else if(iEntity instanceof info_stream)
		return (info_entity)_streams.get(((info_stream)iEntity).getName());
	else if(iEntity instanceof info_transformation)
		return (info_entity)_transformationoutput.get(((info_transformation)iEntity).getName());	
	else return null;
	
	
}


public void load_from_resources() {
	load_from_resources("/config/"+bsController.CONST_XML_ACTIONS);
	load_from_resources("/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);
	
	load_from_resources("META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);	
	load_from_resources("WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);
	
	
	String property_name =  "config."+bsController.CONST_XML_ACTIONS_FOLDER;


	try{
		List array = util_classes.getResources(property_name);
		for(int i=0;i<array.size();i++){
			String property_name0 =  bsController.CONST_XML_ACTIONS_FOLDER+"/"+array.get(i);
			if(property_name0!=null && property_name0.toLowerCase().indexOf(".xml")>-1)
				load_from_resources("/config/"+property_name0);
		}
	}catch(Exception e){
    	bsController.writeLog("Load_actions from resources Array KO "+e.toString(),iStub.log_WARN);
	}



}



private boolean load_from_resources(String property_name) {
	
	bsController.writeLog("Start Load_actions from "+property_name,iStub.log_INFO);


	InputStream is = null;
    BufferedReader br = null;
    String result=null;
    String line="";


    try {
    	is = getClass().getResourceAsStream(property_name);
    	if(is==null)
    		is = this.getClass().getClassLoader().getResourceAsStream(property_name);
    	if(is==null)
    		is = ClassLoader.getSystemClassLoader().getResourceAsStream(property_name);
    	if(is!=null){
    		result="";
	    	br = new BufferedReader(new InputStreamReader(is));
	    	while (null != (line = br.readLine())) {
	    		result+=(line+"\n");
	    	}
    	}
    }catch (Exception e) {
    	bsController.writeLog("Load_actions from "+property_name+" WARNING "+e.toString(),iStub.log_WARN);
    }finally {
    	try {
    		if (br != null) br.close();
    		if (is != null) is.close();
    	}catch (Exception e) {
    	}
	}

    if(result!=null){
	    try{
	    	if(initWithData(result)){
	    		bsController.writeLog("Load_actions from "+property_name+" OK ",iStub.log_INFO);
	    		readOk_Resource = readOk_Resource || true;
	    		loadedFrom+=" "+property_name;
	    		return true;
	    	}
		}catch(Exception e){
			bsController.writeLog("Load_actions from "+property_name+" WARNING "+e.toString(),iStub.log_WARN);
		}
    }
    else
    	bsController.writeLog("Load_actions from "+property_name+" KO ",iStub.log_INFO);
    
    return false;

}

public boolean load_from_resources(ServletContext ctx) {

	boolean read = load_from_resources(ctx,"/WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);
	if(!read) 
		read = load_from_resources(ctx,"/WEB-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);
	if(!read) 
		read = load_from_resources(ctx,"/META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);
	if(!read) 
		read = load_from_resources(ctx,"/META-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_ACTIONS);

	return read;
}

private boolean load_from_resources(ServletContext ctx, String property_name) {
	
	bsController.writeLog("Start Load_actions from "+property_name,iStub.log_INFO);


	InputStream is = null;
    BufferedReader br = null;
    String result=null;
    String line="";


    try {
    	is = ctx.getResourceAsStream(property_name);
      	if(is!=null){
    		result="";
	    	br = new BufferedReader(new InputStreamReader(is));
	    	while (null != (line = br.readLine())) {
	    		result+=(line+"\n");
	    	}
    	}
    }catch (Exception e) {
    	bsController.writeLog("Load_actions from "+property_name+" WARNING "+e.toString(),iStub.log_WARN);
    }finally {
    	try {
    		if (br != null) br.close();
    		if (is != null) is.close();
    	}catch (Exception e) {
    	}
	}

    if(result!=null){
	    try{
	    	if(initWithData(result)){
	    		bsController.writeLog("Load_actions from "+property_name+" OK ",iStub.log_INFO);
	    		readOk_Resource = readOk_Resource || true;
	    		loadedFrom+=" "+property_name;
	    		return true;
	    	}
		}catch(Exception e){
			bsController.writeLog("Load_actions from "+property_name+" WARNING "+e.toString(),iStub.log_WARN);
		}
    }
    
    return false;

}


public boolean initWithData(String _xml) throws bsControllerException, Exception{
		Document documentXML = null;
		documentXML = util_xml.readXMLData(_xml);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}
		if(readDocumentXml(documentXML)) return true;
		else return false;
}

public boolean initWithFile(String _xml) throws bsControllerException, Exception{
	Document documentXML = null;
	documentXML = util_xml.readXML(_xml);
	if(documentXML!=null){
		xmlEncoding = documentXML.getXmlEncoding();
		if(xmlEncoding==null) xmlEncoding="";
	}
	if(readDocumentXml(documentXML)) return true;
	else return false;
}

public boolean initDB(app_init ainit) throws bsControllerException, Exception{
	String app_path=ainit.get_path();
	if(app_path==null || app_path.equals("")) app_path="";
	else app_path+=".";
	
	String xmlData = null;
	boolean dbValid = false;
	
	try{
		xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml)==null)?app_path+bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.actions_id_xml),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

	}
	try{		
		if(xmlData==null) xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(bsController.actions_id_xml)==null)?bsController.actions_id_xml:ainit.getSynonyms_path().getProperty(bsController.actions_id_xml),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

	}
	if(!dbValid)
		ainit.setDb_name_valid(false);
	
	if(xmlData==null) return false;

	Document documentXML = null;
	documentXML = util_xml.readXMLData(xmlData);
	if(documentXML!=null){
		xmlEncoding = documentXML.getXmlEncoding();
		if(xmlEncoding==null) xmlEncoding="";
	}
	if(readDocumentXml(documentXML)) return true;
	else return false;


}

private boolean readDocumentXml(Document documentXML) throws Exception{
	if(documentXML!=null){
		if(_actions==null) _actions = new HashMap();
		if(_actioncalls==null) _actioncalls = new HashMap();
		if(_restmapping==null) _restmapping = new HashMap();
		if(_streams==null){
			_streams = new HashMap();
			readDef = false;
		}
		if(_streams_apply_to_actions==null){
			_streams_apply_to_actions = new HashMap();
			_streams_apply_to_actions.put("*",new Vector());
		}
		if(_beans==null) _beans = new HashMap();
		if(_redirects==null) _redirects = new HashMap();
		Node node = null;
		try{
			int first=0;
			while(node==null && first < documentXML.getChildNodes().getLength()){
				if(documentXML.getChildNodes().item(first).getNodeType()== Node.ELEMENT_NODE)
					node = documentXML.getChildNodes().item(first);
				first++;
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
		if(node==null) return false;
		readFormElements(node);
		for(int i=0;i<node.getChildNodes().getLength();i++){
			if(node.getChildNodes().item(i).getNodeType()== Node.ELEMENT_NODE)
				readFormElements(node.getChildNodes().item(i));
		}
		if(_actions!=null && _actions.get("*")!=null){
			Object[] keysIn = _actions.keySet().toArray();
			Object[] keysFor = (((info_action)_actions.get("*")).get_redirects()).keySet().toArray();
			for(int i=0;i<keysIn.length;i++){
				try{
					HashMap current_redirects = ((info_action)_actions.get((String)keysIn[i])).get_redirects();
					for(int j=0;j<keysFor.length;j++)
						current_redirects.put(keysFor[j],((((info_action)_actions.get("*")).get_redirects())).get(keysFor[j]));
				}catch(Exception e){
				}
			}
		}
	}else 
		return false;
	return true;
}







public i_action actionFactory(String id_action, ServletContext servletContext){
	return actionFactory(id_action,null,servletContext);
}
public i_action actionFactory(String id_action){
	return actionFactory(id_action,null,null);
}
public i_action actionFactory(String id_action, HttpSession session, ServletContext servletContext){
	i_action rAction = new action();
	info_action iAction = null;
	if(_actions==null) _actions = new HashMap();
	if(_actions.get(id_action)==null){
		load_actions session_l_actions = (load_actions)session.getAttribute(bsConstants.CONST_SESSION_ACTIONS_INSTANCE);
		if(session_l_actions!=null){
			try{
				iAction = (info_action)session_l_actions.getBuilder().get_b_actions().get(id_action);
			}catch(Exception e){
			}
		}
		if(iAction==null){
			iAction = new info_action();
			iAction.setPath(id_action);
			rAction.set_infoaction(iAction);
//			((bean)rAction).set_infobean(new info_bean());
			rAction.asBean().set_infobean(new info_bean());
			return rAction;
		}
	}else iAction = (info_action)_actions.get(id_action);

	info_bean iBean = (info_bean)_beans.get(iAction.getName());


	boolean loadedFromProvider=false;
	
	if(bsController.isCanBeProxed()){
//		if((((iAction.getProvider()==null)?"":iAction.getProvider().trim()) + ((getProvider()==null)?"":getProvider().trim()) + ((bsController.getAppInit().get_cdi_provider()==null)?"":bsController.getAppInit().get_cdi_provider().trim())).length()>0 || bsController.getCdiDefaultProvider()!=null || bsController.getEjbDefaultProvider()!=null){
			Object objFromProvider = util_provider.getBeanFromObjectFactory(
						new String[]{
								iAction.getProvider(),
								getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						iAction.getPath(),
						iAction.getType(),
						servletContext);
			
			if(objFromProvider!=null && objFromProvider instanceof i_info_context){
				i_info_context iContext= ((i_action)((i_info_context)objFromProvider).getProxy()).getInfo_context();
				if(iContext.isProxiedEjb())
					rAction = (i_action)((i_info_context)objFromProvider).getProxy();
				else{
					rAction = ((i_action)((i_info_context)objFromProvider).getProxy()).asAction();
					rAction.setInfo_context((i_info_context)objFromProvider);
				}
				rAction.set_bean(null);
				if(iBean!=null)
	//				((bean)rAction).set_infobean(iBean);
					rAction.asBean().set_infobean(iBean);
				loadedFromProvider=true;
			}
			if(objFromProvider!=null && objFromProvider instanceof i_action){
				rAction = (i_action)objFromProvider;
				if(iBean!=null)
	//				((bean)rAction).set_infobean(iBean);
					rAction.asBean().set_infobean(iBean);
				loadedFromProvider=true;
			}
			if(!loadedFromProvider && objFromProvider!=null && objFromProvider instanceof i_bean){
				rAction = ((i_bean)objFromProvider).asAction();
				if(iBean!=null)
	//				((bean)rAction).set_infobean(iBean);
					rAction.asBean().set_infobean(iBean);
				loadedFromProvider=true;
			}
			if(loadedFromProvider){
				if(rAction!=null && rAction.get_bean()!=null){
					rAction.get_bean().set_infoaction(iAction);
	//				if(rAction.getRealBean()!=null)
	//					rAction.getRealBean().setNavigable(false);
				}
				if(rAction!=null && rAction.getCurrent_redirect()!=null){
					rAction.setCurrent_redirect(null);
				}
			}		
//		}
	}
/*	
	if(iAction.getProvider()!=null && !iAction.getProvider().equals("")){
		Object actionFromProvider = util_reflect.providerObjectFactory(iAction.getProvider(), iAction.getPath(), iAction.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_action){
			rAction = (i_action)actionFromProvider;
			if(iBean!=null) ((bean)rAction).set_infobean(iBean);
			loadedFromProvider=true;
		}
	}else if(getProvider()!=null && !getProvider().equals("") && !getProvider().equalsIgnoreCase("false")){
		Object actionFromProvider = util_reflect.providerObjectFactory(getProvider(), iAction.getPath(), iAction.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_action){
			rAction = (i_action)actionFromProvider;
			if(iBean!=null) ((bean)rAction).set_infobean(iBean);
			loadedFromProvider=true;
		}
	}else if(bsController.getAppInit().get_cdi_provider()!=null && !bsController.getAppInit().get_cdi_provider().equals("")){
		Object actionFromProvider = util_reflect.providerObjectFactory(bsController.getAppInit().get_cdi_provider(), iAction.getPath(), iAction.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_action){
			rAction = (i_action)actionFromProvider;
			if(iBean!=null) ((bean)rAction).set_infobean(iBean);
			loadedFromProvider=true;
		}
	}  
*/
	if(!loadedFromProvider){
		if(iAction.getType()!=null && !iAction.getType().equals("")){
			if(iAction.getProperty("init").equalsIgnoreCase("annotation") && !iAction.isAnnotationLoaded())
				iAction = (info_action)loadFromAnnotations(iAction);

			try{
				rAction = (i_action)Class.forName(iAction.getType()).newInstance();
			}catch (Exception e) {
				bsController.writeLog("Load_action-> actionFactory error: "+e.toString(),iStub.log_ERROR);
			}
			
		}
	}
	
	if(rAction!=null){
		if(iAction.getListener()!=null && !iAction.getListener().equals("")){
			try{
				listener_action lAction= (listener_action)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								iAction.getListener());
				if(lAction!=null) rAction.setListener_a(lAction);
			}catch (Exception e) {
				try{
					listener_bean lAction= (listener_bean)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								iAction.getListener());
					if(lAction!=null) rAction.asBean().setListener_b(lAction);
				}catch(Exception ex){
				}catch(Throwable t) {
				}
			}catch(Throwable t) {
			}				
		}
		try{
			if(rAction.getListener_a()==null && getListener_actions()!=null && !getListener_actions().equals("")){
				try{
					listener_action lAction= (listener_action)util_provider.getInstanceFromProvider(
									new String[]{
											getProvider(),
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									getListener_actions());
					if(lAction!=null) rAction.setListener_a(lAction);
				}catch (Exception e) {
					try{
						listener_bean lAction= (listener_bean)util_provider.getInstanceFromProvider(
									new String[]{
											getProvider(),
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									getListener_beans());
						if(lAction!=null) rAction.asBean().setListener_b(lAction);
					}catch(Exception ex){
					}catch(Throwable t) {
					}
				}catch(Throwable t) {
				}				
			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
		}catch(Throwable e){
			new bsControllerException(e, iStub.log_ERROR);
		}
		
		if(loadedFromProvider)
			rAction.onPostInstanceFromProvider();
		else
			rAction.onPostInstance();

		
		rAction.set_infoaction(iAction);
		if(iBean!=null){
			if(iBean.getProperty("init").equalsIgnoreCase("annotation") && !iBean.isAnnotationLoaded() && !iBean.getType().equals(""))
				iBean = (info_bean)loadFromAnnotations(iBean);
//			((bean)rAction).set_infobean(iBean);
			rAction.asBean().set_infobean(iBean);
		}
		else 
			rAction.asBean().set_infobean(new info_bean());
//			((bean)rAction).set_infobean(new info_bean());
	}else{
		iAction = new info_action();
		iAction.setPath(id_action);
		rAction.set_infoaction(iAction);
//		((bean)rAction).set_infobean(new info_bean());
		rAction.asBean().set_infobean(new info_bean());
	}
	return rAction;
}


public i_stream streamFactory(String id_stream){
	return streamFactory(id_stream,null,null);
}
public i_stream streamFactory(String id_stream,ServletContext servletContext){
	return streamFactory(id_stream,null,servletContext);
}
public i_stream streamFactory(String id_stream,HttpSession session,ServletContext servletContext){
	i_stream rStream = null;
	if(memoryInContainer_streams.equalsIgnoreCase("true")){
		rStream = bsController.getStreamFromContainer(id_stream);
		if(rStream!=null) return rStream;
	}
		
	rStream = new stream();
	info_stream iStream = null;

	if(_streams==null || _streams.get(id_stream)==null){
		load_actions session_l_actions = (load_actions)session.getAttribute(bsConstants.CONST_SESSION_ACTIONS_INSTANCE);
		if(session_l_actions!=null){
			try{
				iStream = (info_stream)session_l_actions.getBuilder().get_b_streams().get(id_stream);
			}catch(Exception e){
			}
		}
		if(iStream==null){
			return rStream;
		}
	}else iStream = (info_stream)_streams.get(id_stream);



	boolean loadedFromProvider=false;
	
	if(bsController.isCanBeProxed()){
//		if((((iStream.getProvider()==null)?"":iStream.getProvider().trim()) + ((getProvider()==null)?"":getProvider().trim()) + ((bsController.getAppInit().get_cdi_provider()==null)?"":bsController.getAppInit().get_cdi_provider().trim())).length()>0 || bsController.getCdiDefaultProvider()!=null || bsController.getEjbDefaultProvider()!=null){
			Object objFromProvider = util_provider.getBeanFromObjectFactory(
							new String[]{
									iStream.getProvider(),
									getProvider(),
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							iStream.getName(),
							iStream.getType(),
							servletContext);
			if(objFromProvider!=null && objFromProvider instanceof i_stream){
				rStream = (i_stream)objFromProvider;
				loadedFromProvider=true;
			}
//		}	
	}
/*	
	if(iStream.getProvider()!=null && !iStream.getProvider().equals("")){
		Object streamFromProvider = util_reflect.providerObjectFactory(iStream.getProvider(), iStream.getName(), iStream.getType(), servletContext);
		if(streamFromProvider!=null && streamFromProvider instanceof i_stream){
			rStream = (i_stream)streamFromProvider;
			loadedFromProvider=true;
		}
	}else if(getProvider()!=null && !getProvider().equals("")){
		Object streamFromProvider = util_reflect.providerObjectFactory(getProvider(), iStream.getName(), iStream.getType(), servletContext);
		if(streamFromProvider!=null && streamFromProvider instanceof i_stream){
			rStream = (i_stream)streamFromProvider;
			loadedFromProvider=true;
		}
	}else if(bsController.getAppInit().get_cdi_provider()!=null && !bsController.getAppInit().get_cdi_provider().equals("")){
		Object streamFromProvider = util_reflect.providerObjectFactory(bsController.getAppInit().get_cdi_provider(), iStream.getName(), iStream.getType(), servletContext);
		if(streamFromProvider!=null && streamFromProvider instanceof i_stream){
			rStream = (i_stream)streamFromProvider;
			loadedFromProvider=true;
		}
	}
*/
	if(!loadedFromProvider){
		if(iStream==null || iStream.getType()==null || iStream.getType().equals("")) return rStream;

		if(iStream.getType()!=null && !iStream.getType().equals("")){
			if(iStream.getProperty("init").equalsIgnoreCase("annotation") && !iStream.isAnnotationLoaded())
				iStream = (info_stream)loadFromAnnotations(iStream);			
				try{
					rStream = (i_stream)Class.forName(iStream.getType()).newInstance();
				}catch (Exception e) {
					bsController.writeLog("Load_action-> streamFactory error: "+e.toString(),iStub.log_INFO);
				}
		}
	}
	if(rStream!=null)
		if(iStream.getListener()!=null && !iStream.getListener().equals("")){
			try{
				listener_stream lStream= (listener_stream)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								iStream.getListener());
				if(lStream!=null) rStream.setListener_s(lStream);
			}catch (Exception e) {
			}catch(Throwable t) {
			}				
		}
		if(rStream.getListener_s()==null && getListener_streams()!=null && !getListener_streams().equals("")){
			try{
				listener_stream lStream= (listener_stream)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								getListener_streams());
				if(lStream!=null) rStream.setListener_s(lStream);
			}catch (Exception e) {
			}catch(Throwable t) {
			}			
		}
		
		if(loadedFromProvider)
			rStream.onPostInstanceFromProvider();
		else rStream.onPostInstance();
		
		rStream.set_infostream(iStream);
		
		if(memoryInContainer_streams.equalsIgnoreCase("true"))
			bsController.putStreamIntoContainer(iStream.getName(), rStream);
	
	return rStream;
}



public i_stream actionStream(String id_stream){
	return streamFactory(id_stream,null,null);
}


public i_bean beanFactory(String id_bean){
	return beanFactory(id_bean,null,null,null);
}
public i_bean beanFactory(String id_bean,ServletContext servletContext){
	return beanFactory(id_bean,null,servletContext,null);
}
public i_bean beanFactory(String id_bean,HttpSession session,ServletContext servletContext){
	return beanFactory(id_bean,null,servletContext,null);
}
public i_bean beanFactory(String id_bean,HttpSession session,ServletContext servletContext,i_action action_instance){
	i_bean rBean = null;
	if(action_instance!=null){
		rBean = action_instance.asBean();
	}
	else rBean = new bean();
	info_bean iBean = null;
	if(_beans==null || _beans.get(id_bean)==null){
		load_actions session_l_actions = (load_actions)session.getAttribute(bsConstants.CONST_SESSION_ACTIONS_INSTANCE);
		if(session_l_actions!=null){
			try{
				iBean = (info_bean)session_l_actions.getBuilder().get_b_beans().get(id_bean);
			}catch(Exception e){
			}
		}
		if(iBean==null){
			if(rBean.get_infobean()!=null && action_instance.get_infoaction()!=null){
				rBean.get_infobean().setName(action_instance.get_infoaction().getName());
				rBean.get_infobean().setType(action_instance.get_infoaction().getType());
			}
			if(rBean.get_infobean()!=null){
				if(rBean.get_infobean().getListener()!=null && !rBean.get_infobean().getListener().equals("")){
					try{
						listener_bean lBean= (listener_bean)util_provider.getInstanceFromProvider(
										new String[]{
												getProvider(),
												bsController.getAppInit().get_context_provider(),
												bsController.getAppInit().get_cdi_provider(),
												bsController.getAppInit().get_ejb_provider()
										},
										iBean.getListener());
						if(lBean!=null) rBean.setListener_b(lBean);;
					}catch (Exception e) {
					}catch(Throwable t) {
					}				
				}
				try{
					if(rBean.getListener_b()==null && getListener_beans()!=null && !getListener_beans().equals("")){
						try{
							listener_bean lBean= (listener_bean)util_provider.getInstanceFromProvider(
											new String[]{
													getProvider(),
													bsController.getAppInit().get_context_provider(),
													bsController.getAppInit().get_cdi_provider(),
													bsController.getAppInit().get_ejb_provider()
											},
											getListener_beans());
							if(lBean!=null) rBean.setListener_b(lBean);
						}catch (Exception e) {
						}catch(Throwable t) {
						}				
					}
				}catch(Exception e){
					new bsControllerException(e, iStub.log_ERROR);
				}catch(Throwable e){
					new bsControllerException(e, iStub.log_ERROR);
				}
			}

			return rBean;
		}
	}else iBean = (info_bean)_beans.get(id_bean);

	if(iBean==null) return null;
	boolean loadedFromProvider=false;
	
	if(bsController.isCanBeProxed()){
//		if(((iBean.getProvider()==null)?"":(iBean.getProvider().trim()) + ((getProvider()==null)?"":getProvider().trim()) + ((bsController.getAppInit().get_cdi_provider()==null)?"":bsController.getAppInit().get_cdi_provider().trim())).length()>0 || bsController.getCdiDefaultProvider()!=null || bsController.getEjbDefaultProvider()!=null){
			Object objFromProvider = util_provider.getBeanFromObjectFactory(
							new String[]{
									iBean.getProvider(),
									getProvider(),
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							iBean.getName(),
							iBean.getType(),
							servletContext);
			if(objFromProvider!=null && objFromProvider instanceof i_bean){
				rBean = (i_bean)objFromProvider;
				loadedFromProvider=true;
			}
			if(objFromProvider!=null && !(objFromProvider instanceof i_bean)){
				rBean.setDelegated(objFromProvider);
				loadedFromProvider=true;
			}
//		}
	}
/*	
	if(iBean.getProvider()!=null && !iBean.getProvider().equals("")){
		Object actionFromProvider = util_reflect.providerObjectFactory(iBean.getProvider(), iBean.getName(), iBean.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_bean){
			rBean = (i_bean)actionFromProvider;
			loadedFromProvider=true;
		}
		if(actionFromProvider!=null && !(actionFromProvider instanceof i_bean)){
			rBean.setDelegated(actionFromProvider);
			loadedFromProvider=true;
		}
	}else if(getProvider()!=null && !getProvider().equals("")){
		Object actionFromProvider = util_reflect.providerObjectFactory(getProvider(), iBean.getName(), iBean.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_bean){
			rBean = (i_bean)actionFromProvider;
			loadedFromProvider=true;
		}
		if(actionFromProvider!=null && !(actionFromProvider instanceof i_bean)){
			rBean.setDelegated(actionFromProvider);
			loadedFromProvider=true;
		}
	}else if(bsController.getAppInit().get_cdi_provider()!=null && !bsController.getAppInit().get_cdi_provider().equals("")){
		Object actionFromProvider = util_reflect.providerObjectFactory(bsController.getAppInit().get_cdi_provider(), iBean.getName(), iBean.getType(), servletContext);
		if(actionFromProvider!=null && actionFromProvider instanceof i_bean){
			rBean = (i_bean)actionFromProvider;
			loadedFromProvider=true;
		}
		if(actionFromProvider!=null && !(actionFromProvider instanceof i_bean)){
			rBean.setDelegated(actionFromProvider);
			loadedFromProvider=true;
		}
	}
*/

	if( !loadedFromProvider &&
		(
		(iBean.getType()==null || iBean.getType().trim().equals("")) &&
		(iBean.getModel()==null || iBean.getModel().trim().equals("")) &&
		(iBean.getV_info_items()==null && iBean.getV_info_items().size()==0)
		)) return rBean;


	if(!loadedFromProvider){

		if(iBean.getType()!=null && !iBean.getType().equals("")){
			if(iBean.getProperty("init").equalsIgnoreCase("annotation") && !iBean.isAnnotationLoaded())
				iBean = (info_bean)loadFromAnnotations(iBean);

			try{
				Object obj = Class.forName(iBean.getType()).newInstance();
				if(obj instanceof i_bean)
					rBean = (i_bean)obj;
				else rBean.setDelegated(obj);
			}catch (Exception e) {
				bsController.writeLog("Load_action-> beanFactory error: "+e.toString(),iStub.log_INFO);
			}
		}
		if(iBean.getModel()!=null && !iBean.getModel().equals("")){
			try{
				String model=iBean.getModel();
				byte[] xmlModel = util_classes.getResourceAsByte(model);
				if(xmlModel!=null){
					HashMap fly = (HashMap)util_beanMessageFactory.message2bean(xmlModel, HashMap.class);
					rBean.setParametersFly(fly);
					rBean.setVirtual(true);
				}
			}catch (Exception e) {
				bsController.writeLog("Load_action-> beanFactory load model "+iBean.getModel()+" error: "+e.toString(),iStub.log_INFO);
			}

		}
		if(iBean.getV_info_items()!=null && iBean.getV_info_items().size()>0){
			String xmlModel = "<item name=\""+iBean.getName()+"\">";
			for(int i=0;i<iBean.getV_info_items().size();i++){
				info_item iItem = (info_item)iBean.getV_info_items().get(i);
				if(iItem!=null) xmlModel+=iItem.toXml();
			}
			xmlModel+=System.getProperty("line.separator")+"</item>";
			try{
				HashMap fly = (HashMap)util_beanMessageFactory.message2bean(xmlModel.getBytes(), HashMap.class);
				rBean.setParametersFly(fly);
				rBean.setVirtual(true);
			}catch (Exception e) {
				bsController.writeLog("Load_action-> beanFactory load virtual items of model "+iBean.getModel()+" error: "+e.toString(),iStub.log_INFO);
			}
		}

	}
	if(rBean!=null){
		if(iBean.getListener()!=null && !iBean.getListener().equals("")){
			try{
				listener_bean lBean= (listener_bean)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								iBean.getListener());
				if(lBean!=null) rBean.setListener_b(lBean);;
			}catch (Exception e) {
			}catch(Throwable t) {
			}				
		}
		try{
			if(rBean.getListener_b()==null && getListener_beans()!=null && !getListener_beans().equals("")){
				try{
					listener_bean lBean= (listener_bean)util_provider.getInstanceFromProvider(
									new String[]{
											getProvider(),
											bsController.getAppInit().get_context_provider(),
											bsController.getAppInit().get_cdi_provider(),
											bsController.getAppInit().get_ejb_provider()
									},
									getListener_beans());
					if(lBean!=null) rBean.setListener_b(lBean);
				}catch (Exception e) {
				}catch(Throwable t) {
				}				
			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
		}catch(Throwable e){
			new bsControllerException(e, iStub.log_ERROR);
		}
		if(loadedFromProvider)
			rBean.onPostInstanceFromProvider();
		else rBean.onPostInstance();
		
		rBean.set_infobean(iBean);
	}
	
	
	return rBean;
}





public i_transformation transformationFactory(String transformationName){
	return load_actions.transformationFactory(transformationName,_transformationoutput, null);
}
public i_transformation transformationFactory(String transformationName, ServletContext servletContext){
	return load_actions.transformationFactory(transformationName,_transformationoutput, servletContext);
}



public static i_transformation transformationFactory(String transformationName, HashMap h_transformationoutput, ServletContext servletContext){
	i_transformation rTransformation = new transformation();
	if(h_transformationoutput==null || h_transformationoutput.get(transformationName)==null) return rTransformation;

	info_transformation iTransformation = (info_transformation)h_transformationoutput.get(transformationName);
	boolean loadedFromProvider=false;
	
	
	
	if(iTransformation.getProvider()!=null && !iTransformation.getProvider().equals("")){
		Object transformationFromProvider = providerTransformationFactory(iTransformation.getProvider(), iTransformation.getName(), servletContext);
		if(transformationFromProvider!=null && transformationFromProvider instanceof i_transformation){
			rTransformation = (i_transformation)transformationFromProvider;
			loadedFromProvider=true;
		}
	}

	if(!loadedFromProvider){
		if(iTransformation==null || iTransformation.getType()==null || iTransformation.getType().equals("")) return rTransformation;

		if(iTransformation.getType()!=null && !iTransformation.getType().equals("")){

				try{
					rTransformation = (i_transformation)Class.forName(iTransformation.getType()).newInstance();
				}catch (Exception e) {
					try{
						rTransformation = (i_transformation)Class.forName(i_transformation.CLASSHIDRA_TRANSFORMATION_PACKAGE+iTransformation.getType()).newInstance();
					}catch (Exception ex) {
						bsController.writeLog("info_action-> transformationFactory error: "+e.toString(),iStub.log_INFO);
						bsController.writeLog("info_action-> transformationFactory error: "+ex.toString(),iStub.log_INFO);
					}
				}
		}
	}
	rTransformation.set_infotransformation(iTransformation);
	return rTransformation;
}

private static Object providerTransformationFactory(String id_provider, String transformationName,ServletContext servletContext){
	try{
		if(id_provider==null || transformationName==null || id_provider.equals("") || transformationName.equals("")) return null;
//			i_provider provider  = (i_provider)Class.forName(bsConstants.CONST_PROVIDER_PATH+id_provider).newInstance();
		i_provider provider  = (i_provider)util_provider.getInstanceFromProvider(
						new String[]{
								bsController.getAction_config().getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						bsConstants.CONST_PROVIDER_PATH+id_provider);
		if(provider==null) 
			provider  = (i_provider)util_provider.getInstanceFromProvider(
						new String[]{
								bsController.getAction_config().getProvider(),
								bsController.getAppInit().get_context_provider(),
								bsController.getAppInit().get_cdi_provider(),
								bsController.getAppInit().get_ejb_provider()
						},
						id_provider);
		if(provider==null) 
			return null;
		provider.set_context(servletContext);
		return provider.get_bean(transformationName);
	}catch(Exception e){
		new bsControllerException(e,iStub.log_ERROR);
	}catch (Throwable t) {
		new bsControllerException(t,iStub.log_ERROR);
	}
	
	return null;
}




public void initTop(Node node) throws bsControllerException{
	if(node==null) return;
	try{
		NamedNodeMap nnm = node.getAttributes();
		if (nnm!=null){
			for (int i=0;i<node.getAttributes().getLength();i++){
				String paramName = node.getAttributes().item(i).getNodeName();
				Node node_nnm =	nnm.getNamedItem(paramName);
				if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
			}
		}
	}catch(Exception e){
		new bsControllerException(e,iStub.log_DEBUG);
	}
}





private void readFormElements(Node node) throws Exception{
	if(node==null) return;

	if(node.getNodeName().equals("action-config")){
		this.initTop(node);

	}
	if(node.getNodeName().equals("action-streams")){
		try{
			NamedNodeMap nnm = node.getAttributes();
			if (nnm!=null){
				for (int j=0;j<node.getAttributes().getLength();j++){
					String paramName = node.getAttributes().item(j).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}		
		int stream_order=0;
		HashMap _streams_order = new HashMap();
		for(int k=0;k<node.getChildNodes().getLength();k++){
			if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
				info_stream iStream = new info_stream();
				iStream.init(node.getChildNodes().item(k),_redirects);

					info_stream old_stream = (info_stream)_streams.get(iStream.getName());
					if(old_stream!=null){
						_streams.remove(old_stream.getName());
						_streams_order.remove(Integer.valueOf(old_stream.getInt_order()));
						info_stream fromVinfo = (info_stream)util_find.findElementFromList(v_info_streams, old_stream.getName(), "name");
						if(fromVinfo!=null)
							v_info_streams.remove(fromVinfo);
						Vector app_action = new Vector(old_stream.get_apply_to_action().keySet());
						if(app_action.size()==0){
							int l=0;
							while(l< ((Vector)_streams_apply_to_actions.get("*")).size()){
								info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get("*")).get(l);
								if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get("*")).remove(l);
								else l++;
							}
						}else{
							for(int j=0;j<app_action.size();j++){
								String key=(String)app_action.get(j);
								int l=0;
								while(l< ((Vector)_streams_apply_to_actions.get(key)).size()){
									info_stream current = (info_stream)((Vector)_streams_apply_to_actions.get(key)).get(l);
									if(old_stream.getName().equals(current.getName())) ((Vector)_streams_apply_to_actions.get(key)).remove(l);
									else l++;
								}

							}
						}

					}
					_streams.put(iStream.getName(),iStream);
					if(iStream.getInt_order()==-1){
						while(_streams_order.get(Integer.valueOf(stream_order))!=null) stream_order++;
						_streams_order.put(Integer.valueOf(stream_order) , iStream.getName());
						stream_order++;
					}else{
						_streams_order.put(Integer.valueOf(iStream.getInt_order()) , iStream.getName());
						stream_order=iStream.getInt_order();
						stream_order++;
					}
				
			}
		}

		Vector v_streams_order = new util_sort().sort(new  Vector(_streams_order.keySet()),"");
		for(int i=0;i<v_streams_order.size();i++){
			info_stream current = (info_stream)_streams.get(_streams_order.get(v_streams_order.get(i)));
			if(current!=null){
				Vector app_action = new Vector(current.get_apply_to_action().keySet());
				if(app_action.size()==0){
					String key="*";
					((Vector)_streams_apply_to_actions.get(key)).add(current);
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						if(_streams_apply_to_actions.get(key)==null) _streams_apply_to_actions.put(key,new Vector());
						((Vector)_streams_apply_to_actions.get(key)).add(current);
					}
				}
			}
		}

//		v_info_streams.addAll(new Vector(_streams.values()));
		v_info_streams = (new Vector(_streams.values()));
		v_info_streams = new util_sort().sort(v_info_streams,"int_order");


	}
	if(node.getNodeName().equals("form-beans")){
		try{
			NamedNodeMap nnm = node.getAttributes();
			if (nnm!=null){
				for (int j=0;j<node.getAttributes().getLength();j++){
					String paramName = node.getAttributes().item(j).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}		
		int order=0;
		if(v_info_beans!=null && v_info_beans.size()>0){
			try{
				order = ((info_bean)v_info_beans.get(v_info_beans.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		for(int k=0;k<node.getChildNodes().getLength();k++){
			if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
				info_bean iBean = new info_bean();
				iBean.init(node.getChildNodes().item(k));
				order++;
				iBean.setOrder(Integer.valueOf(order).toString());
				if(iBean!=null) _beans.put(iBean.getName(),iBean);
			}
		}

//		v_info_beans.addAll(new Vector(_beans.values()));
		v_info_beans = (new Vector(_beans.values()));
		v_info_beans = new util_sort().sort(v_info_beans,"int_order");
	}
	if(node.getNodeName().equals("form-redirects")){
		int order=0;
		if(v_info_redirects!=null && v_info_redirects.size()>0){
			try{
				order = ((info_redirect)v_info_redirects.get(v_info_redirects.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		for(int k=0;k<node.getChildNodes().getLength();k++){
			if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
				info_redirect iRedirect = new info_redirect();
				iRedirect.init(node.getChildNodes().item(k));
				order++;
				iRedirect.setOrder(Integer.valueOf(order).toString());
				if(iRedirect!=null) _redirects.put(iRedirect.getPath(),iRedirect);
			}
		}

//		v_info_redirects.addAll(new Vector(_redirects.values()));
		v_info_redirects = (new Vector(_redirects.values()));
		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");

	}

	if(node.getNodeName().equals("redirect-transformations")){
		int order=0;
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			try{
				order = ((info_transformation)v_info_transformationoutput.get(v_info_transformationoutput.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		for(int k=0;k<node.getChildNodes().getLength();k++){
			if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
				if(node.getNodeName().toLowerCase().equals("transformationoutput")){
					info_transformation iTransformationoutput = new info_transformation();
					iTransformationoutput.init(node);
					order++;
					iTransformationoutput.setOrder(Integer.valueOf(order).toString());
					if(iTransformationoutput!=null) _transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
				}
			}
		}

//		v_info_transformationoutput.addAll(new Vector(_transformationoutput.values()));
		v_info_transformationoutput = (new Vector(_transformationoutput.values()));
		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");

	}



	if(node.getNodeName().equals("action-mappings")){
		try{
			NamedNodeMap nnm = node.getAttributes();
			if (nnm!=null){
				for (int j=0;j<node.getAttributes().getLength();j++){
					String paramName = node.getAttributes().item(j).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
		int order=0;
		if(v_info_actions!=null && v_info_actions.size()>0){
			try{
				order = ((info_action)v_info_actions.get(v_info_actions.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}

		for(int k=0;k<node.getChildNodes().getLength();k++){
			if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
				info_action iAction = new info_action();
				iAction.init(node.getChildNodes().item(k),_redirects,_beans); 
				order++;
				iAction.setOrder(Integer.valueOf(order).toString());
				if(iAction!=null){
					_actions.put(iAction.getPath(),iAction);
					if(iAction.get_calls().size()>0){
						Vector a_actioncalls = new Vector(iAction.get_calls().values());
						for(int j=0;j<a_actioncalls.size();j++){
/*							
							_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getOwner()+
									((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
									((info_call)a_actioncalls.get(j)).getName(),
									a_actioncalls.get(j));
							if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
								_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getPath(),
										a_actioncalls.get(j));
*/							
							if(((info_call)a_actioncalls.get(j)).getExposed()==null || ((info_call)a_actioncalls.get(j)).getExposed().size()==0){
								_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getOwner()+
										((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
										((info_call)a_actioncalls.get(j)).getName(),
										a_actioncalls.get(j));
								if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
									_actioncalls.put(
											((info_call)a_actioncalls.get(j)).getPath(),
											a_actioncalls.get(j));
							}else{
								for(int e=0;e<((info_call)a_actioncalls.get(j)).getExposed().size();e++){
									String suffix = "."+((info_call)a_actioncalls.get(j)).getExposed().get(e).toString();
									_actioncalls.put(
											((info_call)a_actioncalls.get(j)).getOwner()+
											((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
											((info_call)a_actioncalls.get(j)).getName()+suffix,
											a_actioncalls.get(j));
									if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
										_actioncalls.put(
												((info_call)a_actioncalls.get(j)).getPath()+suffix,
												a_actioncalls.get(j));

								}
							}
							if(((info_call)a_actioncalls.get(j)).getRestmapping()!=null && ((info_call)a_actioncalls.get(j)).getRestmapping().size()>0){
								for(int r=0;r<((info_call)a_actioncalls.get(j)).getRestmapping().size();r++){
									info_rest iRest = (info_rest)((info_call)a_actioncalls.get(j)).getRestmapping().get(r);
									if(_restmapping.get(iRest.getPath())==null)
										_restmapping.put(iRest.getPath(), new Vector());
									((Vector)_restmapping.get(iRest.getPath())).add(iRest);
								}
							}
						}
						
					}
					if(iAction.getRestmapping().size()>0){
						for(int r=0;r<iAction.getRestmapping().size();r++){
							info_rest iRest = (info_rest)iAction.getRestmapping().get(r);
							if(_restmapping.get(iRest.getPath())==null)
								_restmapping.put(iRest.getPath(), new Vector());
							((Vector)_restmapping.get(iRest.getPath())).add(iRest);
						}
					}
										
				}
			}
		}
//		v_info_actions.addAll(new Vector(_actions.values()));
		v_info_actions = (new Vector(_actions.values()));
		v_info_actions = new util_sort().sort(v_info_actions,"int_order");

	}
}
public String getError() {
	return error;
}
public void setError(String string) {
	error = string;
}
public String getAuth_error() {
	return auth_error;
}
public void setAuth_error(String string) {
	auth_error = string;
}



public String getSession_error() {
	return session_error;
}
public void setSession_error(String string) {
	session_error = string;
}

public boolean isReadOk() {
	return readOk_File || readOk_Folder || readOk_Resource || readOk_Db || readOk_ExtLoader;
}

public static HashMap get_actions() {
	return _actions;
}

public void set_actions(HashMap map) {
	_actions = map;
}
public static HashMap get_streams() {
	return _streams;
}
public void set_streams(HashMap _streams) {
	load_actions._streams = _streams;
}
public static HashMap get_beans() {
	return _beans;
}
public void set_beans(HashMap _beans) {
	load_actions._beans = _beans;
}
public static HashMap get_redirects() {
	return _redirects;
}
public void set_redirects(HashMap _redirects) {
	load_actions._redirects = _redirects;
}
public HashMap get_streams_apply_to_actions() {
	return _streams_apply_to_actions;
}
public boolean isReadOk_File() {
	return readOk_File;
}
public String getExternalloader() {
	return externalloader;
}
public void setExternalloader(String externalloader) {
	this.externalloader = externalloader;
}

public String getLoadedFrom() {
	return loadedFrom;
}

public String toString(){
	return toXml();
}

public String toXml(){
	String result="";
	if(xmlEncoding!=null && !xmlEncoding.equals(""))
		result+="<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?>"+System.getProperty("line.separator");
	result+="<action-config";
	result+=" externalloader=\""+util_format.normaliseXMLText(externalloader)+"\"";
	if(provider!=null && !provider.trim().equals("")) result+=" provider=\""+util_format.normaliseXMLText(provider)+"\"";
	if(instance_navigated!=null && !instance_navigated.trim().equals("")) result+=" instance_navigated=\""+util_format.normaliseXMLText(instance_navigated)+"\"";
	if(instance_local_container!=null && !instance_local_container.trim().equals("")) result+=" instance_local_container=\""+util_format.normaliseXMLText(instance_local_container)+"\"";
	if(instance_scheduler_container!=null && !instance_scheduler_container.trim().equals("")) result+=" instance_scheduler_container=\""+util_format.normaliseXMLText(instance_scheduler_container)+"\"";
	if(instance_onlysession!=null && !instance_onlysession.trim().equals("")) result+=" instance_onlysession=\""+util_format.normaliseXMLText(instance_onlysession)+"\"";
	if(instance_servletcontext!=null && !instance_servletcontext.trim().equals("")) result+=" instance_servletcontext=\""+util_format.normaliseXMLText(instance_servletcontext)+"\"";

	result+=">";
	result+=System.getProperty("line.separator")+"   <action-streams";
	if(listener_streams!=null && !listener_streams.trim().equals("")) result+=" listener_streams=\""+util_format.normaliseXMLText(listener_streams)+"\"";
	if(memoryInContainer_streams!=null && !memoryInContainer_streams.trim().equals("")) result+=" memoryInContainer_streams=\""+util_format.normaliseXMLText(memoryInContainer_streams)+"\"";

	result+=">";
	if(v_info_streams!=null && v_info_streams.size()>0){
		for(int i=0;i<v_info_streams.size();i++){
			info_stream entity = (info_stream)v_info_streams.get(i);
			if(entity!=null && !entity.getAnnotated().equals("true")) result+=entity.toXml();
			if(entity!=null && entity.getAnnotated().equals("true")){
				result+=System.getProperty("line.separator")+"<!--";
				result+=entity.toXml();
				result+=System.getProperty("line.separator")+"-->";
			}
		}
	}
	result+=System.getProperty("line.separator")+"   </action-streams>";

	result+=System.getProperty("line.separator")+"   <form-beans";
	if(listener_beans!=null && !listener_beans.trim().equals("")) result+=" listener_beans=\""+util_format.normaliseXMLText(listener_beans)+"\"";
	result+=">";	
	if(v_info_beans!=null && v_info_beans.size()>0){
		for(int i=0;i<v_info_beans.size();i++){
			info_bean entity = (info_bean)v_info_beans.get(i);
			if(entity!=null && !entity.getAnnotated().equals("true")) result+=entity.toXml();
			if(entity!=null && entity.getAnnotated().equals("true")){
				result+=System.getProperty("line.separator")+"<!--";
				result+=entity.toXml();
				result+=System.getProperty("line.separator")+"-->";
			}
		}
	}
	result+=System.getProperty("line.separator")+"   </form-beans>";

	result+=System.getProperty("line.separator")+"   <form-redirects>";
	if(v_info_redirects!=null && v_info_redirects.size()>0){
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect entity = (info_redirect)v_info_redirects.get(i);
			if(entity!=null){
				entity.setPrefix("form-");
				if(entity!=null && !entity.getAnnotated().equals("true")) result+=entity.toXml();
				if(entity!=null && entity.getAnnotated().equals("true")){
					result+=System.getProperty("line.separator")+"<!--";
					result+=entity.toXml();
					result+=System.getProperty("line.separator")+"-->";
				}

			}
			
		}
	}
	result+=System.getProperty("line.separator")+"   </form-redirects>";

	result+=System.getProperty("line.separator")+"   <action-mappings";
	if(error!=null && !error.trim().equals("")) result+=" error=\""+util_format.normaliseXMLText(error)+"\"";
	if(auth_error!=null && !auth_error.trim().equals("")) result+=" auth_error=\""+util_format.normaliseXMLText(auth_error)+"\"";
	if(session_error!=null && !session_error.trim().equals("")) result+=" session_error=\""+util_format.normaliseXMLText(session_error)+"\"";
	if(listener_actions!=null && !listener_actions.trim().equals("")) result+=" listener_actions=\""+util_format.normaliseXMLText(listener_actions)+"\"";
	result+=">";
	if(v_info_actions!=null && v_info_actions.size()>0){
		for(int i=0;i<v_info_actions.size();i++){
			info_action entity = (info_action)v_info_actions.get(i);
			if(entity!=null && !entity.getAnnotated().equals("true")) result+=entity.toXml();
			if(entity!=null && entity.getAnnotated().equals("true")){
				result+=System.getProperty("line.separator")+"<!--";
				result+=entity.toXml();
				result+=System.getProperty("line.separator")+"-->";
			}
		}
	}
	result+=System.getProperty("line.separator")+"   </action-mappings>";

	result+=System.getProperty("line.separator")+"   <redirect-transformations>";
	if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
		for(int i=0;i<v_info_transformationoutput.size();i++){
			info_transformation entity = (info_transformation)v_info_transformationoutput.get(i);
			if(entity!=null && !entity.getAnnotated().equals("true")) result+=entity.toXml();
			if(entity!=null && entity.getAnnotated().equals("true")){
				result+=System.getProperty("line.separator")+"<!--";
				result+=entity.toXml();
				result+=System.getProperty("line.separator")+"-->";
			}
		}
	}
	result+=System.getProperty("line.separator")+"   </redirect-transformations>";

	result+=System.getProperty("line.separator")+"</action-config>";

	return result;
}

public static HashMap get_transformationoutput() {
	return _transformationoutput;
}

public  void set_transformationoutput(HashMap transformationoutput) {
	_transformationoutput = transformationoutput;
}

public Vector getV_info_actions() {
	return v_info_actions;
}

public void setV_info_actions(Vector vInfoActions) {
	v_info_actions = vInfoActions;
}

public Vector getV_info_streams() {
	return v_info_streams;
}

public void setV_info_streams(Vector vInfoStreams) {
	v_info_streams = vInfoStreams;
}

public Vector getV_info_beans() {
	return v_info_beans;
}

public void setV_info_beans(Vector vInfoBeans) {
	v_info_beans = vInfoBeans;
}

public Vector getV_info_redirects() {
	return v_info_redirects;
}

public void setV_info_redirects(Vector vInfoRedirects) {
	v_info_redirects = vInfoRedirects;
}

public Vector getV_info_transformationoutput() {
	return v_info_transformationoutput;
}

public void setV_info_transformationoutput(Vector vInfoTransformation) {
	v_info_transformationoutput = vInfoTransformation;
}

public load_actions_builder getBuilder() {
	return builder;
}

public void set_streams_apply_to_actions(HashMap streamsApplyToActions) {
	_streams_apply_to_actions = streamsApplyToActions;
}

public String getXmlEncoding() {
	return xmlEncoding;
}

public void setXmlEncoding(String xmlEncoding) {
	this.xmlEncoding = xmlEncoding;
}

public void setReadOk_File(boolean readOkFile) {
	readOk_File = readOkFile;
}

public boolean isReadDef() {
	return readDef;
}

public void setReadDef(boolean readDef) {
	this.readDef = readDef;
}

public boolean isReadOk_Resource() {
	return readOk_Resource;
}

public void setReadOk_Resource(boolean readOkResource) {
	readOk_Resource = readOkResource;
}

public boolean isReadOk_Folder() {
	return readOk_Folder;
}

public void setReadOk_Folder(boolean readOkFolder) {
	readOk_Folder = readOkFolder;
}

public boolean isReadOk_Db() {
	return readOk_Db;
}

public void setReadOk_Db(boolean readOkDb) {
	readOk_Db = readOkDb;
}

public boolean isReadOk_ExtLoader() {
	return readOk_ExtLoader;
}

public void setReadOk_ExtLoader(boolean readOkExtLoader) {
	readOk_ExtLoader = readOkExtLoader;
}

public String getListener_actions() {
	return listener_actions;
}

public void setListener_actions(String listenerA) {
	listener_actions = listenerA;
}

public String getListener_beans() {
	return listener_beans;
}

public void setListener_beans(String listenerB) {
	listener_beans = listenerB;
}

public String getListener_streams() {
	return listener_streams;
}

public void setListener_streams(String listenerS) {
	listener_streams = listenerS;
}


public String getMemoryInContainer_streams() {
	return memoryInContainer_streams;
}

public void setMemoryInContainer_streams(String memoryInContainerStreams) {
	memoryInContainer_streams = memoryInContainerStreams;
}

public String getProvider() {
	return provider;
}

public void setProvider(String provider) {
	this.provider = provider;
}

public String getInstance_navigated() {
	return instance_navigated;
}

public void setInstance_navigated(String provider_navigated) {
	this.instance_navigated = provider_navigated;
}

public String getInstance_local_container() {
	return instance_local_container;
}

public void setInstance_local_container(String provider_local_container) {
	this.instance_local_container = provider_local_container;
}

public String getInstance_scheduler_container() {
	return instance_scheduler_container;
}

public void setInstance_scheduler_container(String provider_scheduler_container) {
	this.instance_scheduler_container = provider_scheduler_container;
}

public String getInstance_onlysession() {
	return instance_onlysession;
}

public String getInstance_servletcontext() {
	return instance_servletcontext;
}

public void setInstance_onlysession(String instance_onlysession) {
	this.instance_onlysession = instance_onlysession;
}

public void setInstance_servletcontext(String instance_servletcontext) {
	this.instance_servletcontext = instance_servletcontext;
}


public static HashMap get_actioncalls() {
	return _actioncalls;
}

public static HashMap get_restmapping(){
	return _restmapping;
}

public static void set_actioncalls(HashMap _actioncalls) {
	load_actions._actioncalls = _actioncalls;
}

public static void set_restmapping(HashMap _restmapping) {
	load_actions._restmapping = _restmapping;
}


public boolean isReimposted() {
	return reimposted;
}

public void setReimposted(boolean reimposted) {
	this.reimposted = reimposted;
}

class load_actions_builder  implements  java.io.Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private HashMap _b_actions=null;
	private HashMap _b_actioncalls=null;
	private HashMap _b_restmapping=null;
	private HashMap _b_streams=null;
	private HashMap _b_streams_apply_to_actions=null;

	private HashMap _b_beans=null;
	private HashMap _b_redirects=null;
	private HashMap _b_transformationoutput=null;

	load_actions_builder(){
		super();
		if(_b_actions==null) _b_actions = new HashMap();
		if(_b_actioncalls==null) _b_actioncalls = new HashMap();
		if(_b_restmapping==null) _b_restmapping = new HashMap();
		if(_b_streams==null) _b_streams = new HashMap();
		if(_b_streams_apply_to_actions==null){
			_b_streams_apply_to_actions = new HashMap();
			_b_streams_apply_to_actions.put("*",new Vector());
		}
		if(_b_beans==null) _b_beans = new HashMap();
		if(_b_redirects==null) _b_redirects = new HashMap();
		if(_b_transformationoutput==null) _b_transformationoutput = new HashMap();

	}

	public void syncroWithBuilder(){
		load_actions._actions =_b_actions;
		load_actions._actioncalls =_b_actioncalls;
		load_actions._restmapping = _b_restmapping;
		load_actions._streams = _b_streams;
		load_actions._streams_apply_to_actions = _b_streams_apply_to_actions;
		load_actions._beans = _b_beans;
		load_actions._redirects = _b_redirects;
		load_actions._transformationoutput = _b_transformationoutput;
	}

	public boolean builder_init(String _xml) throws bsControllerException, Exception{
		return builder_init(_xml,true);
	}
	public boolean builder_init(String _xml, boolean load_def_actions) throws bsControllerException, Exception{

		v_info_actions=new Vector();
		v_info_streams=new Vector();
		v_info_beans=new Vector();
		v_info_redirects=new Vector();
		v_info_transformationoutput=new Vector();

		if(load_def_actions)
			builder_load_def_actions();
		
		if(getExternalloader()!=null && !getExternalloader().equals("")){
			try{
				i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(
							new String[]{
									getProvider(),
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							getExternalloader());
				extl.load();
				reInit(extl);
			}catch(Exception e){
			}catch(Throwable t){
			}
		}


		Document documentXML = null;
		documentXML = util_xml.readXMLData(_xml);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}
		if(builder_readDocumentXml(documentXML)) return true;
		else return false;
	}


	private boolean builder_initWithData(String _xml) throws bsControllerException, Exception{
		Document documentXML = null;
		documentXML = util_xml.readXMLData(_xml);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}
		if(builder_readDocumentXml(documentXML)) return true;
		else return false;
	}

	private boolean builder_readDocumentXml(Document documentXML) throws Exception{

		if(documentXML!=null){

			Node node = null;
			try{
				int first=0;
				while(node==null && first < documentXML.getChildNodes().getLength()){
					if(documentXML.getChildNodes().item(first).getNodeType()== Node.ELEMENT_NODE)
						node = documentXML.getChildNodes().item(first);
					first++;
				}
			}catch(Exception e){
				new bsControllerException(e,iStub.log_DEBUG);
			}
			if(node==null) return false;
			builder_readFormElements(node);
			for(int i=0;i<node.getChildNodes().getLength();i++){
				if(node.getChildNodes().item(i).getNodeType()== Node.ELEMENT_NODE)
					builder_readFormElements(node.getChildNodes().item(i));
			}
			if(_b_actions!=null && _b_actions.get("*")!=null){
				Object[] keysIn = _b_actions.keySet().toArray();
				Object[] keysFor = (((info_action)_b_actions.get("*")).get_redirects()).keySet().toArray();
				for(int i=0;i<keysIn.length;i++){
					try{
						HashMap current_redirects = ((info_action)_b_actions.get((String)keysIn[i])).get_redirects();
						for(int j=0;j<keysFor.length;j++)
							current_redirects.put(keysFor[j],((((info_action)_b_actions.get("*")).get_redirects())).get(keysFor[j]));
					}catch(Exception e){
					}
				}

			}
		}else return false;
		return true;
	}

	private void builder_load_def_actions() {
		String property_name = "resources/"+ bsController.CONST_XML_ACTIONS;

		InputStream is = null;
	    BufferedReader br = null;
	    String result=null;
	    String line="";


	    try {
	    	is = this.getClass().getResourceAsStream(property_name);
	    	if(is!=null){
	    		result="";
		    	br = new BufferedReader(new InputStreamReader(is));
		    	while (null != (line = br.readLine())) {
		    		result+=(line+"\n");
		    	}
	    	}
	    }catch (Exception e) {
	    }finally {
	    	try {
	    		if (br != null) br.close();
	    		if (is != null) is.close();
	    	}catch (Exception e) {
	    	}
		}

	    try{
	    	if(result!=null){
	    		builder_initWithData(result);
	    		readDef=true;
	    		loadedFrom+=" "+property_name;
	    	}
	    }catch (Exception e) {
	    	readDef=false;
		}
	    builder_loadFromAnnotations();
	}

	private void builder_loadFromAnnotations(){
		
		if(_b_actions==null) _b_actions = new HashMap();
		if(_b_actioncalls==null) _b_actioncalls = new HashMap();
		if(_b_restmapping==null) _b_restmapping = new HashMap();
		if(_b_streams==null){
			_b_streams = new HashMap();
			readDef = false;
		}
		if(_b_streams_apply_to_actions==null){
			_b_streams_apply_to_actions = new HashMap();
			_b_streams_apply_to_actions.put("*",new Vector());
		}
		if(_b_beans==null) _b_beans = new HashMap();
		if(_b_redirects==null) _b_redirects = new HashMap();

		app_init ainit = bsController.getAppInit();
		i_annotation_scanner l_annotated = null;
		
		if(ainit.get_annotation_scanner()==null || ainit.get_annotation_scanner().equals("")){
			l_annotated = new annotation_scanner();
		}else{
			try{
				l_annotated = (i_annotation_scanner)util_provider.getInstanceFromProvider(
								new String[]{
										getProvider(),
										bsController.getAppInit().get_context_provider(),
										bsController.getAppInit().get_cdi_provider(),
										bsController.getAppInit().get_ejb_provider()
								},
								ainit.get_annotation_scanner());
			}catch(Exception e){
				new bsException("Load Error Annotation scaner: "+ainit.get_annotation_scanner(), iStub.log_ERROR);
				new bsException(e.toString(), iStub.log_ERROR);
				new bsException("Loading Default Annotation", iStub.log_INFO);
				l_annotated = new annotation_scanner();
			}
		}
		
		if(l_annotated==null) l_annotated = new annotation_scanner();
		bsController.writeLog("Start Load_actions with Annotation scaner: "+l_annotated.getClass().getName(),iStub.log_INFO);
		l_annotated.loadAllObjects(_b_redirects);
		
		if(l_annotated.getError()!=null && !l_annotated.getError().equals(""))
			error = l_annotated.getError();
		if(l_annotated.getSession_error()!=null && !l_annotated.getSession_error().equals(""))
			session_error = l_annotated.getSession_error();	
		if(l_annotated.getAuth_error()!=null && !l_annotated.getAuth_error().equals(""))
			auth_error = l_annotated.getAuth_error();	

		if(l_annotated.getListener_actions()!=null && !l_annotated.getListener_actions().equals(""))
			listener_actions = l_annotated.getListener_actions();
		if(l_annotated.getListener_beans()!=null && !l_annotated.getListener_beans().equals(""))
			listener_beans = l_annotated.getListener_beans();
		if(l_annotated.getListener_streams()!=null && !l_annotated.getListener_streams().equals(""))
			listener_streams = l_annotated.getListener_streams();
		if(l_annotated.getMemoryInContainer_streams()!=null && !l_annotated.getMemoryInContainer_streams().equals(""))
			memoryInContainer_streams = l_annotated.getMemoryInContainer_streams();
		if(l_annotated.getProvider()!=null && !l_annotated.getProvider().equals(""))
			provider = l_annotated.getProvider();	
		if(l_annotated.getInstance_navigated()!=null && !l_annotated.getInstance_navigated().equals(""))
			instance_navigated = l_annotated.getInstance_navigated();
		if(l_annotated.getInstance_local_container()!=null && !l_annotated.getInstance_local_container().equals(""))
			instance_local_container = l_annotated.getInstance_local_container();	
		if(l_annotated.getInstance_scheduler_container()!=null && !l_annotated.getInstance_scheduler_container().equals(""))
			instance_scheduler_container = l_annotated.getInstance_scheduler_container();
		if(l_annotated.getInstance_onlysession()!=null && !l_annotated.getInstance_onlysession().equals(""))
			instance_onlysession = l_annotated.getInstance_onlysession();	
		if(l_annotated.getInstance_servletcontext()!=null && !l_annotated.getInstance_servletcontext().equals(""))
			instance_servletcontext = l_annotated.getInstance_servletcontext();
		
		Vector a_streams = new Vector(l_annotated.get_streams().values());

		int stream_order=0;
		HashMap _streams_order = new HashMap();
		for(int k=0;k<a_streams.size();k++){
			info_stream iStream = (info_stream)a_streams.get(k);
			info_stream old_stream = (info_stream)_b_streams.get(iStream.getName());
			if(old_stream!=null){
				_b_streams.remove(old_stream.getName());
				_streams_order.remove(Integer.valueOf(old_stream.getInt_order()));
				info_stream fromVinfo = (info_stream)util_find.findElementFromList(v_info_streams, old_stream.getName(), "name");
				if(fromVinfo!=null)
					v_info_streams.remove(fromVinfo);
				Vector app_action = new Vector(old_stream.get_apply_to_action().keySet());
				if(app_action.size()==0){
					int l=0;
					while(l< ((Vector)_b_streams_apply_to_actions.get("*")).size()){
						info_stream current = (info_stream)((Vector)_b_streams_apply_to_actions.get("*")).get(l);
						if(old_stream.getName().equals(current.getName())) ((Vector)_b_streams_apply_to_actions.get("*")).remove(l);
						else l++;
					}
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						int l=0;
						while(l< ((Vector)_b_streams_apply_to_actions.get(key)).size()){
							info_stream current = (info_stream)((Vector)_b_streams_apply_to_actions.get(key)).get(l);
							if(old_stream.getName().equals(current.getName())) ((Vector)_b_streams_apply_to_actions.get(key)).remove(l);
							else l++;
						}
					}
				}
				
			}
			_b_streams.put(iStream.getName(),iStream);
			if(iStream.getInt_order()==-1){
				while(_streams_order.get(Integer.valueOf(stream_order))!=null) stream_order++;
				_streams_order.put(Integer.valueOf(stream_order) , iStream.getName());
				stream_order++;
			}else{
				_streams_order.put(Integer.valueOf(iStream.getInt_order()) , iStream.getName());
				stream_order=iStream.getInt_order();
				stream_order++;
			}
		}

		Vector v_streams_order = new util_sort().sort(new  Vector(_streams_order.keySet()),"");
		for(int i=0;i<v_streams_order.size();i++){
			info_stream current = (info_stream)_b_streams.get(_streams_order.get(v_streams_order.get(i)));
			if(current!=null){
				Vector app_action = new Vector(current.get_apply_to_action().keySet());
				if(app_action.size()==0){
					String key="*";
					((Vector)_b_streams_apply_to_actions.get(key)).add(current);
				}else{
					for(int j=0;j<app_action.size();j++){
						String key=(String)app_action.get(j);
						if(_b_streams_apply_to_actions.get(key)==null) _b_streams_apply_to_actions.put(key,new Vector());
						((Vector)_b_streams_apply_to_actions.get(key)).add(current);
					}
				}
			}
		}
		
		//v_info_streams.addAll(new Vector(_streams.values()));
		v_info_streams = (new Vector(_b_streams.values()));
		v_info_streams = new util_sort().sort(v_info_streams,"int_order");
		
		
		Vector a_beans = new Vector(l_annotated.get_beans().values());
		int max_int_order = -1;
		if(v_info_beans!=null && v_info_beans.size()>0){
			try{
				max_int_order = ((info_bean)v_info_beans.get(v_info_beans.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_beans = new util_sort().sort(a_beans,"int_order");
			
		for(int i=0;i<a_beans.size();i++){
			if(max_int_order>-1) ((info_bean)a_beans.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_b_beans.put(((info_bean)a_beans.get(i)).getName(), a_beans.get(i));
		}
		v_info_beans = (new Vector(_b_beans.values()));
		v_info_beans = new util_sort().sort(v_info_beans,"int_order");
		
		
		
		Vector a_redirects = new Vector(l_annotated.get_redirects().values());
		max_int_order = -1;
		if(v_info_redirects!=null && v_info_redirects.size()>0){
			try{
				max_int_order = ((info_bean)v_info_redirects.get(v_info_redirects.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_redirects = new util_sort().sort(a_redirects,"int_order");
			
		for(int i=0;i<a_redirects.size();i++){
			if(max_int_order>-1) ((info_redirect)a_redirects.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_b_redirects.put(((info_redirect)a_redirects.get(i)).getPath(), a_redirects.get(i));
		}
		v_info_redirects = (new Vector(_b_redirects.values()));
		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");


		Vector a_actions = new Vector(l_annotated.get_actions().values());
		max_int_order = -1;
		if(v_info_actions!=null && v_info_actions.size()>0){
			try{
				max_int_order = ((info_action)v_info_actions.get(v_info_actions.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_actions = new util_sort().sort(a_actions,"int_order");
			
		for(int i=0;i<a_actions.size();i++){
			if(max_int_order>-1) ((info_action)a_actions.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_b_actions.put(((info_action)a_actions.get(i)).getPath(), a_actions.get(i));
			if(((info_action)a_actions.get(i)).get_calls().size()>0){
				Vector a_actioncalls = new Vector(((info_action)a_actions.get(i)).get_calls().values());
				for(int j=0;j<a_actioncalls.size();j++){
/*
					_b_actioncalls.put(
							((info_call)a_actioncalls.get(j)).getOwner()+
							((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
							((info_call)a_actioncalls.get(j)).getName(),
							a_actioncalls.get(j));
*/					
					if(((info_call)a_actioncalls.get(j)).getExposed()==null || ((info_call)a_actioncalls.get(j)).getExposed().size()==0){
						_b_actioncalls.put(
								((info_call)a_actioncalls.get(j)).getOwner()+
								((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
								((info_call)a_actioncalls.get(j)).getName(),
								a_actioncalls.get(j));
						if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
							_b_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getPath(),
									a_actioncalls.get(j));
					}else{
						for(int e=0;e<((info_call)a_actioncalls.get(j)).getExposed().size();e++){
							String suffix = "."+((info_call)a_actioncalls.get(j)).getExposed().get(e).toString();
							_b_actioncalls.put(
									((info_call)a_actioncalls.get(j)).getOwner()+
									((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
									((info_call)a_actioncalls.get(j)).getName()+suffix,
									a_actioncalls.get(j));
							if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
								_b_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getPath()+suffix,
										a_actioncalls.get(j));

						}
					}
					
					if(((info_call)a_actioncalls.get(j)).getRestmapping()!=null && ((info_call)a_actioncalls.get(j)).getRestmapping().size()>0){
						for(int r=0;r<((info_call)a_actioncalls.get(j)).getRestmapping().size();r++){
							info_rest iRest = (info_rest)((info_call)a_actioncalls.get(j)).getRestmapping().get(r);
							if(_b_restmapping.get(iRest.getPath())==null)
								_b_restmapping.put(iRest.getPath(), new Vector());
							((Vector)_b_restmapping.get(iRest.getPath())).add(iRest);
						}
					}
				}
				
			}
			if(((info_action)a_actions.get(i)).getRestmapping().size()>0){
				for(int r=0;r<((info_action)a_actions.get(i)).getRestmapping().size();r++){
					info_rest iRest = (info_rest)((info_action)a_actions.get(i)).getRestmapping().get(r);
					if(_b_restmapping.get(iRest.getPath())==null)
						_b_restmapping.put(iRest.getPath(), new Vector());
					((Vector)_b_restmapping.get(iRest.getPath())).add(iRest);
				}
			}
		
		}
		v_info_actions = (new Vector(_b_actions.values()));
		v_info_actions = new util_sort().sort(v_info_actions,"int_order");
		
		
		Vector a_transformations = new Vector(l_annotated.get_transformationoutput().values());
		max_int_order = -1;
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			try{
				max_int_order = ((info_transformation)v_info_transformationoutput.get(v_info_transformationoutput.size()-1)).getInt_order();
			}catch(Exception e){			
			}
		}
		if(max_int_order>-1)
			a_transformations = new util_sort().sort(a_transformations,"int_order");
			
		for(int i=0;i<a_transformations.size();i++){
			if(max_int_order>-1) ((info_transformation)a_transformations.get(i)).setOrder(String.valueOf(max_int_order+1+i));
			_b_transformationoutput.put(((info_transformation)a_transformations.get(i)).getName(), a_transformations.get(i));
		}
		v_info_transformationoutput = (new Vector(_b_transformationoutput.values()));
		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
		
		
		
	}
	private void builder_readFormElements(Node node) throws Exception{
		if(node==null) return;

		if(node.getNodeName().equals("action-config")){
			initTop(node);
		}
		if(node.getNodeName().equals("action-streams")){
			int stream_order=0;
			HashMap _b_streams_order = new HashMap();
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_stream iStream = new info_stream();
					iStream.init(node.getChildNodes().item(k),_b_redirects);
					if(iStream!=null){
						info_stream old_stream = (info_stream)_b_streams.get(iStream.getName());
						if(old_stream!=null){
							_b_streams.remove(old_stream.getName());
							_b_streams_order.remove(Integer.valueOf(old_stream.getInt_order()));
							info_stream fromVinfo = (info_stream)util_find.findElementFromList(v_info_streams, old_stream.getName(), "name");
							if(fromVinfo!=null)
								v_info_streams.remove(fromVinfo);
							Vector app_action = new Vector(old_stream.get_apply_to_action().keySet());
							if(app_action.size()==0){
								int l=0;
								while(l< ((Vector)_b_streams_apply_to_actions.get("*")).size()){
									info_stream current = (info_stream)((Vector)_b_streams_apply_to_actions.get("*")).get(l);
									if(old_stream.getName().equals(current.getName())) ((Vector)_b_streams_apply_to_actions.get("*")).remove(l);
									else l++;
								}
							}else{
								for(int j=0;j<app_action.size();j++){
									String key=(String)app_action.get(j);
									int l=0;
									while(l< ((Vector)_b_streams_apply_to_actions.get(key)).size()){
										info_stream current = (info_stream)((Vector)_b_streams_apply_to_actions.get(key)).get(l);
										if(old_stream.getName().equals(current.getName())) ((Vector)_b_streams_apply_to_actions.get(key)).remove(l);
										else l++;
									}

								}
							}

						}
						_b_streams.put(iStream.getName(),iStream);
						if(iStream.getInt_order()==-1){
							while(_b_streams_order.get(Integer.valueOf(stream_order))!=null) stream_order++;
							_b_streams_order.put(Integer.valueOf(stream_order) , iStream.getName());
							stream_order++;
						}else{
							_b_streams_order.put(Integer.valueOf(iStream.getInt_order()) , iStream.getName());
							stream_order=iStream.getInt_order();
							stream_order++;
						}
					}
				}
			}

			Vector v_b_streams_order = new util_sort().sort(new  Vector(_b_streams_order.keySet()),"");
			for(int i=0;i<v_b_streams_order.size();i++){
				info_stream current = (info_stream)_b_streams.get(_b_streams_order.get(v_b_streams_order.get(i)));
				if(current!=null){
					Vector app_action = new Vector(current.get_apply_to_action().keySet());
					if(app_action.size()==0){
						String key="*";
						((Vector)_b_streams_apply_to_actions.get(key)).add(current);
					}else{
						for(int j=0;j<app_action.size();j++){
							String key=(String)app_action.get(j);
							if(_b_streams_apply_to_actions.get(key)==null) _b_streams_apply_to_actions.put(key,new Vector());
							((Vector)_b_streams_apply_to_actions.get(key)).add(current);
						}
					}
				}
			}

//			v_info_streams.addAll(new Vector(_b_streams.values()));
			v_info_streams=(new Vector(_b_streams.values()));
			v_info_streams = new util_sort().sort(v_info_streams,"int_order");


		}
		if(node.getNodeName().equals("form-beans")){
			int order=0;
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_bean iBean = new info_bean();
					iBean.init(node.getChildNodes().item(k));
					order++;
					iBean.setOrder(Integer.valueOf(order).toString());
					if(iBean!=null) _b_beans.put(iBean.getName(),iBean);
				}
			}
//			v_info_beans.addAll(new Vector(_b_beans.values()));
			v_info_beans = (new Vector(_b_beans.values()));
			v_info_beans = new util_sort().sort(v_info_beans,"int_order");
		}
		if(node.getNodeName().equals("form-redirects")){
			int order=0;
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_redirect iRedirect = new info_redirect();
					iRedirect.init(node.getChildNodes().item(k));
					order++;
					iRedirect.setOrder(Integer.valueOf(order).toString());
					if(iRedirect!=null) _b_redirects.put(iRedirect.getPath(),iRedirect);
				}
			}
//			v_info_redirects.addAll(new Vector(_b_redirects.values()));
			v_info_redirects = (new Vector(_b_redirects.values()));
			v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");

		}

		if(node.getNodeName().equals("redirect-transformations")){
			int order=0;
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					if(node.getNodeName().toLowerCase().equals("transformationoutput")){
						info_transformation iTransformationoutput = new info_transformation();
						iTransformationoutput.init(node);
						order++;
						iTransformationoutput.setOrder(Integer.valueOf(order).toString());
						if(iTransformationoutput!=null) _b_transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
					}
				}
			}
//			v_info_transformationoutput.addAll(new Vector(_b_transformationoutput.values()));
			v_info_transformationoutput = (new Vector(_b_transformationoutput.values()));
			v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");

		}



		if(node.getNodeName().equals("action-mappings")){
			try{
				NamedNodeMap nnm = node.getAttributes();
				if (nnm!=null){
					for (int j=0;j<node.getAttributes().getLength();j++){
						String paramName = node.getAttributes().item(j).getNodeName();
						Node node_nnm =	nnm.getNamedItem(paramName);
						if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
					}
				}
			}catch(Exception e){
				new bsControllerException(e,iStub.log_DEBUG);
			}
			int order=0;
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_action iAction = new info_action();
					iAction.init(node.getChildNodes().item(k),_b_redirects,_b_beans);
					order++;
					iAction.setOrder(Integer.valueOf(order).toString());
					if(iAction!=null){
						_b_actions.put(iAction.getPath(),iAction);
						if(iAction.get_calls().size()>0){
							Vector a_actioncalls = new Vector(iAction.get_calls().values());
							for(int j=0;j<a_actioncalls.size();j++){
/*								
								_b_actioncalls.put(
										((info_call)a_actioncalls.get(j)).getOwner()+
										((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
										((info_call)a_actioncalls.get(j)).getName(),
										a_actioncalls.get(j));
								if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
									_b_actioncalls.put(
											((info_call)a_actioncalls.get(j)).getPath(),
											a_actioncalls.get(j));
*/								
								if(((info_call)a_actioncalls.get(j)).getExposed()==null || ((info_call)a_actioncalls.get(j)).getExposed().size()==0){
									_b_actioncalls.put(
											((info_call)a_actioncalls.get(j)).getOwner()+
											((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
											((info_call)a_actioncalls.get(j)).getName(),
											a_actioncalls.get(j));
									if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
										_b_actioncalls.put(
												((info_call)a_actioncalls.get(j)).getPath(),
												a_actioncalls.get(j));
								}else{
									for(int e=0;e<((info_call)a_actioncalls.get(j)).getExposed().size();e++){
										String suffix = "."+((info_call)a_actioncalls.get(j)).getExposed().get(e).toString();
										_b_actioncalls.put(
												((info_call)a_actioncalls.get(j)).getOwner()+
												((bsController.getAppInit().get_actioncall_separator()!=null)?bsController.getAppInit().get_actioncall_separator():"")  +
												((info_call)a_actioncalls.get(j)).getName()+suffix,
												a_actioncalls.get(j));
										if(((info_call)a_actioncalls.get(j)).getPath()!=null && !((info_call)a_actioncalls.get(j)).getPath().equals(""))
											_b_actioncalls.put(
													((info_call)a_actioncalls.get(j)).getPath()+suffix,
													a_actioncalls.get(j));

									}
								}
								if(((info_call)a_actioncalls.get(j)).getRestmapping()!=null && ((info_call)a_actioncalls.get(j)).getRestmapping().size()>0){
									for(int r=0;r<((info_call)a_actioncalls.get(j)).getRestmapping().size();r++){
										info_rest iRest = (info_rest)((info_call)a_actioncalls.get(j)).getRestmapping().get(r);
										if(_b_restmapping.get(iRest.getPath())==null)
											_b_restmapping.put(iRest.getPath(), new Vector());
										((Vector)_b_restmapping.get(iRest.getPath())).add(iRest);
									}
								}
							}
							
						}
						if(iAction.getRestmapping().size()>0){
							for(int r=0;r<iAction.getRestmapping().size();r++){
								info_rest iRest = (info_rest)iAction.getRestmapping().get(r);
								if(_b_restmapping.get(iRest.getPath())==null)
									_b_restmapping.put(iRest.getPath(), new Vector());
								((Vector)_b_restmapping.get(iRest.getPath())).add(iRest);
							}
						}
					}
				}
			}
//			v_info_actions.addAll(new Vector(_b_actions.values()));
			v_info_actions = (new Vector(_b_actions.values()));
			v_info_actions = new util_sort().sort(v_info_actions,"int_order");

		}
	}

	public HashMap get_b_beans() {
		return _b_beans;
	}

	public void set_b_beans(HashMap bBeans) {
		_b_beans = bBeans;
	}

	public HashMap get_b_actions() {
		return _b_actions;
	}

	public HashMap get_b_streams() {
		return _b_streams;
	}

	public HashMap get_b_streams_apply_to_actions() {
		return _b_streams_apply_to_actions;
	}

	public HashMap get_b_redirects() {
		return _b_redirects;
	}

	public HashMap get_b_transformationoutput() {
		return _b_transformationoutput;
	}

	public HashMap get_b_actioncalls() {
		return _b_actioncalls;
	}
	
	public HashMap get_b_restmapping() {
		return _b_restmapping;
	}	
}



















}
