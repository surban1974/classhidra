package it.classhidra.core.controller;

import it.classhidra.core.controller.info_entity;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_nodeorganization extends info_entity implements i_elementBase{
	private static final long serialVersionUID = 1L;
	
	public static String VISIBILITY_PARENT = "P";
	public static String VISIBILITY_CURRENT_LEVEL = "L";
	public static String VISIBILITY_BOTTOM = "B";
	public static String VISIBILITY_ONLY_SELF = "S";
	public static String VISIBILITY_ALL = "A";
	
	private String id_node;
	private String matriculation;
	private String visibility;
	private String mess_id;
	private String description;
	private HashMap _permitted;
	private Vector v_permitted;
	private HashMap _forbiden;
	private Vector v_forbiden;
	private info_nodeorganization parent;
	private Vector children;
	private HashMap _children;
	private HashMap _children_m;
	private int level=0;
	
	public info_nodeorganization(){
		super();
		reimposta();
	}	

	public void reimposta(){
		id_node="";
		matriculation="";
		visibility="SB";
		mess_id="";
		description="";
		_permitted=new HashMap();
		_forbiden=new HashMap();
		v_permitted=new Vector();
		v_forbiden=new Vector();
		_children=new HashMap();
		_children_m=new HashMap();
		children=new Vector();
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
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
							
				}
			}
			for(int i=0;i<node.getChildNodes().getLength();i++){
				if(node.getChildNodes().item(i).getNodeType()== Node.ELEMENT_NODE) 
					readFormElements(node.getChildNodes().item(i));
			}
			
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
		refreshV_elements();
	}

	
	private void readFormElements(Node node) throws Exception{
		if(node==null) return;
		
		if(node.getNodeName().equals("node-organization")){
			info_nodeorganization infoNode = new info_nodeorganization();
			infoNode.setLevel(this.getLevel()+1);
			infoNode.init(node);
			if(infoNode.getId_node().equals("")) infoNode.setId_node(infoNode.getMatriculation());
			infoNode.setParent(this);
			children.add(infoNode);
			_children.put(infoNode.getId_node(), infoNode);
			_children_m.put(infoNode.getMatriculation(), infoNode);
		}
		if(node.getNodeName().equals("exception-permitted") || node.getNodeName().equals("exception-allowed")){
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_nodeorganization infoNodePermitted = new info_nodeorganization();
					infoNodePermitted.init(node);
					if(infoNodePermitted.getId_node().equals("")) infoNodePermitted.setId_node(infoNodePermitted.getMatriculation());
					_permitted.put(infoNodePermitted.getId_node(), infoNodePermitted);
				}	
			}
		}	
		if(node.getNodeName().equals("exception-forbidden")){
			for(int k=0;k<node.getChildNodes().getLength();k++){
				if(node.getChildNodes().item(k).getNodeType()== Node.ELEMENT_NODE){
					info_nodeorganization infoNodeForbidden = new info_nodeorganization();
					infoNodeForbidden.init(node);
					if(infoNodeForbidden.getId_node().equals("")) infoNodeForbidden.setId_node(infoNodeForbidden.getMatriculation());
					_forbiden.put(infoNodeForbidden.getId_node(), infoNodeForbidden);
				}	
			}
		}	

	}	
	
	public void refreshV_elements(){
		v_forbiden = new Vector(_forbiden.values());
		v_permitted = new Vector(_permitted.values());
	}		
	
	public info_nodeorganization find(String _id_node){
		if(this.id_node.equals(_id_node)) return this;
		if(_children.get(_id_node)!=null) return (info_nodeorganization)_children.get(_id_node);
		
		for(int i=0;i<children.size();i++){
			info_nodeorganization current = (info_nodeorganization)children.get(i);
			info_nodeorganization founded = current.find(_id_node);
			if(founded!=null) return founded;
		}
		return null;
	}
	
	public info_nodeorganization find_m(String _matriculation){
		if(this.matriculation.equals(_matriculation)) return this;
		if(_children_m.get(_matriculation)!=null) return (info_nodeorganization)_children_m.get(_matriculation);
		
		for(int i=0;i<children.size();i++){
			info_nodeorganization current = (info_nodeorganization)children.get(i);
			info_nodeorganization founded = current.find_m(_matriculation);
			if(founded!=null) return founded;
		}
		return null;
	}	
	
	public HashMap getElementsVisible(){
		HashMap result=new HashMap();
		if(visibility.indexOf(VISIBILITY_ONLY_SELF)>-1) result.put(this.getMatriculation(),this);
		if(visibility.indexOf(VISIBILITY_PARENT)>-1 && parent!=null) result.put(parent.getMatriculation(),parent);
		if(visibility.indexOf(VISIBILITY_CURRENT_LEVEL)>-1 && parent!=null){
			for(int i=0;i<parent.getChildren().size();i++){
				info_nodeorganization current = (info_nodeorganization)parent.getChildren().get(i);
				if(!current.getId_node().equals(this.id_node))
					result.put(current.getMatriculation(),current);
			}	
		}
		if(visibility.indexOf(VISIBILITY_BOTTOM)>-1){
			result = getElementsAll(result);
		}
		if(visibility.indexOf(VISIBILITY_ALL)>-1){
			if(parent==null){
				result.put(this.getMatriculation(),this);
				result = getElementsAll(result);
			}else{
				info_nodeorganization top_parent = parent;
				while(top_parent.getParent()!=null) top_parent = top_parent.getParent();
				result = top_parent.getElementsAll(result);
			}
		}
		if(_forbiden.size()>0){
			Vector forbiden = new Vector(_forbiden.keySet());
			for(int i=0;i<forbiden.size();i++){
				String key = (String)forbiden.get(i);
				result.remove(key);
			}
		}
		if(_permitted.size()>0){
			Vector permitted = new Vector(_permitted.keySet());
			for(int i=0;i<permitted.size();i++){
				String key = (String)permitted.get(i);
				if(result.get(key)==null)
					result.put(((info_nodeorganization)_permitted.get(key)).getMatriculation(),_permitted.get(key));
			}
		}		
		return result;
	}
	
	public HashMap getElementsAll(HashMap result){
		for(int i=0;i<this.getChildren().size();i++){
			info_nodeorganization current = (info_nodeorganization)this.getChildren().get(i);
			result.put(current.getMatriculation(),current);
			result = current.getElementsAll(result);
		}
		return result;
	}
	
	public String getId_node() {
		return id_node;
	}

	public void setId_node(String idNode) {
		id_node = idNode;
	}

	public String getMatriculation() {
		return matriculation;
	}

	public void setMatriculation(String matriculation) {
		this.matriculation = matriculation;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap get_permitted() {
		return _permitted;
	}

	public void set_permitted(HashMap permitted) {
		_permitted = permitted;
	}

	public HashMap get_forbiden() {
		return _forbiden;
	}

	public void set_forbiden(HashMap forbiden) {
		_forbiden = forbiden;
	}

	public Vector getChildren() {
		return children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}

	public info_nodeorganization getParent() {
		return parent;
	}

	public void setParent(info_nodeorganization parent) {
		this.parent = parent;
	}

	public HashMap get_children() {
		return _children;
	}

	public void set_children(HashMap children) {
		_children = children;
	}

	public HashMap get_children_m() {
		return _children_m;
	}

	public void set_children_m(HashMap childrenM) {
		_children_m = childrenM;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String toHTMLList(String classStyleVis,String classStyleDis, String prefix, HashMap visibilityMap, boolean onlyVis){
		String result="";
		String levelnbsp="";
		if(!onlyVis  || (onlyVis && visibilityMap.get(matriculation)!=null)){
			for(int i=0;i<level;i++) levelnbsp+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			String _class="";
			if(!onlyVis){
				if(visibilityMap.get(matriculation)!=null) _class=classStyleVis;
				else _class=classStyleDis;
			}else{				
				_class=classStyleVis;
			}
			result+=levelnbsp+prefix+"<span id=\""+matriculation+"\" class=\""+_class+"\">"+matriculation+" - "+description+"</span><br>";
		}
		for(int i=0;i<this.getChildren().size();i++){
			info_nodeorganization current = (info_nodeorganization)this.getChildren().get(i);
			result+=current.toHTMLList(classStyleVis, classStyleDis, prefix, visibilityMap, onlyVis);
		}
		
		return result;
	}
	
	public String toXml(){
		String spaces="   ";
		info_nodeorganization tmp_parent = parent;
		while(tmp_parent!=null){
			spaces+="   ";
			tmp_parent=tmp_parent.getParent();
		}
		String result=System.getProperty("line.separator")+spaces+"<node-organization";
		result+=" id_node=\""+util_format.normaliseXMLText(getId_node())+"\"";
		result+=" matriculation=\""+util_format.normaliseXMLText(getMatriculation())+"\"";
		result+=" visibility=\""+util_format.normaliseXMLText(getVisibility())+"\"";
		result+=" mess_id=\""+util_format.normaliseXMLText(getMess_id())+"\"";
		result+=" description=\""+util_format.normaliseXMLText(getDescription())+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		if(v_forbiden!=null && v_forbiden.size()>0){
			isEntity=true;
			result=System.getProperty("line.separator")+spaces+"   <exception-forbidden>";
			for(int i=0;i<v_forbiden.size();i++){
				info_nodeorganization iNode = (info_nodeorganization)v_forbiden.get(i);
				if(iNode!=null) result+=iNode.toXml();
			}
			result=System.getProperty("line.separator")+spaces+"   </exception-forbidden>";
		}

		if(v_permitted!=null && v_permitted.size()>0){
			isEntity=true;
			result=System.getProperty("line.separator")+spaces+"   <exception-allowed>";
			for(int i=0;i<v_permitted.size();i++){
				info_nodeorganization iNode = (info_nodeorganization)v_permitted.get(i);
				if(iNode!=null) result+=iNode.toXml();
			}
			result=System.getProperty("line.separator")+spaces+"   </exception-allowed>";
		}
		
		for(int i=0;i<this.getChildren().size();i++){
			isEntity=true;
			info_nodeorganization current = (info_nodeorganization)this.getChildren().get(i);
			result+=current.toXml();
		}
		
		if(isEntity)
			result+=System.getProperty("line.separator")+spaces+"</node-organization>";
		else result+="</node-organization>";

		
		return result;
	}

	public String getMess_id() {
		return mess_id;
	}

	public void setMess_id(String messId) {
		mess_id = messId;
	}

	public Vector getV_permitted() {
		return v_permitted;
	}

	public void setV_permitted(Vector vPermitted) {
		v_permitted = vPermitted;
	}

	public Vector getV_forbiden() {
		return v_forbiden;
	}

	public void setV_forbiden(Vector vForbiden) {
		v_forbiden = vForbiden;
	}
}
