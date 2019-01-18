/**
* Creation date: (31/05/2016)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;



import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

public class info_async extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -1L;
	private String value;
	private String timeout;	
	private String flushBuffer;
	private String loopEvery;
	private String reInitBeenEveryLoop;

	private List headers;
	private String bsIdAction;
	private String bsIdCall;
	private String bsIdComplete;



	
	public info_async(){
		super();
		reimposta();
	}

	public info_async init(Node node) throws bsControllerException{
		if(node==null) return this;
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
			NodeList nodeList = node.getChildNodes();
			
			for(int i=0;i<nodeList.getLength();i++){
				if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
					if(nodeList.item(i).getNodeName().toLowerCase().equals("header")){
						info_header iHeader = new info_header();
						iHeader.init(nodeList.item(i));
						headers.add(iHeader);
					}
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
		return this;
	}

	public void reimposta(){
		value="";
		timeout="";	
		flushBuffer="";
		loopEvery="";
		headers=new ArrayList();
	}


	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		return toXml("");
	}
	
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <async";
		if(value!=null && !value.trim().equals("")) result+=" value=\""+util_format.normaliseXMLText(value)+"\"";
		if(timeout!=null && !timeout.trim().equals("")) result+=" timeout=\""+util_format.normaliseXMLText(timeout)+"\"";
		if(flushBuffer!=null && !flushBuffer.trim().equals("")) result+=" flushBuffer=\""+util_format.normaliseXMLText(flushBuffer)+"\"";
		if(loopEvery!=null && !loopEvery.trim().equals("")) result+=" loopEvery=\""+util_format.normaliseXMLText(loopEvery)+"\"";
		if(reInitBeenEveryLoop!=null && !reInitBeenEveryLoop.trim().equals("")) result+=" reInitBeenEveryLoop=\""+util_format.normaliseXMLText(reInitBeenEveryLoop)+"\"";

		result+=super.toXml();
		if(headers==null && headers.size()==0)
			result+="/>";
		else{
			result+=">";
			for(int i=0;i<headers.size();i++){
				info_header iHeader = (info_header)headers.get(i);
				if(iHeader!=null)
					result+=iHeader.toXml(space+"            ");
				
			}
			result+=System.getProperty("line.separator")+space+"      </async>";
		}
		return result;
	}

	public String getValue() {
		return value;
	}

	public info_async setValue(String value) {
		this.value = value;
		return this;
	}

	public String getTimeout() {
		return timeout;
	}

	public info_async setTimeout(String timeout) {
		this.timeout = timeout;
		return this;
	}

	public String getFlushBuffer() {
		return flushBuffer;
	}

	public info_async setFlushBuffer(String flushBuffer) {
		this.flushBuffer = flushBuffer;
		return this;
	}

	public String getLoopEvery() {
		return loopEvery;
	}

	public info_async setLoopEvery(String loopEvery) {
		this.loopEvery = loopEvery;
		return this;
	}

	public List getHeaders() {
		return headers;
	}

	public info_async setHeaders(List headers) {
		this.headers = headers;
		return this;
	}
	
	public String getReInitBeenEveryLoop() {
		return reInitBeenEveryLoop;
	}

	public info_async setReInitBeenEveryLoop(String reInitBeenEveryLoop) {
		this.reInitBeenEveryLoop = reInitBeenEveryLoop;
		return this;
	}	

	public info_async setInfoBs(String id_action,String id_call,String id_complete){
		bsIdAction = id_action;
		bsIdCall = id_call;
		bsIdComplete = id_complete;
		return this;
	}

	public String getBsIdCall() {
		return bsIdCall;
	}

	public void setBsIdCall(String bsIdCall) {
		this.bsIdCall = bsIdCall;
	}

	public String getBsIdAction() {
		return bsIdAction;
	}

	public String getBsIdComplete() {
		return bsIdComplete;
	}



	



	
}
