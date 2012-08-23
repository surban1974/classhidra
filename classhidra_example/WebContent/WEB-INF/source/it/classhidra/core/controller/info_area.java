//29/06/2006 Zillo Ascio
package it.classhidra.core.controller;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_area extends info_entity implements i_elementBase{
	private static final long serialVersionUID = 4385407302199602175L;
	private String area;
	private String settore;
	
	public info_area(){
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
			new bsControllerException(e,iStub.log_DEBUG);
		}
	}

	
	public void reimposta(){
		area="";
		settore="";
	}
	public String getArea() {
		return area;
	}
	public void setArea(String string) {
		area = string;
	}
	public String getSettore() {
		return settore;
	}
	public void setSettore(String string) {
		settore = string;
	}

	
}
