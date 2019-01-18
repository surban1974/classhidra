package it.classhidra.core.controller;


import java.util.Vector;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_menu_element extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -4335618503251976686L;
	public String id;
	public String action;
	public String auth_action;
	public String descr;
	public String mess_id;
	public String img;
	public String type;
	public String load;
	
	private info_menu_element parent;
	private Vector children;
	private String externalloader;


	
	public info_menu_element() {
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
		
	public info_menu_element find(String id){
		if(this.getId().equals(id)) return this;
		for(int i=0;i<children.size();i++){
			info_menu_element founded = ((info_menu_element)children.get(i)).find(id);
			if(founded!=null) return founded;
		}
		return null;
	}
	public boolean remove(String id){

		for(int i=0;i<children.size();i++){
			info_menu_element founded = (info_menu_element)children.get(i);
			if(founded.getId().equals(id)){
				children.remove(i);
				return true;
			}
		}
		return false;
	}	
	
	public boolean move(String id,int position){

		for(int i=0;i<children.size();i++){
			info_menu_element founded = (info_menu_element)children.get(i);
			if(founded.getId().equals(id)){
				try{
					info_menu_element forchange = (info_menu_element)children.get(i+position);
					children.set(i+position, founded);
					children.set(i,forchange);
					return true;
				}catch(Exception e){
					return false;
				}
			}
		}
		return false;
	}		
	
	public boolean add(info_menu_element newNode){
		children.add(newNode);
		return true;
	}	

	
	public void reimposta(){
		id="";
		action="menuCreator";
		auth_action="";
		descr="";
		mess_id="";
		img="";
		type="static";
		load="PUT";
		children=new Vector();
		externalloader="";
	}

	public String toXml(){
		String spaces="   ";
		info_menu_element tmp_parent = parent;
		while(tmp_parent!=null){
			spaces+="   ";
			tmp_parent=tmp_parent.getParent();
		}
		String result=System.getProperty("line.separator")+spaces+"<element";
		result+=" id=\""+util_format.normaliseXMLText(getId())+"\"";
		result+=" action=\""+util_format.normaliseXMLText(getAction())+"\"";
		if(auth_action!=null && auth_action.trim().length()>0)
			result+=" auth_action=\""+util_format.normaliseXMLText(getAuth_action())+"\"";
		result+=" descr=\""+util_format.normaliseXMLText(getDescr())+"\"";
		result+=" mess_id=\""+util_format.normaliseXMLText(getMess_id())+"\"";
		result+=" img=\""+util_format.normaliseXMLText(getImg())+"\"";
		result+=" type=\""+util_format.normaliseXMLText(getType())+"\"";
		result+=" load=\""+util_format.normaliseXMLText(getLoad())+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		
		for(int i=0;i<this.getChildren().size();i++){
			isEntity=true;
			info_menu_element current = (info_menu_element)this.getChildren().get(i);
			result+=current.toXml();
		}
		
		if(isEntity)
			result+=System.getProperty("line.separator")+spaces+"</element>";
		else result+="</element>";

		
		return result;
	}
	
	public String getAction() {
		return action;
	}

	public String getDescr() {
		return descr;
	}

	public String getId() {
		return id;
	}

	public String getImg() {
		return img;
	}

	public String getMess_id() {
		return mess_id;
	}

	public void setAction(String string) {
		action = string;
	}

	public void setDescr(String string) {
		descr = string;
	}

	public void setId(String string) {
		id = string;
	}

	public void setImg(String string) {
		img = string;
	}
	
	public void setImg(Object string) {
		if(string!=null && string instanceof String) img = (String)string;
	}	

	public void setMess_id(String string) {
		mess_id = string;
	}

	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String string) {
		load = string;
	}
	public info_menu_element getParent() {
		return parent;
	}
	public void setParent(info_menu_element parent) {
		this.parent = parent;
	}
	public Vector getChildren() {
		return children;
	}
	public void setChildren(Vector children) {
		this.children = children;
	}
	public String getExternalloader() {
		return externalloader;
	}
	public void setExternalloader(String externalloader) {
		this.externalloader = externalloader;
	}
	public String getAuth_action() {
		return auth_action;
	}
	public void setAuth_action(String authAction) {
		auth_action = authAction;
	}


}
