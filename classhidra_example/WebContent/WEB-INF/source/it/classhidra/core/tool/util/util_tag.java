package it.classhidra.core.tool.util;

import java.util.StringTokenizer;

import javax.servlet.jsp.tagext.Tag;

public class util_tag {

	public static Object getBeanAsBSTag(String bean, Tag requested){
		Object result=null;
		
		if(bean.toUpperCase().indexOf("BS:")==0){
			Tag currentTag = requested;

			StringTokenizer st = new StringTokenizer(bean.toUpperCase(),".");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				if(key.indexOf("BS:")==0){
					StringTokenizer st1 = new StringTokenizer(key,":");
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

	private static Tag getPrevTag(Tag current, String className,Tag parent){

		while(parent!=null){
			if(parent.getClass().getName().equals(className)) return parent;
			parent = parent.getParent();
		}
		return parent;
	}
	
}
