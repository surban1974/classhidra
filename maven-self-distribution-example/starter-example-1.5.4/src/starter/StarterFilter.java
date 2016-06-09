package starter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsFilter;

@WebFilter(
		filterName="StarterFilter",
		value={
				"/actions/*",
				"/*"
		},
		initParams={
				@WebInitParam(name="CharacterEncoding",value="UTF-8"),
				@WebInitParam(name="ExcludedUrl",value="/js/;/css/;/images/;"),
				@WebInitParam(name="ExcludedPattern",value="^(?!.*/neohort/).*\\.jsp$"),
				@WebInitParam(name="RestSupport",value="true")
		},
		
		dispatcherTypes={
				DispatcherType.REQUEST,
				DispatcherType.FORWARD,
				DispatcherType.INCLUDE
		},
		asyncSupported = true
	)
public class StarterFilter extends bsFilter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws ServletException, IOException {
	

		if(req!=null && req instanceof HttpServletRequest && resp!=null && resp instanceof HttpServletResponse){
		

			bsController.isInitialized();

			super.doFilter(req, resp, chain);
		}else{
            chain.doFilter(req, resp);
            return;			
		}
	}

}
