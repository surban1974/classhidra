package it.classhidra.core.controller;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

public class info_transformation extends info_entity implements i_elementBase{
	private static final long serialVersionUID = 1L;
	public static final String CONST_INPUTFORMAT_BYTE="BYTE";
	public static final String CONST_INPUTFORMAT_STRING="STRING";
	public static final String CONST_INPUTFORMAT_FORM="FORM";
	
	public static final String CONST_EVENT_BEFORE="BEFORE";
	public static final String CONST_EVENT_AFTER="AFTER";
	public static final String CONST_EVENT_BOTH="BOTH";

	private String name;
	private String type;
	private String path;
	private String event;
	private String inputformat;
	
	
	public info_transformation(){
		super();
		reimposta();
	}

	public void init(Node node) throws bsControllerException{
		if(node==null) return;
		try{
			NamedNodeMap nnm = node.getAttributes();	 		
			if (nnm!=null){
				for (int i=0;i<node.getAttributes().getLength();i++){
					String paramName = node.getAttributes().item(i).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					String sNodeValue = node_nnm.getNodeValue();
					if(sNodeValue!=null){
						sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
						sNodeValue = util_format.replace(sNodeValue, " ", "");
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
							
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
	}

	
	public void reimposta(){
		name="";
		type="";
		path="";
		event=CONST_EVENT_BEFORE;
		inputformat="string";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInputformat() {
		return inputformat;
	}

	public void setInputformat(String inputformat) {
		this.inputformat = inputformat;
	}	
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}		
	
	public String toString(){		
		return toXml();
	}
	
	public String toXml(){
		return toXml("");
	}
	
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <transformationoutput";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(type!=null && !type.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(type)+"\"";

		if(path!=null && !path.trim().equals("")) result+=" path=\""+util_format.normaliseXMLText(path)+"\"";
		if(event!=null && !event.trim().equals("")) result+=" event=\""+util_format.normaliseXMLText(event)+"\"";
		if(inputformat!=null && !inputformat.trim().equals("")) result+=" inputformat=\""+util_format.normaliseXMLText(inputformat)+"\"";
		result+=super.toXml();
		result+=" />";
		return result;
	}


}
