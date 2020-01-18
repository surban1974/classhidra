package it.classhidra.scheduler.common;

import java.io.Serializable;

import it.classhidra.scheduler.scheduling.db.db_batch;

public interface i_4Period extends Serializable{
	String calcolatePeriod(String currentTime, String periodTime, long increased, db_batch batch, int applicant);
}
