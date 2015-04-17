package it.classhidra.scheduler.common;

public interface listener_batch {
	public void onBeforeReadInput(String xml);
	public void onAfterReadInput(String xml);
	public String onBeforeExecute();
	public String onAfterExecute();
	public String onErrorExecute();
	public String onBeforeWriteOutput();
	public String onAfterWriteOutput();	
}
