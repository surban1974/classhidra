package it.classhidra.scheduler.scheduling;

import java.io.Serializable;

import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;

public interface IBatchScheduling extends Serializable{

	void start();

	void reScan();

	void reStart();

	void stop();

	void clearContainer();
	
	void kill4timeout();

	batch_init getConfiguration();

	ProcessBatchEngine getPbe();

	schedulingThreadProcess getThProcess();
	
	boolean isActive();

}