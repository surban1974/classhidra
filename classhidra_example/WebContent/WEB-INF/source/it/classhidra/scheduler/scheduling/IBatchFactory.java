package it.classhidra.scheduler.scheduling;

import it.classhidra.scheduler.common.i_batch;

public interface IBatchFactory {
	i_batch getInstance(String cd_btch, String cls_batch);
}
