package examples.spring.classhidra.framework.web.components;

import static java.util.concurrent.TimeUnit.SECONDS;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEvent;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.Access;
import it.classhidra.annotation.elements.AccessRelation;
import it.classhidra.annotation.elements.Action;

@ActionMapping (
		redirects={
				@Redirect(	
						path="/jsp/framework/processBatch.jsp",
						descr="BATCH",
						mess_id="title_fw_Batch"
				)
				
			},
		actions={
				@Action (
						path="processBatch",
						name="formProcessBatch",
						redirect="/jsp/framework/processBatch.jsp",
						navigated="true",
						memoryInSession="true",
						reloadAfterAction="true",
						redirects={
								@Redirect(	
									auth_id="pb_id0",
									path="/jsp/ajax/view_LogBatch.jsp",
									navigated="false"
								),
								@Redirect(	
									auth_id="pb_id1",
									path="/jsp/ajax/jscript_LogBatch.jsp",
									navigated="false"
								),
								@Redirect(	
									auth_id="pb_id2",
									path="/jsp/framework/processBatch_detail.jsp",
									navigated="false"
								)								
								
						},
					    entity=@Entity(
							permissions=@Access(
									forbidden={
											@AccessRelation(targets="default_target;", rules="GUESTS;" )	
									}
							)
						)
				)
				
				
		}
)

@Bean (	name="formProcessBatch")
@NavigatedDirective(memoryContent="true")
@Component
@Scope("prototype")

public class SpringComponentProcessBatch extends action implements i_action, i_bean, Serializable{
	private static final long serialVersionUID = 1L;
	private Vector elements;
	private Vector elementslog;
	private Vector elementsview;
	private Vector elementssch;
	private Vector elementsthread;
	private String cd_btch;
	private String cd_p_btch;
	private db_batch selected;
	private db_batch parent;
	private Date next_scan;
	private String period_scan;
	private String res_scan;



public SpringComponentProcessBatch(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

	if(get_bean().getMiddleAction()==null) get_bean().setMiddleAction("");
	if(get_bean().getMiddleAction().equals("log")){
		logBatch(request);
		get_bean().setMiddleAction("");
		return new redirects("/jsp/ajax/view_LogBatch.jsp");
	}
	if(get_bean().getMiddleAction().equals("detail")){
//		logBatch(request);
		db_batch founded = findElement(get_bean().get("cd_btch"));
		get_bean().set("selected",founded);
		get_bean().setMiddleAction("");
		return new redirects("/jsp/framework/processBatch_detail.jsp");
	}	
	if(get_bean().getMiddleAction().equals("detail_update")){

		HashMap fBean = new HashMap();
		fBean.put("selected", get_bean().get("selected"));
		i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
		try{
			m4b.operation(i_4Batch.o_UPDATE, fBean);
		}catch(Exception e){
		}
		setSelected(null);
	}
	if(get_bean().getMiddleAction().equals("detail_remove")){

		HashMap fBean = new HashMap();
		fBean.put("cd_btch", get_bean().get("selected.cd_btch"));
		fBean.put("cd_ist", get_bean().get("selected.cd_ist"));
		
		i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
		try{
			m4b.operation(i_4Batch.o_DELETE, fBean);
		}catch(Exception e){
		}
		setSelected(null);
	}	
	if(get_bean().getMiddleAction().equals("clear_log")){
		clearLogBatch(request);
		get_bean().setMiddleAction("");
		return new redirects("/jsp/ajax/view_LogBatch.jsp");
	}
	if(get_bean().getMiddleAction().equals("update_batch")){
		if(updateBatch(request)){
		}else{
			logBatch(request);
			get_bean().setMiddleAction("");
			return new redirects("/jsp/ajax/view_LogBatch.jsp");
		}
	}
	if(get_bean().getMiddleAction().equals("exec_batch")){
		get_bean().setMiddleAction("");
		runBatch(request);
		try{
			SECONDS.sleep(5);
		}catch(Exception e){			
		}
	}
	if(get_bean().getMiddleAction().equals("view_img")){
		loadBatchsView(request);
		get_bean().setMiddleAction("");
		return new redirects("/jsp/ajax/jscript_LogBatch.jsp");
	}
	if(get_bean().getMiddleAction().equals("reInit")){
		try{
			DriverScheduling.reStart();
			SECONDS.sleep(5);
		}catch(Exception e){
		}


	}
	if(get_bean().getMiddleAction().equals("stop")){
		try{
			DriverScheduling.stop();
			SECONDS.sleep(5);
		}catch(Exception e){
		}
	}	

	loadBatchs(request);

	return new redirects(get_infoaction().getRedirect());
}

private void runBatch(HttpServletRequest request){
	db_batch founded = findElement(get_bean().get("cd_btch"));
	if(founded!=null){
		if(founded.getState().shortValue()==i_batch.STATE_INEXEC){
			i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
			HashMap fBean = new HashMap();
			fBean.put("cd_btch", get_bean().get("cd_btch"));

			try{
				m4b.operation("CLEAR_STATE", fBean);
			}catch(Exception e){
			}
		}else{
			ProcessBatchEvent pbe = new ProcessBatchEvent();
			pbe.launch(founded.getCd_ist(),founded.getCd_btch(),false, false);
		}
	}
	get_bean().set("cd_btch", "");

}

private void logBatch(HttpServletRequest request){

	Vector elementslog = new Vector();
	db_batch founded = findElement(get_bean().get("cd_btch"));

	get_bean().set("selected",founded);

	i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
	HashMap fBean = new HashMap();
	fBean.put("cd_btch", get_bean().get("cd_btch"));
	fBean.put("operation", "log");

	try{
		elementslog = (Vector)m4b.operation(i_4Batch.o_FINDFORMLIST, fBean);
	}catch(Exception e){
	}

	get_bean().set("elementslog",elementslog);

}

private void loadBatchsView(HttpServletRequest request){

	Vector elementsview = new Vector();

	i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
	HashMap fBean = new HashMap();
	fBean.put("cd_btch", get_bean().get("cd_p_btch"));


	try{
		elementsview = (Vector)m4b.operation(i_4Batch.o_FINDFORMLIST, fBean);
	}catch(Exception e){
	}

	get_bean().set("elementsview",elementsview);


}

private void clearLogBatch(HttpServletRequest request){

	Vector elementslog = new Vector();
	db_batch founded = findElement(get_bean().get("cd_btch"));

	get_bean().set("selected",founded);

	i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
	HashMap fBean = new HashMap();
	fBean.put("cd_btch", get_bean().get("cd_btch"));
	fBean.put("operation", "log");

	try{
		elementslog = (Vector)m4b.operation(i_4Batch.o_DELETE, fBean);
	}catch(Exception e){
	}

	get_bean().set("elementslog",elementslog);

}

private boolean updateBatch(HttpServletRequest request){

	db_batch founded = findElement(get_bean().get("cd_btch"));

	get_bean().set("selected",founded);

	Boolean result = new Boolean(false);

	HashMap fBean = new HashMap();
	fBean.put("selected", get_bean().get("selected"));

	i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();



	try{
		result = (Boolean)m4b.operation(i_4Batch.o_UPDATE, fBean);
	}catch(Exception e){
	}

//	if(result.booleanValue()){
//		DriverScheduling.reStart();
//	}

	return result.booleanValue();

}

private void loadBatchs(HttpServletRequest request){

	SpringComponentProcessBatch form = (SpringComponentProcessBatch)get_bean();
	Vector elements = new Vector();

	i_4Batch m4b = DriverScheduling.getConfiguration().get4BatchManager();
	HashMap fBean = new HashMap();
//	fBean.put("cd_ist",get_bean().getCurrent_auth().get_target_property().get(bsConstants.CONST_AUTH_TARGET_ISTITUTION));
	fBean.put("cd_btch", get_bean().get("cd_p_btch"));
	db_batch founded = null;
	try{
		founded = (db_batch)m4b.operation(i_4Batch.o_FIND, fBean);
	}catch(Exception e){		
	}

	form.setParent(founded);

	try{
		elements = (Vector)m4b.operation(i_4Batch.o_FINDFORMLIST, fBean);
	}catch(Exception e){
	}

	form.setElements(elements);
	try{

		founded = new db_batch();

		if(DriverScheduling.getThProcess()!=null){
		
			get_bean().set("next_scan",DriverScheduling.getThProcess().getScan_time());
			
			batch_init b_init = DriverScheduling.getConfiguration();
			long period_scan = 0;
			try{	
				period_scan = new Long(b_init.get_sleep()).longValue();
			}catch(Exception ex){
				
			}
			
			
	
			long dif = period_scan;
			dif=dif/1000;
			
			int min = (int)dif/60;
			int sec = (int)(dif - min*60);
			get_bean().set("period_scan",min+":"+sec);
	
			
			dif = ((Date)get_bean().get("next_scan")).getTime() - new Date().getTime();
			dif=dif/1000;
			
			min = (int)dif/60;
			sec = (int)(dif - min*60);
			get_bean().set("res_scan",min+":"+sec);
		}else{
			form.setNext_scan(null);
		}
		
	}catch(Exception e){

	}
	

	
	try{
		Vector threads = new Vector(DriverScheduling.getThProcess().getPbe().getContainer_threadevents());
		get_bean().set("elementsthread",threads);
	}catch(Exception e){
		
	}	
	


}
public db_batch findElement( Object obj_value){
	Vector elements = (Vector)get_bean().get("elements");
	if(obj_value==null ) return null;
	for(int i=0;i<elements.size();i++){
			db_batch el = (db_batch)elements.get(i);
			if(el!=null && obj_value.equals(el.getCd_btch())) return el;
	}
	return null;
}

public void reimposta(){
	elements = new Vector();
	elementslog = new Vector();
	elementsview = new Vector();
	elementssch = new Vector();
	elementsthread = new Vector();
	cd_btch="";
	cd_p_btch="";
	period_scan="";
	res_scan="";
}

public Vector getElements() {
	return elements;
}

public void setElements(Vector elements) {
	this.elements = elements;
}

public String getCd_btch() {
	return cd_btch;
}

public void setCd_btch(String cdBtch) {
	cd_btch = cdBtch;
}

public String getCd_p_btch() {
	return cd_p_btch;
}

public void setCd_p_btch(String cdPBtch) {
	cd_p_btch = cdPBtch;
}

public db_batch getSelected() {
	return selected;
}

public void setSelected(db_batch selected) {
	this.selected = selected;
}

public db_batch getParent() {
	return parent;
}

public void setParent(db_batch parent) {
	this.parent = parent;
}

public Vector getElementslog() {
	return elementslog;
}

public void setElementslog(Vector elementslog) {
	this.elementslog = elementslog;
}

public Vector getElementsview() {
	return elementsview;
}

public void setElementsview(Vector elementsview) {
	this.elementsview = elementsview;
}

public Vector getElementssch() {
	return elementssch;
}

public void setElementssch(Vector elementssch) {
	this.elementssch = elementssch;
}

public Date getNext_scan() {
	return next_scan;
}

public void setNext_scan(Date nextScan) {
	next_scan = nextScan;
}

public String getRes_scan() {
	return res_scan;
}

public void setRes_scan(String resScan) {
	res_scan = resScan;
}

public void setPeriod_scan(String periodScan) {
	period_scan = periodScan;
}

public String getPeriod_scan() {
	return period_scan;
}

public Vector getElementsthread() {
	return elementsthread;
}

public void setElementsthread(Vector elementsthread) {
	this.elementsthread = elementsthread;
}




}
