package examples.cdi.classhidra.framework.web.components;
import it.classhidra.annotation.elements.Action;

import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.statistic.I_StatisticProvider;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.statistic.StatisticProvider_Simple;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_sort;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Action (
		path="statistisc_json",
		statistic="false"
)


public class CdiComponentStatisticsJson extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6534122783978835682L;



public CdiComponentStatisticsJson(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

	if(middleAction==null) middleAction="";

	if(middleAction.equals("showasxml")){
		I_StatisticProvider provider = bsController.getStatisticProvider();
		if(provider!=null){
			String json=provider.getAllEntitiesAsXml();
			try{



				OutputStream out = response.getOutputStream();
				out.write("<textarea wrap='off' style='width:100%;height: 100%;overflow:scroll;border: solid 1px silver;' readonly='readonly'>".getBytes());
				out.write(json.getBytes());
				out.write("</textarea>".getBytes());
				out.flush();

			}catch(Exception e){
			}

		}
		return null;
	}

	try{
		I_StatisticProvider provider = bsController.getStatisticProvider();
		if(provider!=null){
			int length = 100;
			String json="";
			List<StatisticEntity> statistics = null;
			if(provider instanceof StatisticProvider_Simple){
				statistics = provider.getAllEntities();
				try{
					length = Integer.valueOf(bsController.getAppInit().get_statistic_stacklength()).intValue();
				}catch(Exception e){
				}
			}else{
				try{
					length = Integer.valueOf(bsController.getAppInit().get_statistic_stacklength()).intValue();
				}catch(Exception e){
				}
				statistics = provider.getLastEntities(length);
			}
			statistics = util_sort.sort(statistics, "st");
			long startX=-1;
			long finishX=-1;
			long totalDelta=0;
			long maxX=0;
			long maxY=0;
			for(StatisticEntity entity : statistics){
				if(startX==-1) startX=entity.getSt().getTime();
				finishX=entity.getSt().getTime();
				totalDelta+=entity.getDelta();
				if(entity.getDelta()>maxY) maxY=entity.getDelta();
				if(entity.getSt().getTime()-startX>maxX) maxX = entity.getSt().getTime()-startX;
			}

			if(startX==-1)
				json+=("{\"length\":"+length+",\"n\"=0,\"fr\"=0,\"dn\"=0,\"data\":[]}");
			else{
				json+=("{\"length\":"+length+",");
				json+=("\"n\":"+statistics.size()+",");
				json+=("\"fr\":"+(finishX-startX)/statistics.size()+",");
				json+=("\"dn\":"+totalDelta/statistics.size()+",");
				json+=("\"data\":[");
				boolean isFirst=true;
				for(StatisticEntity entity : statistics){
					if(!isFirst) json+=(",");
					else isFirst=false;
					json+=("{");
					json+=("\"x\":"+ (entity.getSt().getTime()-startX)*1000/maxX +",");
					json+=("\"y\":"+ entity.getDelta()*1000/maxY +",");
					json+=("\"vx\":\""+ util_format.dataToString(entity.getSt(), "dd/MM/yyyy HH:mm ss SSS") +"\",");
					json+=("\"vy\":\""+ entity.getDelta()+" ms" +"\"");
					json+=("}");
				}
				json+=("]}");
			}
			try{
				OutputStream out = response.getOutputStream();
				out.write(json.getBytes());
			}catch(Exception e){
			}
			return null;

		}
	}catch(Exception e){

	}

return null;
}

}
