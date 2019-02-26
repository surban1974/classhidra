package it.classhidra.scheduler.scheduling;

import java.io.Serializable;

import it.classhidra.scheduler.common.i_batch;

public interface IBatchFactory extends Serializable{
	i_batch getInstance(String cd_btch, String cls_batch);
}
