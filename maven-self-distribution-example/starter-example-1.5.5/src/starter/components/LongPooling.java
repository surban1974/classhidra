package starter.components; 

import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Async;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.ResponseHeader;
import it.classhidra.annotation.elements.SessionDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.serialize.JsonWriter;



@Action (
	path="pooling",
	name="model",
	redirect="/starter/pages/async.html",
	entity=@Entity(
			property="allway:public"
	),
	Expose=@Expose(methods = {Expose.POST,Expose.GET}),
	Async=@Async(true)
)


@SessionDirective
//@NavigatedDirective(memoryContent="true")
public class LongPooling extends action implements i_action, i_bean, Serializable{

	private static final long serialVersionUID = 1L;
	private Boolean pooling = null;
	private String initTime = null;
	private long stopPoolingTime = 0;

	public LongPooling(){
		super();
	}

	@Override
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, UnavailableException, bsControllerException {
		return super.actionservice(request, response);
	}

	@ActionCall(
			name="stop",
			Expose=@Expose(methods = {Expose.POST,Expose.GET})			
			)
	public void stop(HttpServletRequest request){
		setAsyncInterrupt(true);
		pooling=false;
		stopPoolingTime = new Date().getTime();
	}
	
	@ActionCall(
			name="status",
			Redirect=@Redirect(contentType="application/json"),
			Expose=@Expose(methods = {Expose.POST,Expose.GET})			
			)
	public String status(HttpServletRequest request){
		return JsonWriter.object2json(pooling, "pooling");
	}
	
	@ActionCall(
			name="check",
			Redirect=@Redirect(contentType="text/event-stream"),
			Expose=@Expose(methods = {Expose.POST,Expose.GET}),
			Async=@Async(
					value=true,
					flushBuffer=true,
					timeout=60000,
					loopEvery=5000,
					headers = {
							@ResponseHeader(name="Cache-Control",value="no-cache"),
							@ResponseHeader(name="Connection",value="keep-alive")
							}
					)
			)
	public String check(HttpServletRequest request){
		if(!pooling){
			if(stopPoolingTime!=0 && (new Date().getTime()-stopPoolingTime)>5000){
				stopPoolingTime=0;
				pooling = true;
			}
		}
//		try{
//			AsyncContainer asyncContainer = (AsyncContainer)request.getAttribute("AsyncContainer");
//			
//			if(asyncContainer!=null && asyncContainer.getAsyncContext()!=null){
//				asyncContainer.getAsyncContext().getResponse().getOutputStream().write("Check ".getBytes());
//				asyncContainer.getAsyncContext().getResponse().flushBuffer();
//			}
//		}catch(Exception e){
//			e.toString();
//		}
		return "{init: \" "+initTime+" \",current:\""+util_format.dataToString(new Date(), "yyyy-MM-dd HH:mm:sss")+"\" }";
	}	
	


	@Override
	public void init(HttpServletRequest request) throws bsControllerException {
		initTime = util_format.dataToString(new Date(), "yyyy-MM-dd HH:mm:sss");
		if(pooling==null)
			pooling = false;
		super.init(request);
	}

	public Boolean getPooling() {
		return pooling;
	}

	public void setPooling(Boolean pooling) {
		this.pooling = pooling;
	}		

}
