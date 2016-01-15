/**
* Creation date: (09/01/2016)
* @author: Svyatoslav Urbanovych surban@bigmir.net  svyatoslav.urbanovych@gmail.com
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
package wrappers.ejb;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.log.statistic.I_StatisticProvider;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton(name=app_init.id_statistic_provider)
@Local(I_StatisticProvider.class)
public class StatisticProvider_EjbSimple implements I_StatisticProvider {
	
	private LinkedList stack = new LinkedList();
	
	public StatisticProvider_EjbSimple(){
		super();
		bsController.writeLog("Instanced Ejb StatisticProvider -> "+this.getClass().getName(), iStub.log_INFO);
	}

	public void addStatictic(StatisticEntity stat) {
		int stacklength = bsConstants.CONST_LEN_STATISTIC_STACK;
		try{
			stacklength = Integer.valueOf(bsController.getAppInit().get_statistic_stacklength()).intValue();
		}catch(Exception e){
		}
		if(stack.size()>stacklength) stack.removeLast();
		stack.addFirst(stat);
	}

	public List getAllEntities() {
		if(stack==null){
			stack = new LinkedList();
		}
		return stack;
	}

	public String getAllEntitiesAsXml() {
		if(stack==null){
			stack = new LinkedList();
			return "";
		} 
		else{
			String result="";
			ListIterator  it = stack.listIterator();
		    while(it.hasNext()){
		    	try{
		    		result= ((StatisticEntity)it.next()).toXml()+"\n"+result;
		    	}catch (Exception e) {
				}
		    }
		    return result;
		}
	}

	public List getLastEntities(int quantity) {
		if(stack==null){
			stack = new LinkedList();
		} 
		LinkedList stackQ = new LinkedList();
		int q=0;
		while(q<quantity && q<stack.size()){
			stackQ.addFirst(stack.get(q));
			q++;
		}
		return stackQ;	
	}

	public String getLastEntitiesAsXml(int quantity) {
		LinkedList stack = (LinkedList)getLastEntities(quantity);
		if(stack==null){
			stack = new LinkedList();
			return "";
		} 
		else{
			String result="";
		    ListIterator  it = stack.listIterator();
		    while(it.hasNext()){
		    	try{
		    		result= ((StatisticEntity)it.next()).toXml()+"\n"+result;
		    	}catch (Exception e) {
				}
		    }
		    return result;
		}
	}

	public void clearAll(){
		if(stack==null){
			stack = new LinkedList();
		} 
		stack.clear();		
	}
	
	public void syncroAll(){
	}

	public List getEntities(String commandDefinedIntoProviderRelise) {
		return null;
	}

	public String getEntitiesAsXml(String commandDefinedIntoProviderRelise) {
		return null;
	}
}
