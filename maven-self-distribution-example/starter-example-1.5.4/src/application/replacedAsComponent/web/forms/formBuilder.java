package application.replacedAsComponent.web.forms;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_apply_to_action;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.controller.info_item;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.info_transformation;
import it.classhidra.core.controller.load_actions;
import it.classhidra.framework.web.beans.option_element;



public class formBuilder extends bean implements i_bean{
	private static final long serialVersionUID = -4499306352896225554L;
	private load_actions l_actions;
	private String id_selected_action;
	private String id_selected_stream;
	private String id_selected_redirect;
	private String id_selected_bean;
	private String id_selected_transformationoutput;
	private String id_selected_apply_to_action;
	private String id_selected_item;
	private String id_selected_section;
	private String type_selected;

	private info_action selected_action;
	private info_stream selected_stream;
	private info_redirect selected_redirect;
	private info_bean selected_bean;
	private info_transformation selected_transformationoutput;
	private info_apply_to_action selected_apply_to_action;
	private info_item selected_item;
	private info_section selected_section;

	private Vector elements_true_false;
	private Vector elements_transformationoutput_event;
	private Vector elements_transformationoutput_inputformat;
	private Vector elements_transformationoutput_outputformat;
	private Vector elements_all_redirects;

	private Vector elements_item_types;

	private String xmlContent;

	private boolean display_streams=true;
	private boolean display_actions=true;
	private boolean display_beans=false;
	private boolean display_redirects=false;




public void reimposta(){
	id_selected_action="";
	id_selected_stream="";
	id_selected_redirect="";
	id_selected_bean="";
	id_selected_transformationoutput="";
	id_selected_apply_to_action="";
	id_selected_item="";
	id_selected_section="";
	type_selected="";


	elements_true_false = new Vector();
	elements_true_false.add(new option_element("false","false"));
	elements_true_false.add(new option_element("true","true"));

	elements_transformationoutput_event = new Vector();
	elements_transformationoutput_event.add(new option_element("before","before"));
	elements_transformationoutput_event.add(new option_element("after","after"));
	elements_transformationoutput_event.add(new option_element("both","both"));

	elements_transformationoutput_inputformat = new Vector();
	elements_transformationoutput_inputformat.add(new option_element("byte","byte"));
	elements_transformationoutput_inputformat.add(new option_element("string","string"));
	elements_transformationoutput_inputformat.add(new option_element("form","form"));

	elements_transformationoutput_outputformat = new Vector();
	elements_transformationoutput_outputformat.add(new option_element("byte","byte"));
	elements_transformationoutput_outputformat.add(new option_element("string","string"));

	elements_item_types = new Vector();
	elements_item_types.add(new option_element("java.lang.Integer","java.lang.Integer"));
	elements_item_types.add(new option_element("java.lang.Short","java.lang.Short"));
	elements_item_types.add(new option_element("java.lang.Long","java.lang.Long"));
	elements_item_types.add(new option_element("java.lang.Float","java.lang.Float"));
	elements_item_types.add(new option_element("java.lang.Double","java.lang.Double"));
	elements_item_types.add(new option_element("java.lang.Byte","java.lang.Byte"));
	elements_item_types.add(new option_element("java.lang.Boolean","java.lang.Boolean"));
	elements_item_types.add(new option_element("java.lang.String","java.lang.String"));
	elements_item_types.add(new option_element("java.lang.Character","java.lang.Character"));
	elements_item_types.add(new option_element("java.util.Vector","java.util.Vector"));
	elements_item_types.add(new option_element("java.util.HashMap","java.util.HashMap"));

	elements_all_redirects=new Vector();

	xmlContent="";

	l_actions=new load_actions();

}

public redirects validate(HttpServletRequest request){
	return null;
}

public load_actions getL_actions() {
	return l_actions;
}

public void setL_actions(load_actions lActions) {
	l_actions = lActions;
}

public String getId_selected_action() {
	return id_selected_action;
}

public void setId_selected_action(String idSelectedAction) {
	id_selected_action = idSelectedAction;
}

public String getId_selected_stream() {
	return id_selected_stream;
}

public void setId_selected_stream(String idSelectedStream) {
	id_selected_stream = idSelectedStream;
}

public String getId_selected_redirect() {
	return id_selected_redirect;
}

public void setId_selected_redirect(String idSelectedRedirect) {
	id_selected_redirect = idSelectedRedirect;
}

public String getId_selected_bean() {
	return id_selected_bean;
}

public void setId_selected_bean(String idSelectedBean) {
	id_selected_bean = idSelectedBean;
}

public String getId_selected_transformationoutput() {
	return id_selected_transformationoutput;
}

public void setId_selected_transformationoutput(String idSelectedTransformation) {
	id_selected_transformationoutput = idSelectedTransformation;
}

public info_action getSelected_action() {
	return selected_action;
}

public void setSelected_action(info_action selectedAction) {
	selected_action = selectedAction;
}

public info_stream getSelected_stream() {
	return selected_stream;
}

public void setSelected_stream(info_stream selectedStream) {
	selected_stream = selectedStream;
}

public info_redirect getSelected_redirect() {
	return selected_redirect;
}

public void setSelected_redirect(info_redirect selectedRedirect) {
	selected_redirect = selectedRedirect;
}

public info_bean getSelected_bean() {
	return selected_bean;
}

public void setSelected_bean(info_bean selectedBean) {
	selected_bean = selectedBean;
}

public info_transformation getSelected_transformationoutput() {
	return selected_transformationoutput;
}

public void setSelected_transformationoutput(
		info_transformation selectedTransformation) {
	selected_transformationoutput = selectedTransformation;
}

public String getType_selected() {
	return type_selected;
}

public void setType_selected(String typeSelected) {
	type_selected = typeSelected;
}

public String getId_selected_apply_to_action() {
	return id_selected_apply_to_action;
}

public void setId_selected_apply_to_action(String idSelectedApplyToAction) {
	id_selected_apply_to_action = idSelectedApplyToAction;
}

public info_apply_to_action getSelected_apply_to_action() {
	return selected_apply_to_action;
}

public void setSelected_apply_to_action(
		info_apply_to_action selectedApplyToAction) {
	selected_apply_to_action = selectedApplyToAction;
}

public Vector getElements_true_false() {
	return elements_true_false;
}

public void setElements_true_false(Vector elementsTrueFalse) {
	elements_true_false = elementsTrueFalse;
}

public Vector getElements_transformationoutput_event() {
	return elements_transformationoutput_event;
}

public void setElements_transformationoutput_event(
		Vector elementsTransformationoutputEvent) {
	elements_transformationoutput_event = elementsTransformationoutputEvent;
}

public Vector getElements_transformationoutput_inputformat() {
	return elements_transformationoutput_inputformat;
}

public void setElements_transformationoutput_inputformat(
		Vector elementsTransformationoutputInputformat) {
	elements_transformationoutput_inputformat = elementsTransformationoutputInputformat;
}

public Vector getElements_transformationoutput_outputformat() {
	return elements_transformationoutput_outputformat;
}

public void setElements_transformationoutput_outputformat(
		Vector elementsTransformationoutputOutputformat) {
	elements_transformationoutput_outputformat = elementsTransformationoutputOutputformat;
}

public String getXmlContent() {
	return xmlContent;
}

public void setXmlContent(String xmlContent) {
	this.xmlContent = xmlContent;
}

public boolean isDisplay_streams() {
	return display_streams;
}

public void setDisplay_streams(boolean displayStreams) {
	display_streams = displayStreams;
}

public boolean isDisplay_actions() {
	return display_actions;
}

public void setDisplay_actions(boolean displayActions) {
	display_actions = displayActions;
}

public boolean isDisplay_beans() {
	return display_beans;
}

public void setDisplay_beans(boolean displayBeans) {
	display_beans = displayBeans;
}

public boolean isDisplay_redirects() {
	return display_redirects;
}

public void setDisplay_redirects(boolean displayRedirects) {
	display_redirects = displayRedirects;
}

public String getId_selected_item() {
	return id_selected_item;
}

public void setId_selected_item(String idSelectedItem) {
	id_selected_item = idSelectedItem;
}

public info_item getSelected_item() {
	return selected_item;
}

public void setSelected_item(info_item selectedItem) {
	selected_item = selectedItem;
}

public Vector getElements_item_types() {
	return elements_item_types;
}

public void setElements_item_types(Vector elementsItemTypes) {
	elements_item_types = elementsItemTypes;
}

public Vector getElements_all_redirects() {
	return elements_all_redirects;
}

public void setElements_all_redirects(Vector elementsAllRedirects) {
	elements_all_redirects = elementsAllRedirects;
}

public String getId_selected_section() {
	return id_selected_section;
}

public void setId_selected_section(String idSelectedSection) {
	id_selected_section = idSelectedSection;
}

public info_section getSelected_section() {
	return selected_section;
}

public void setSelected_section(info_section selectedSection) {
	selected_section = selectedSection;
}



}
