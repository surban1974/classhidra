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
package wrappers.cdi;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.log.statistic.I_StatisticProvider;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.stubs.iStub;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@Named(app_init.id_statistic_provider)
@ApplicationScoped
public class StatisticProvider_CdiSimple implements I_StatisticProvider {
	private static final long serialVersionUID = 1L;
	private static LinkedList<StatisticEntity> stack = new LinkedList<StatisticEntity>();
	
	public StatisticProvider_CdiSimple(){
		super();
		bsController.writeLog("Instanced Cdi StatisticProvider -> "+this.getClass().getName(), iStub.log_INFO);
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

	public List<StatisticEntity> getAllEntities() {
		if(stack==null){
			stack = new LinkedList<StatisticEntity>();
		}
		return stack;
	}

	public String getAllEntitiesAsXml() {
		if(stack==null){
			stack = new LinkedList<StatisticEntity>();
			return "";
		} 
		else{
			String result="";
			ListIterator<StatisticEntity>  it = stack.listIterator();
		    while(it.hasNext()){
		    	try{
		    		result= ((StatisticEntity)it.next()).toXml()+"\n"+result;
		    	}catch (Exception e) {
				}
		    }
		    return result;
		}
	}

	public List<StatisticEntity> getLastEntities(int quantity) {
		if(stack==null){
			stack = new LinkedList<StatisticEntity>();
		} 
		LinkedList<StatisticEntity> stackQ = new LinkedList<StatisticEntity>();
		int q=0;
		while(q<quantity && q<stack.size()){
			stackQ.addFirst(stack.get(q));
			q++;
		}
		return stackQ;	
	}

	public String getLastEntitiesAsXml(int quantity) {
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)getLastEntities(quantity);
		if(stack==null){
			stack = new LinkedList<StatisticEntity>();
			return "";
		} 
		else{
			String result="";
		    ListIterator<StatisticEntity>  it = stack.listIterator();
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
			stack = new LinkedList<StatisticEntity>();
		} 
		stack.clear();		
	}
	
	public void syncroAll(){
	}

	public List<StatisticEntity> getEntities(String commandDefinedIntoProviderRelise) {
		return null;
	}

	public String getEntitiesAsXml(String commandDefinedIntoProviderRelise) {
		return null;
	}
}
