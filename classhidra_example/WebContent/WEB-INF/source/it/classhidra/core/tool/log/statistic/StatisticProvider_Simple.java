package it.classhidra.core.tool.log.statistic;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StatisticProvider_Simple implements I_StatisticProvider {
	private static final long serialVersionUID = 1L;

	public void addStatictic(StatisticEntity stat) {
		@SuppressWarnings("unchecked")
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null){
			stack = new LinkedList<StatisticEntity>();
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

	public List<StatisticEntity> getAllEntities() {
		@SuppressWarnings("unchecked")
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return new LinkedList<StatisticEntity>();
		else return stack;
	}

	public String getAllEntitiesAsXml() {
		@SuppressWarnings("unchecked")
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return "";
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
		@SuppressWarnings("unchecked")
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack==null) return new LinkedList<StatisticEntity>();
		else{
			LinkedList<StatisticEntity> stackQ = new LinkedList<StatisticEntity>();
			int q=0;
			while(q<quantity && q<stack.size()){
				stackQ.addFirst(stack.get(q));
				q++;
			}
			return stackQ;	
		}
	}

	public String getLastEntitiesAsXml(int quantity) {
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)getLastEntities(quantity);
		if(stack==null) return "";
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
		@SuppressWarnings("unchecked")
		LinkedList<StatisticEntity> stack = (LinkedList<StatisticEntity>)bsController.getFromLocalContainer(bsConstants.CONST_ID_STATISTIC_STACK);
		if(stack!=null) stack.clear();		
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
