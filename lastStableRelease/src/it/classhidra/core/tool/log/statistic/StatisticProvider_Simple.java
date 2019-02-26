package it.classhidra.core.tool.log.statistic;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StatisticProvider_Simple implements I_StatisticProvider {
	private static final long serialVersionUID = 1L;

	public void addStatictic(StatisticEntity stat) {
		LinkedList stack = (LinkedList)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null){
			stack = new LinkedList();
			bsController.putToLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK,stack);
		}
		int stacklength = bsConstants.CONST_LEN_STATISTIC_STACK;
		try{
			stacklength = Integer.valueOf(bsController.getAppInit().get_statistic_stacklength()).intValue();
		}catch(Exception e){
		}
		if(stack.size()>stacklength) stack.removeLast();
		stack.addFirst(stat);
	}

	public List getAllEntities() {
		LinkedList stack = (LinkedList)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return new LinkedList();
		else return stack;
	}

	public String getAllEntitiesAsXml() {
		LinkedList stack = (LinkedList)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return "";
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
		LinkedList stack = (LinkedList)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return new LinkedList();
		else{
			LinkedList stackQ = new LinkedList();
			int q=0;
			while(q<quantity && q<stack.size()){
				stackQ.addFirst(stack.get(q));
				q++;
			}
			return stackQ;	
		}
	}

	public String getLastEntitiesAsXml(int quantity) {
		LinkedList stack = (LinkedList)getLastEntities(quantity);
		if(stack==null) return "";
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
		LinkedList stack = (LinkedList)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack!=null) stack.clear();		
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
