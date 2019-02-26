package it.classhidra.core.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface i_tag_helper extends Serializable{
	public final static String CONST_TAG_EXECUTORS = "TAG_EXECUTORS";
	public final static String CONST_TAG_HELPER = "TAG_HELPER";
	public final static String CONST_TAG_PAGE_CONTEXT = "TAG_PAGE_CONTEXT";
	public final static String CONST_TAG_COMPONENT_ID = "TAG_COMPONENT_ID";
	public final static String CONST_TAG_RENDERING_FULL = "fullpage";
	public final static String CONST_TAG_RENDERING_SINGLE = "single";
	
	String getTagExecutor(final String callerClassName, final boolean fullpage);
	String getTagContent(final String key, final String info, final i_action action_instance, final HttpServletRequest request, final HttpServletResponse response);
}
