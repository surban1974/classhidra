package it.classhidra.core.tool.exception;

import javax.servlet.jsp.JspException;

public class bsTagEndRendering extends JspException {
	private static final long serialVersionUID = 1L;
	private String tagId;
	public bsTagEndRendering(String id) {
		super();
		tagId=id;
	}
	public String getTagId() {
		return tagId;
	}
}
