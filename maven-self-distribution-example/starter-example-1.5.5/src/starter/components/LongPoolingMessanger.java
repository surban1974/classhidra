package starter.components; 

import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.cmex.CmexContainer;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Async;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.Parameter;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.ResponseHeader;
import it.classhidra.annotation.elements.SessionDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsContext;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.serialize.JsonWriter;
//import static java.util.concurrent.TimeUnit.SECONDS;



@Action (
	path="messanger",
	name="model",
	redirect="/starter/pages/messanger.html",
	entity=@Entity(
			property="allway:public"
	),
	Expose=@Expose(methods = {Expose.POST,Expose.GET})
	,Async=@Async(true)
)


@SessionDirective
public class LongPoolingMessanger extends action implements i_action, i_bean, Serializable{

	private static final long serialVersionUID = 1L;
	protected CmexContainer<String> topic;
	protected Boolean pooling = null;
	protected long stopPoolingTime = 0;
	protected final static String subscribing = "CMEX/CHAT";
	protected final static long subscribingMaxLifeTimeMillis = -1;//30*1000;

	public LongPoolingMessanger(){
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
	public String stop(HttpServletRequest request){
		setAsyncInterrupt(true);
		pooling=false;
		stopPoolingTime = new Date().getTime();
		return "";
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
			name="push",
			Redirect=@Redirect(contentType="application/json"),
			Expose=@Expose(methods = {Expose.POST,Expose.GET})			
			)
	public String push(@Parameter(name="message") String message){
		if(topic!=null)
			topic.subscribeAndPush(subscribing, message, subscribingMaxLifeTimeMillis);
		return "ok";
	}
	
	@ActionCall(
			name="check",
			Redirect=@Redirect(contentType="text/event-stream"),
			Expose=@Expose(methods = {Expose.POST,Expose.GET}),
			Async=@Async(
					value=true,
					flushBuffer=false,
					timeout=10*60000,
					loopEvery=0,
					headers = {
							@ResponseHeader(name="Cache-Control",value="no-cache"),
							@ResponseHeader(name="Connection",value="keep-alive")
							}
					)
					
			)
	public String check(bsContext context) throws Exception{
		if(!pooling){
			if(stopPoolingTime!=0){
				stopPoolingTime=0;
				pooling = true;
			}
		}
		context.write("Start ".getBytes());
		for(int i=0;i<1018;i++)
			context.write(" ".getBytes());
		context.flushBuffer();
		while(topic!=null && pooling){	
			String message = topic.waitForMessage(subscribing, subscribingMaxLifeTimeMillis);
			if(message!=null){
				context.write(new Date().toString().getBytes());
				context.write(message.getBytes());
				context.flushBuffer();
			}
/*			
			Queue<String> list = topic.subscribe(subscribing, subscribingMaxLifeTimeMillis);
			if(list!=null){
				synchronized (list) {
					while (list.isEmpty()) {
						list.wait();
					}
					String message = list.poll();
					if(message!=null){
						context.write(new Date().toString().getBytes());
						context.write(message.getBytes());
						context.flushBuffer();
					}
				}
			}
*/
		}
		if(topic!=null)
			topic.unsubscribe(subscribing);		
		return "";
	}	
	



	@Override
	public void init(HttpServletRequest request) throws bsControllerException {
		if(pooling==null)
			pooling = false;
		super.init(request);
		try{
			topic = CmexContainer.topic(request.getSession());
		}catch(Exception e){			
		}
		
	}

	public Boolean getPooling() {
		return pooling;
	}

	public void setPooling(Boolean pooling) {
		this.pooling = pooling;
	}		

}
