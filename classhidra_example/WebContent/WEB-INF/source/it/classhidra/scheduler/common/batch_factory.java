package it.classhidra.scheduler.common;


public class batch_factory {

	private String cls_batch;
	private String xml;

	
	public batch_factory( String _xml){
		super();
//		cls_batch=_cls_batch;
		xml=_xml;

	}

	public i_batch create_batch_(){
		i_batch result = null;
		
		try{
			result = (i_batch)Class.forName(cls_batch).newInstance();
			result.onBeforeReadInput(xml);
			result.readInput(xml);
			result.onAfterReadInput(xml);
		}catch(Exception e){
			
		}
		
		return result;
	}
}
