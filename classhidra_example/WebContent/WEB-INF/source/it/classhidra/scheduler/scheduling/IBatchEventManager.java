package it.classhidra.scheduler.scheduling;

import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.i_batch_log;

public interface IBatchEventManager {
    public final static short EVENT_STILL_INEXEC = 1;
    public final static short EVENT_SUSPEND = 10;
    public final static short EVENT_OK = 0;
    public final static short EVENT_KO = 2;
    public final static short EVENT_INEXIST = 3;
    public final static short EVENT_INEXIST_WORKER = 4;
    public final static short ENGINE_KO = 200;
	
	void add(short event, db_batch batch, i_batch_log log);
	void add(short event, String idBatch, i_batch_log log); 
	void add(short event, String logMessage); 
}
