package it.classhidra.core.tool.util.v2;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.ref.WeakReference;

import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;

public class Util_gc {
	public static String memoryHeapUsage(){
		MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = memory.getHeapMemoryUsage();
		return
				"Init: "+humanReadableByteCount(heap.getInit(),false)+";Used: "+humanReadableByteCount(heap.getUsed(),false)+";Commited: "+humanReadableByteCount(heap.getCommitted(),false)+";Max: "+humanReadableByteCount(heap.getMax(),false)+";";

	}

	public static void GC(){
		try{
			Object obj = new Object();
			WeakReference<Object> ref = new WeakReference<Object>(obj);
			obj = null;
			while(ref.get()!=null)
				System.gc();
		}catch(Exception ex){
			new bsControllerException(ex, iStub.log_ERROR);
		}catch (Throwable  th) {
			new bsControllerException(th, iStub.log_ERROR);
		}
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
