
package it.classhidra.core.controller;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_apply_to_action extends info_entity implements i_elementBase{


	private static final long serialVersionUID = -1L;
	private String action;
	private String excluded="false";

	
	public info_apply_to_action(){
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
		action="";
		excluded="false";
	}
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String toString(){		
		return toXml();
	}

	
	public String toXml(){
		return toXml("");
	}	
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <apply-to-action";
		if(action!=null && !action.trim().equals("")) result+=" action=\""+util_format.normaliseXMLText(action)+"\"";
		if(excluded!=null && !excluded.trim().equals("") && !excluded.trim().toLowerCase().equals("false")) result+=" excluded=\""+util_format.normaliseXMLText(excluded)+"\"";

		result+=super.toXml();
		result+=" />";
		
		return result;
	}

	public String getExcluded() {
		return excluded;
	}

	public void setExcluded(String excluded) {
		this.excluded = excluded;
	}		
}
