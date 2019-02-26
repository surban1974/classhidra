package it.classhidra.core.tool.util;

import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_tag_helper;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;

public class util_tag {

	public static Object getBeanAsBSTag(String bean, Tag requested){
		Object result=null;
		
		if(bean.toUpperCase().indexOf("BS:")==0){
			Tag currentTag = requested;

			final StringTokenizer st = new StringTokenizer(bean.toUpperCase(),".");
			while(st.hasMoreTokens()){
				final String key = st.nextToken();
				if(key.indexOf("BS:")==0){
					final StringTokenizer st1 = new StringTokenizer(key,":");
					String className=null;
					if(st1.hasMoreTokens()){
						st1.nextToken();
						if(st1.hasMoreTokens()) className=st1.nextToken();
					}
					if(className==null) return result;
					className="it.classhidra.core.controller.tags.tag"+util_reflect.adaptMethodName(className.toLowerCase());
					currentTag=getPrevTag(currentTag, className,currentTag.getParent());
					if(currentTag==null) return result;
				}
			}
			result = currentTag;
		}		
		
		return result;
	}

	private static Tag getPrevTag(Tag current, String className, Tag parent){

		while(parent!=null){
			if(parent.getClass().getName().equals(className)) return parent;
			parent = parent.getParent();
		}
		return parent;
	}
	
	public static String getTagExecutor(final String callerClassName, final boolean fullpage) {
		if(bsController.getTagComponentRender()!=null) {
			final i_tag_helper helper = (i_tag_helper)bsController.getTagComponentRender().get_bean(i_tag_helper.CONST_TAG_HELPER);
			if(helper!=null)
				return helper.getTagExecutor(callerClassName, fullpage);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addTagExecutorObject(final String className, Object pageObject) {
		if(bsController.getTagComponentRender()!=null) {
			try {
				((Map<String,Object>)bsController.getTagComponentRender().get_bean(i_tag_helper.CONST_TAG_EXECUTORS))
						.put(className, pageObject);
				return true;
			}catch(Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
		}
		return false;
	}

	
	public static String getTagContent(final String key, final String info, final i_action action_instance, final HttpServletRequest request, final HttpServletResponse response) {
		if(bsController.getTagComponentRender()!=null) {
			final i_tag_helper helper = (i_tag_helper)bsController.getTagComponentRender().get_bean(i_tag_helper.CONST_TAG_HELPER);
			if(helper!=null)
				return helper.getTagContent(key, info, action_instance, request, response);
		}
		return null;
	}

	
}
